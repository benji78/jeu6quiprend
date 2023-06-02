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
    private boolean needToTakeStack = false;
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

    //method to select a card and return the new cardStacks and scoreboard
    public JSONArray selectCard(int cardId) {
        if (!gameStarted)
            throw new IllegalStateException("Game not started");
        if (cardId < 0 || cardId > 104)
            throw new IllegalArgumentException("Card id must be between 0 and 104");
        //find player named "Player1"
        Player player = Arrays.stream(players)
                .filter(p -> p.getName().equals("Player1"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player1 not found"));
        Player bot = Arrays.stream(players)
                .filter(p -> p.getName().equals("Bot"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Bot not found"));
        Card selectedCard = player.getCards().stream()
                .filter(c -> c.getValue() == cardId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player " + player.getName() + " does not have card " + cardId));
        player.setSelectedCard(selectedCard);

        //bot always select higher card
        bot.setSelectedCard(bot.getCards().stream().max(Card::compareTo).get());

        checkSelectedCards();
        cardSelected();


        JSONArray jsonArray = new JSONArray();

        if(needToTakeStack){
            jsonArray.put(getJsonCardStacks());
            jsonArray.put(getJsonScoreBoard());
            Arrays.stream(players)
                    .filter(p -> p.getName().equals("Player1"))
                    .findFirst()
                    .ifPresent(p -> jsonArray.put(p.getCards()));
            jsonArray.put("error, player needs to take stack");
        } else {
            jsonArray.put(getJsonCardStacks());
            jsonArray.put(getJsonScoreBoard());
            Arrays.stream(players)
                    .filter(p -> p.getName().equals("Player1"))
                    .findFirst()
                    .ifPresent(p -> jsonArray.put(p.getCards()));
            jsonArray.put("ok");
        }

        return jsonArray;
    }


    public void checkSelectedCards(){
        for (Player player : players) {
            Card selectedCard = player.getSelectedCard();
            if (selectedCard == null)
                throw new IllegalStateException("Player " + player.getName() + " has no selected card");
            if (!player.getCards().contains(selectedCard))
                throw new IllegalStateException("Player " + player.getName() + " does not own the selected card");
        }
    }

    //method to take a stack and return the new cardStacks and scoreboard based on the card selected
    public void cardSelected(){
        // sort the players' selected cards by value
        Arrays.sort(players);
        System.out.println("players sorted by selected card: " + Arrays.toString(players));

        // put selected cards in the card stacks with the smallest difference
        // otherwise the player has to choose a stack, so we return a message to the client
        for (Player player : players) {

            System.out.println("player: " + player + " cards: " + player.getCards());
            System.out.println("player: " + player + " selected card: " + player.getSelectedCard());

            Card selectedCard = player.getSelectedCard();
            int stackDifference = Integer.MAX_VALUE;
            CardStack selectedCardStack = null;

            for (CardStack cardStack : cardStacks) { // for each card stack
                if (selectedCard.getValue() < cardStack.getTopValue()) { // card's value too small for this stack
                    continue;
                }
                if (selectedCard.getValue() - cardStack.getTopValue() < stackDifference) { // card's value is closer to the top of this stack
                    stackDifference = selectedCard.getValue() - cardStack.getTopValue(); // update the difference
                    selectedCardStack = cardStack; // update the selected card stack
                }
            }

            if (selectedCardStack != null) { // if a card stack was found
                selectedCardStack.addCardMayTake(player); // add the card to the stack
                continue;
            } else { // if no card stack was found
                needToTakeStack = true;

                //check if the player is the bot
                if(player.getName().equals("Bot")){
                    System.out.println("Bot needs to take stack");
                    //bot always take the stack
                    selectStack(player.chooseRandomRow(), "Bot");
                }

            }

            if (this.players[0].getCards().isEmpty() && this.players[1].getCards().isEmpty()) {
                this.cardStacks = Cards.distributeRandomCards(this.players);
            }
        }
    }

    public JSONArray selectStack(int stackId, String name){
        if (!gameStarted)
            throw new IllegalStateException("Game not started");
        if (!needToTakeStack)
            throw new IllegalStateException("No need to take stack");
        if (stackId < 0 || stackId > 3)
            throw new IllegalArgumentException("Stack id must be between 0 and 3");

        Player player = Arrays.stream(players)
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player not found"));
        CardStack selectedStack = cardStacks[stackId];
        player.takeCardStack(selectedStack);

        needToTakeStack = false;

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(getJsonCardStacks());
        jsonArray.put(getJsonScoreBoard());
        Arrays.stream(players)
                .filter(p -> p.getName().equals("Player1"))
                .findFirst()
                .ifPresent(p -> jsonArray.put(p.getCards()));
        jsonArray.put("stack taken");

        return jsonArray;
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
