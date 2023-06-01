package com.isep.jeu6quiprendback.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Getter
public class Player implements Comparable<Player> {
    private final String name;
    private int score = 0;
    @Setter
    private PlayerCards cards;
    @Setter
    private Card selectedCard = null;
    @Setter
    private CardStack selectedCardStack = null;

    public Player(String name) {
        this.name = name;
    }

    public Collection<Card> getCards() {
        return cards.getCards();
    }

    public void takeSelectedCardStack() {
        Objects.requireNonNull(selectedCardStack);
        takeCardStack(selectedCardStack);
    }

    public void takeCardStack(CardStack cardStack) {
        score += cardStack.getSumHeads();
        cardStack.resetWithCard(selectedCard);
        removeSelectedCard();
    }

    protected void removeSelectedCard() {
        cards.take(selectedCard);
        selectedCard = null;
        selectedCardStack = null;
    }

    @Override
    public String toString() {
        return "Player [name:" + name + ", score:" + score + "]";
    }

    @Override // Arrays.sort(players);
    public int compareTo(Player player) {
        return Integer.compare(selectedCard.getValue(), player.selectedCard.getValue());
    }

}
