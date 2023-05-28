package com.isep.jeu6quiprend;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * hen adding a card with value less than top value,
 * take all the stacked cards and put your card
 * <p>
 * when adding a 6-th card,
 * take all the stack cards (penalties)
 * and put your card which become the first of the stack.
 * <p>
 * otherwise just add the card on the stack
 * (at positions 2,3,4,5)
 */
@Getter
public class CardStack {
    private final List<Card> cards = new ArrayList<>(5);
    private int sumHeads;
    private int topValue;

    public CardStack(Card firstCard) {
        Objects.requireNonNull(firstCard);
        resetWithCard(firstCard);
    }

    public int getCardCount() {
        return cards.size();
    }

    public void addCardMayTake(Player player) {
        Card selectedCard = player.getSelectedCard();
        Objects.requireNonNull(selectedCard);
        // card's value below top card's or 6th card in stack => take the stack
        if (getCardCount() == 5) { // selectedCard not yet added to stack so 5
            player.takeCardStack(this);
            return;
        }
        if (selectedCard.getValue() <= topValue) { // @TODO rename method and remove this check
            throw new IllegalStateException("Use player.takeCardStack(cardStack) instead of addCardMayTake()");
        }
        cards.add(selectedCard);
        player.removeSelectedCard();
        sumHeads += selectedCard.getHeads();
        topValue = selectedCard.getValue();
    }

    protected void resetWithCard(Card card) {
        cards.clear();
        cards.add(card);
        sumHeads = card.getHeads();
        topValue = card.getValue();
    }

//    public void takeStack(Card card, Player player) {
//        Objects.requireNonNull(card);
//        Objects.requireNonNull(player);
//        player.addToScore(sumHeads);
//        player.deleteCard(card);
//        resetWithCard(card);
//    }
}
