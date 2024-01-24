package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.Position;
import org.example.model.CardInput;
import org.example.model.MetaInfo;
import org.example.model.MetaInfoWrapper;
import org.example.model.PlayerCard;
import org.example.model.PriceResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    private static final Logger LOG = LogManager.getLogger(Scraper.class);

    private Element infoContentTable;

    public Scraper() {

    }

    public PriceResult getPriceData() {
        PriceResult priceResult = new PriceResult();
        try {
            HttpURLConnection httpURLConnection = getHttpURLConnection();

            // Check if the response code is 200 (HTTP OK)
            if (httpURLConnection.getResponseCode() == 200) {
                // Create an ObjectMapper from Jackson library
                ObjectMapper objectMapper = new ObjectMapper();
                priceResult = objectMapper.readValue(httpURLConnection.getInputStream(), PriceResult.class);
            } else {
                System.err.println("HTTP Error: " + httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage());
            }

            // Close the connection
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return priceResult;
    }

    private static HttpURLConnection getHttpURLConnection() throws IOException {
        String baseUrl = "https://sbccruncher.cc/api/prices?platform=pc&datasource=futbin";

        URL url = new URL(baseUrl);

        // Open a connection to the URL
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // Set request method and other properties if needed
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0;Win64) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");

        // Connect to the URL
        httpURLConnection.connect();
        return httpURLConnection;
    }

    public PlayerCard getCardData(CardInput cardInput) throws IOException {
        PlayerCard playerCard = new PlayerCard();
        String cardUrl = "https://www.futbin.com/24/player/" + cardInput.getFutBinId();
        Connection connection = Jsoup.connect(cardUrl);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0;Win64) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");
        Document doc = connection.get();

        infoContentTable = doc.getElementById("info_content").child(0).child(0);

        Element divElement = doc.selectFirst("#page-info");
        String currentPosition = divElement.attr("data-position");
        Position currPosition = null;
        try {
            currPosition = mapPosition(currentPosition);
        } catch (Exception ex) {
            LOG.error("Unknown position found " + currentPosition + " for card id " + cardInput.getFutBinId());
        }

        Element thElement = doc.select("div.pcdisplay-alt-pos").first();
        if (thElement != null) {
            assert thElement.parent() != null;
            String tdText = thElement.text();
            String[] positionsArray = tdText.split(" ");
            List<Position> positionsList = new ArrayList<>();
            for (String position : positionsArray) {
                try {
                    if (!position.isEmpty() && !position.isBlank()) {
                        positionsList.add(mapPosition(position));
                    }
                } catch (Exception ex) {
                    LOG.error("Unknown position found " + position + " for card id " + cardInput.getFutBinId());
                }
            }
            positionsList.add(currPosition);
            playerCard.setPositions(positionsList);
        }

        playerCard.setNation(extractAttribute("Nation"));
        playerCard.setId(Integer.valueOf(extractAttribute("ID")));
        playerCard.setClubId(Integer.valueOf(extractAttribute("Club ID")));
        playerCard.setLeagueId(Integer.valueOf(extractAttribute("League ID")));

        try {
            // Create a URL object for your playerUrl
            HttpURLConnection httpURLConnection = getHttpURLConnection(cardInput, currPosition);

            // Check if the response code is 200 (HTTP OK)
            if (httpURLConnection.getResponseCode() == 200) {
                // Create an ObjectMapper from Jackson library
                ObjectMapper objectMapper = new ObjectMapper();

                // Read the JSON response from the connection's input stream
                List<MetaInfo> metaInfoList;
                if (currPosition == Position.GK) {
                    MetaInfoWrapper metaInfoWrapper = objectMapper.readValue(httpURLConnection.getInputStream(), MetaInfoWrapper.class);
                    metaInfoList = metaInfoWrapper.getMetaRatings();
                    metaInfoList = metaInfoList.stream()
                            .filter(metaInfo -> metaInfo.getChemstyleId() == 272 ||
                                    (metaInfo.getChemstyleId() == 250 && metaInfo.getChemistry() == 0))
                            .toList();

                } else {
                    metaInfoList = objectMapper.readValue(httpURLConnection.getInputStream(), objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, MetaInfo.class));
                    if (cardInput.getEvoId() == null || cardInput.getEvoId().isBlank() || cardInput.getEvoId().isEmpty()) {
                        metaInfoList = metaInfoList.stream().filter(MetaInfo::isBestChemstyleAtChem).toList();
                    }
                }

                // Now you have a list of MetaInfo objects from the JSON response
                playerCard.setMetaInfoList(metaInfoList);
            } else {
                System.err.println("HTTP Error: " + httpURLConnection.getResponseCode() + " " + httpURLConnection.getResponseMessage());
            }

            // Close the connection
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerCard;
    }

    private static HttpURLConnection getHttpURLConnection(CardInput cardInput, Position currPosition) throws IOException {
        HttpURLConnection httpURLConnection = getUrlConnection(cardInput, currPosition);

        // Set request method and other properties if needed
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0;Win64) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");

        // Connect to the URL
        httpURLConnection.connect();
        return httpURLConnection;
    }

    private static HttpURLConnection getUrlConnection(CardInput cardInput, Position currPosition) throws IOException {
        String baseUrl = currPosition == Position.GK
                ? "https://s5epq42mpa.eu-west-1.awsapprunner.com/players/" + cardInput.getEasySbcId() + "?playerRole=gk"
                : "https://s5epq42mpa.eu-west-1.awsapprunner.com/squad-builder/meta-ratings?resourceId="
                + cardInput.getEasySbcId() + "&allChemStyles=1";
        if (cardInput.getEvoId() != null && !cardInput.getEvoId().isBlank() && !cardInput.getEvoId().isEmpty()) {
            baseUrl += "&evolutionPath=" + cardInput.getEvoId() + "&allChemStyles=1";
        }

        URL url = new URL(baseUrl);

        // Open a connection to the URL
        return (HttpURLConnection) url.openConnection();
    }

    private String extractAttribute(String attributeName) {
        Element thElement = infoContentTable.select("th:contains(" + attributeName + ")").first();
        if (thElement != null) {
            Element tdElement = thElement.parent().select("td").first();
            if (tdElement != null) {
                return tdElement.text();
            }
        }
        return "";
    }

    private Position mapPosition(String position) throws Exception {
        switch (position) {
            case "GK" -> {
                return Position.GK;
            }
            case "RB" -> {
                return Position.RB;
            }
            case "LB" -> {
                return Position.LB;
            }
            case "CB" -> {
                return Position.CB;
            }
            case "CDM" -> {
                return Position.CDM;
            }
            case "RWB" -> {
                return Position.RWB;
            }
            case "LWB" -> {
                return Position.LWB;
            }
            case "RM" -> {
                return Position.RM;
            }
            case "LM" -> {
                return Position.LM;
            }
            case "CM" -> {
                return Position.CM;
            }
            case "CAM" -> {
                return Position.CAM;
            }
            case "RW" -> {
                return Position.RW;
            }
            case "LW" -> {
                return Position.LW;
            }
            case "CF" -> {
                return Position.CF;
            }
            case "ST" -> {
                return Position.ST;
            }
            default -> throw new Exception();
        }
    }
}
