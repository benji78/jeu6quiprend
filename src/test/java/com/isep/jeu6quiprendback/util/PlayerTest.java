package com.isep.jeu6quiprendback.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTest {
    private Player player;
    private PlayerCards playerCards;
    private CardStack cardStack;

    @BeforeEach
    public void setUp() {
        player = new Player("Player 1");
        playerCards = new PlayerCards(Arrays.asList(
                new Card(1),
                new Card(2),
                new Card(3),
                new Card(4),
                new Card(5),
                new Card(6),
                new Card(7),
                new Card(8),
                new Card(9),
                new Card(10)
        ));
        player.setCards(playerCards);
        cardStack = new CardStack(new Card(11));
    }

    @Test
    public void testRemoveSelectedCard() {
        player.setSelectedCard(new Card(5));
        player.setSelectedCardStack(cardStack);
        player.removeSelectedCard();

        List<Card> remainingCards = new ArrayList<>(player.getCards());
        Assertions.assertEquals(9, remainingCards.size());
        Assertions.assertNull(player.getSelectedCard());
        Assertions.assertNull(player.getSelectedCardStack());
    }

    @Test
    public void testCompareTo() {
        player.setSelectedCard(new Card(5));
        Player otherPlayer = new Player("Other Player");
        otherPlayer.setSelectedCard(new Card(10));

        int result = player.compareTo(otherPlayer);
        Assertions.assertTrue(result < 0);

        otherPlayer.setSelectedCard(new Card(3));
        result = player.compareTo(otherPlayer);
        Assertions.assertTrue(result > 0);

        otherPlayer.setSelectedCard(new Card(5));
        result = player.compareTo(otherPlayer);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testChooseRandomCard() {
        int randomCardIndex = player.chooseRandomCard();
        List<Card> cards = new ArrayList<>(player.getCards());  // Convert set to list
        Assertions.assertTrue(randomCardIndex >= 0 && randomCardIndex < cards.size());
    }

    @Test
    public void testChooseRandomRow() {
        int randomRowIndex = player.chooseRandomRow();
        Assertions.assertTrue(randomRowIndex >= 0 && randomRowIndex < 4);
    }
}
