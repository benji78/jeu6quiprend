package com.isep.jeu6quiprendback.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CardTest {
    @Test
    public void testCardCreation() {
        Card card = new Card(55);
        Assertions.assertEquals(55, card.getValue());
        Assertions.assertEquals(7, card.getHeads());
    }

    @Test
    public void testCardComparison() {
        Card card1 = new Card(10);
        Card card2 = new Card(20);
        Card card3 = new Card(10);

        Assertions.assertTrue(card1.compareTo(card2) < 0);
        Assertions.assertTrue(card2.compareTo(card1) > 0);
        Assertions.assertEquals(0, card1.compareTo(card3));
    }

    @Test
    public void testCardToString() {
        Card card = new Card(42);
        Assertions.assertEquals("Card-42", card.toString());
    }
}