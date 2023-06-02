package com.isep.jeu6quiprendback.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

public class PlayerCardsTest {
    private PlayerCards playerCards;
    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    public void setUp() {
        card1 = new Card(1);
        card2 = new Card(2);
        card3 = new Card(3);
        Collection<Card> cards = Arrays.asList(card1, card2, card3);
        playerCards = new PlayerCards(cards);
    }

    @Test
    public void testGetCards() {
        Collection<Card> cards = playerCards.getCards();
        Assertions.assertEquals(3, cards.size());
        Assertions.assertTrue(cards.contains(card1));
        Assertions.assertTrue(cards.contains(card2));
        Assertions.assertTrue(cards.contains(card3));
    }

    @Test
    public void testTake() {
        playerCards.take(card2);

        Collection<Card> remainingCards = playerCards.getCards();
        Assertions.assertEquals(2, remainingCards.size());
        Assertions.assertTrue(remainingCards.contains(card1));
        Assertions.assertFalse(remainingCards.contains(card2));
        Assertions.assertTrue(remainingCards.contains(card3));
    }

    @Test
    public void testTakeInvalidCard() {
        Card invalidCard = new Card(4);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            playerCards.take(invalidCard);
        });
    }
}
