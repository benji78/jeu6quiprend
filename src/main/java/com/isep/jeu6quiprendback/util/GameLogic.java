package com.isep.jeu6quiprendback.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GameLogic {

    private boolean gameStarted = false;
    private Player[] players;
    private CardStack[] cardStacks;
    // array that will contain the cards that the players have selected
    private Card[] selectedCards = new Card[2];

    public JSONArray start() {
        if (gameStarted)
            throw new IllegalStateException("Game already started");
        players = new Player[2];
        players[0] = new Player("Player1");
        players[1] = new Player("Bot");

        cardStacks = Cards.distributeRandomCards(players);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getJsonCardStacks());
        jsonArray.put(getJsonScoreBoard());
        jsonArray.put(players[0].getCards());
        jsonArray.put("game started");

        System.out.println(players[0].getCards());
        System.out.println(players[1].getCards());

        gameStarted = true;

        return jsonArray;

    }

    //gettAllCards
    public JSONArray getAllCards() {
        JSONArray jsonArray = new JSONArray();
        Arrays.stream(players)
                .filter(p -> p.getName().equals("Player1"))
                .findFirst()
                .ifPresent(p -> jsonArray.put(p.getCards()));
        return jsonArray;
    }

    public JSONArray selectCard(int cardId) {
        if (!gameStarted)
            throw new IllegalStateException("Game not started");
        if (cardId < 0 || cardId > 104)
            throw new IllegalArgumentException("Card id must be between 0 and 104");

        // get the card from the player's hand
        Card card = Arrays.stream(players)
                .filter(p -> p.getName().equals("Player1"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player1 not found"))
                .getCards().stream()
                .filter(c -> c.getValue() == cardId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // add the card to the selected cards array
        selectedCards[0] = card;

        //add the card from the bot to the selected cards array
        selectedCards[1] = Arrays.stream(players)
                .filter(p -> p.getName().equals("Bot"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Bot not found"))
                .getCards().stream().findFirst().orElseThrow(() -> new IllegalStateException("Bot has no cards"));

        // now i want the main method to check the cards
        checkCards();

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(getJsonCardStacks());
        jsonArray.put(getJsonScoreBoard());
        Arrays.stream(players)
                .filter(p -> p.getName().equals("Player1"))
                .findFirst()
                .ifPresent(p -> jsonArray.put(p.getCards()));
        jsonArray.put("ok");

        return jsonArray;
    }

    public void checkCards(){

    }


    public JSONArray getJsonCardStacks() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < cardStacks.length; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stackId", i); // index of the card stack

            JSONArray cardValues = new JSONArray();
            cardStacks[i].getCards().forEach(card -> cardValues.put(card.getValue()));
            jsonObject.put("cards", cardValues); // values of the cards in the stack

            jsonObject.put("topValue", cardStacks[i].getTopValue()); // value of the top card
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    public JSONArray getJsonScoreBoard() {
        JSONArray jsonArray = new JSONArray();

        for (Player player : players) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerName", player.getName());
            jsonObject.put("playerScore", player.getScore());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }



}
