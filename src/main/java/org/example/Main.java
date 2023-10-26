package org.example;

import org.example.model.PlayerCard;
import org.example.util.Scraper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        PlayerCard playerCard = scraper.getCardData();
    }
}