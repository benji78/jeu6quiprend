package com.isep.jeu6quiprend;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CardStackTest {
    @Test
    public void testInitialization() {
        Card firstCard = new Card(5);
        CardStack cardStack = new CardStack(firstCard);

        assertEquals(1, cardStack.getCardCount());
        assertEquals(5, cardStack.getTopValue());
        assertEquals(2, cardStack.getSumHeads());
    }

    @Test
    public void testAddCardMayTake_ValidCard() {
        CardStack cardStack = new CardStack(new Card(5));

        Player player = new Player("Hugo");
        Card card = new Card(55);
        player.setCards(new PlayerCards(List.of(card)));
        player.setSelectedCard(card);
        cardStack.addCardMayTake(player);

        assertEquals(2, cardStack.getCardCount());
        assertEquals(0, player.getCards().size());
        assertNull(player.getSelectedCard());
        assertNull(player.getSelectedCardStack());
    }

    @Test
    public void testTakeSelectedCardStack_SmallerCard() {
        CardStack cardStack = new CardStack(new Card(55));

        Player player = new Player("Hugo");
        Card card = new Card(5);
        player.setCards(new PlayerCards(List.of(card)));
        player.setSelectedCard(card);
        player.setSelectedCardStack(cardStack);
//        cardStack.addCardMayTake(player);
        player.takeSelectedCardStack();

        assertEquals(1, cardStack.getCardCount());
        assertEquals(7, player.getScore()); // card 55 = 7 heads
        assertEquals(2, cardStack.getSumHeads()); // card 5 = 2 heads
        assertEquals(0, player.getCards().size());
        assertNull(player.getSelectedCard());
        assertNull(player.getSelectedCardStack());
    }

    @Test // not very relevant anymore since I use takeCardStack() directly …
    public void testAddCardMayTake_6thCard() {
        CardStack cardStack = new CardStack(new Card(5));

        Player player = new Player("Hugo");
        for (int i = 0; i < 10; i++) {
            Card card = new Card(55 + i);
            System.out.println(card.getHeads());
            player.setCards(new PlayerCards(List.of(card)));
            player.setSelectedCard(card);
//            don't need to set player's selectedCardStack
            cardStack.addCardMayTake(player);

            assertEquals((1 + i) % 5 + 1, cardStack.getCardCount()); // 1, 2, 3, 4, 5 repeat (start at 2)
            assertEquals(0, player.getCards().size());
            assertNull(player.getSelectedCard());
            assertNull(player.getSelectedCardStack());
        }
        assertEquals(19, player.getScore()); // (2+7+1+1+1)+(1+3+1+1+1) = 12+7 = 19
    }
}
