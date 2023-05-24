package com.isep.jeu6quiprend;

import lombok.Getter;

@Getter
public class Card {
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
}

