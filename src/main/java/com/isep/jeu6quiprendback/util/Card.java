package com.isep.jeu6quiprendback.util;

import lombok.Getter;

@Getter
public class Card implements Comparable<Card> {
    private final int value;
    private final int heads;

    public Card(int value) {
        this.value = value;
        if (value == 55) {
            heads = 7;
        } else if (value % 11 == 0) {
            heads = 5;
        } else if (value % 10 == 0) {
            heads = 3;
        } else if (value % 5 == 0) {
            heads = 2;
        } else {
            heads = 1;
        }
    }

    @Override
    public String toString() {
        return "Card-" + value;
    }

    @Override
    public int compareTo(Card card) {
        return Integer.compare(value, card.value);
    }
}

