package com.isep.jeu6quiprendback.util;

import com.isep.jeu6quiprendback.util.Card;
import com.isep.jeu6quiprendback.util.CardStack;
import com.isep.jeu6quiprendback.util.Cards;
import com.isep.jeu6quiprendback.util.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Game {

    private boolean gameStarted = false;
    private Player[] players;
    private CardStack[] cardStacks;

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

        System.out.println(players[0].getCards());
        System.out.println(players[1].getCards());

        gameStarted = true;

        return jsonArray;
        /*//        @TODO remove
        players[0].setSelectedCard(players[0].getCards().stream().max(Card::compareTo).get());
        players[1].setSelectedCard(players[1].getCards().stream().max(Card::compareTo).get());

        System.out.println(getJsonCardStacks());
        System.out.println(getJsonScoreBoard());

//        @TODO remove
        checkSelectedCards();
        cardSelected();*/

    }

    //gettAllCards
    public JSONArray getAllCards() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(players[0].getCards());
        return jsonArray;
    }

    //method to select a card and return the new cardStacks and scoreboard
    public JSONArray selectCard(int cardId, String playerName) {
        if (!gameStarted)
            throw new IllegalStateException("Game not started");
        if (cardId < 0 || cardId > 104)
            throw new IllegalArgumentException("Card id must be between 0 and 104");
        if (playerName == null || playerName.isBlank())
            throw new IllegalArgumentException("Player name must not be null or blank");
        Player player = players[0];
        Card selectedCard = player.getCards().stream()
                .filter(c -> c.getValue() == cardId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player " + playerName + " does not have card " + cardId));
        player.setSelectedCard(selectedCard);

        //bot select card
        players[1].setSelectedCard(players[1].getCards().stream().max(Card::compareTo).get());

        checkSelectedCards();
        cardSelected();

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getJsonCardStacks());
        jsonArray.put(getJsonScoreBoard());
        jsonArray.put(players[0].getCards());

        return jsonArray;
    }

    public void checkSelectedCards() {
        for (Player player : players) {
            Card selectedCard = player.getSelectedCard();
            if (selectedCard == null)
                throw new IllegalStateException("Player " + player.getName() + " has no selected card");
            if (!player.getCards().contains(selectedCard))
                throw new IllegalStateException("Player " + player.getName() + " does not own the selected card");
        }
    }

    public void cardSelected() {
//        sort the players' selected cards by value
        Arrays.sort(players);

//        @TODO remove
//        int[] selectedCardIds = {43, 55, 67};
//        List<Card> selectedCards = new java.util.ArrayList<>(Arrays.stream(selectedCardIds)
//                .mapToObj(Cards::cardOf)
//                .toList());
//        selectedCards.sort(Card::compareTo);


//        put selected cards in the card stacks with the smallest difference
//        otherwise the player takes the chosen stack
        for (Player player : players) {
            Card selectedCard = player.getSelectedCard();
            int stackDifference = Integer.MAX_VALUE;
            CardStack selectedCardStack = null;
            for (CardStack cardStack : cardStacks) {
                if (selectedCard.getValue() < cardStack.getTopValue()) { // card's value too small, try next card stack
                    continue;
                }
                if (selectedCard.getValue() - cardStack.getTopValue() < stackDifference) {
                    stackDifference = selectedCard.getValue() - cardStack.getTopValue();
                    selectedCardStack = cardStack;
                }
            }
            if (selectedCardStack != null) { // card's value too small
                selectedCardStack.addCardMayTake(player);
                continue;
            }
            selectedCardStack = player.getSelectedCardStack();
            if (selectedCardStack == null)
                throw new IllegalStateException("Player " + player.getName() + " has no selected card stack");

            System.out.println("Player " + player.getName() + " selected card " + selectedCard.getValue() + " and added it to stack " + selectedCardStack.getTopValue());

            player.takeSelectedCardStack();
        }

        if (players[0].getCards().isEmpty() && players[1].getCards().isEmpty()) {
            cardStacks = Cards.distributeRandomCards(players);
        }

        System.out.println("Returns:");
        System.out.println(getJsonCardStacks());
        System.out.println(getJsonScoreBoard());
    }

    // Method to get the JSON representation of the card stacks
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
