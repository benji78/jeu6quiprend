package com.isep.jeu6quiprend;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

public class PlayerCards {
    private final TreeSet<Card> cards = new TreeSet<>();

    public PlayerCards(Collection<Card> ls) {
        cards.addAll(ls);
    }

    public Collection<Card> getCards() {
        return Collections.unmodifiableSet(cards);
    }

    public void take(Card card) {
        if (!cards.remove(card)) throw new IllegalArgumentException("Card not in player's cards");
    }
}
