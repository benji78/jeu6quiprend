package com.isep.jeu6quiprend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class Game {
    private Player[] players;
    private CardStack[] cardStacks;

    public void start() {
        players = new Player[2];
        players[0] = new Player("Peter");
        players[1] = new Player("Bot");

        cardStacks = Cards.distributeRandomCards(players);

//        @TODO remove
        players[0].setSelectedCard(players[0].getCards().stream().max(Card::compareTo).get());
        players[1].setSelectedCard(players[1].getCards().stream().max(Card::compareTo).get());

        System.out.println(getJsonCardStacks());
        System.out.println(getJsonScoreBoard());

//        @TODO remove
        checkSelectedCards();
        cardSelected();
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
        System.exit(0);
    }

    // Method to get the JSON representation of the card stacks
    public String getJsonCardStacks() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < cardStacks.length; i++) {
            JSONObject jsonObject = new JSONObject();
//          @TODO is this relevant?
            jsonObject.put("stackId", i); // index of the card stack

            JSONArray cardValues = new JSONArray();
            cardStacks[i].getCards().forEach(card -> cardValues.put(card.getValue()));
            jsonObject.put("cards", cardValues); // values of the cards in the stack

            jsonObject.put("topValue", cardStacks[i].getTopValue()); // value of the top card
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }

    public String getJsonScoreBoard() {
        JSONArray jsonArray = new JSONArray();

        for (Player player : players) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("playerName", player.getName());
            jsonObject.put("playerScore", player.getScore());
            jsonArray.put(jsonObject);
        }

        return jsonArray.toString();
    }
}
