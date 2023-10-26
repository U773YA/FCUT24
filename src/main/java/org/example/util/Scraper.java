package org.example.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.Position;
import org.example.model.MetaInfo;
import org.example.model.PlayerCard;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.URL;
import java.net.HttpURLConnection;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    private static final Logger LOG = LogManager.getLogger(Scraper.class);

    private Element infoContentTable;

    public Scraper() {

    }

    public PlayerCard getCardData() throws IOException {
        PlayerCard playerCard = new PlayerCard();
        String cardUrl = "https://www.futbin.com/24/player/106";
        Connection connection = Jsoup.connect(cardUrl);
        connection.userAgent("Mozilla/5.0 (Windows NT 10.0;Win64) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");
        Document doc = connection.get();

        Element playerStatsJson = doc.getElementById("player_stats_json");
        infoContentTable = doc.getElementById("info_content").child(0).child(0);

        Element divElement = doc.selectFirst("#page-info");
        String currentPosition = divElement.attr("data-position");
        Position currPosition = null;
        try {
            currPosition = mapPosition(currentPosition);
        } catch (Exception ex) {
            LOG.error("Unknown position found " + currentPosition + " for card id " + 106);
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
                    LOG.error("Unknown position found " + position + " for card id " + 106);
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
            HttpURLConnection httpURLConnection = getHttpURLConnection();

            // Check if the response code is 200 (HTTP OK)
            if (httpURLConnection.getResponseCode() == 200) {
                // Create an ObjectMapper from Jackson library
                ObjectMapper objectMapper = new ObjectMapper();

                // Read the JSON response from the connection's input stream
                List<MetaInfo> metaInfoList = objectMapper.readValue(httpURLConnection.getInputStream(), objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, MetaInfo.class));

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

    private static HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("https://s5epq42mpa.eu-west-1.awsapprunner.com/squad-builder/meta-ratings?resourceId=226302&allChemStyles=1");

        // Open a connection to the URL
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // Set request method and other properties if needed
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0;Win64) AppleWebkit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");

        // Connect to the URL
        httpURLConnection.connect();
        return httpURLConnection;
    }

    private String extractAttribute(String attributeName) {
        Element thElement = infoContentTable.select("th:contains(" + attributeName + ")").first();
        if (thElement != null) {
            Element tdElement = thElement.parent().select("td").first();
            if (tdElement != null) {
                String tdText = tdElement.text();
                return tdText;
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
