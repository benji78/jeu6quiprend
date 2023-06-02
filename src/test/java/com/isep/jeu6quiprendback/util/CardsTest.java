package com.isep.jeu6quiprendback.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CardsTest {
    private static CardStack[] cardStacks;
    private static Player[] players;

    @BeforeAll
    public static void setUp() {
        cardStacks = new CardStack[4];
        players = new Player[3];
        for (int i = 0; i < 4; i++) {
            cardStacks[i] = new CardStack(new Card(i + 1));
        }
        for (int i = 0; i < 3; i++) {
            players[i] = new Player("Player " + (i + 1));
        }
    }

    @Test
    public void testCardOf() {
        Card card = Cards.cardOf(1);
        Assertions.assertNotNull(card);
        Assertions.assertEquals(1, card.getValue());
    }

    @Test
    public void testDistributeRandomCards() {
        CardStack[] result = Cards.distributeRandomCards(players);

        Assertions.assertEquals(4, result.length);
        for (CardStack stack : result) {
            Assertions.assertNotNull(stack);
        }

        int totalPlayerCards = Cards.CARDS_PER_PLAYER * players.length;
        Assertions.assertEquals(totalPlayerCards, countTotalPlayerCards(players));

        int remainingCards = Cards.MAX_CARD_VALUE - Cards.MIN_CARD_VALUE + 1 - totalPlayerCards - 4;
        Assertions.assertEquals(remainingCards, countRemainingCards(cardStacks));
    }

    private int countRemainingCards(CardStack[] cardStacks) {
        int count = 0;
        for (CardStack stack : cardStacks) {
            count += stack.getCards().size();
        }
        return count;
    }

    @Test
    public void testDistributeRandomCardsTooManyPlayers() {
        Player[] tooManyPlayers = new Player[12];
        for (int i = 0; i < 12; i++) {
            tooManyPlayers[i] = new Player("Player " + (i + 1));
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Cards.distributeRandomCards(tooManyPlayers);
        });
    }

    private int countTotalPlayerCards(Player[] players) {
        int count = 0;
        for (Player player : players) {
            count += player.getCards().size();
        }
        return count;
    }
}
