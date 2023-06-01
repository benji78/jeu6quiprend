package com.isep.jeu6quiprendback.util;

import java.util.*;
import java.util.stream.IntStream;

/**
 * The set of cards.
 */
public class Cards {
    public static final int MIN_CARD_VALUE = 1;
    public static final int MAX_CARD_VALUE = 104;
    public static final int CARDS_PER_PLAYER = 10;
    private static final List<Card> cards = createCards();

    public static Card cardOf(int i) {
        return cards.get(i - 1);
    }

    private static List<Card> createCards() {
        List<Card> res = new ArrayList<>();
        for (int i = MIN_CARD_VALUE; i <= MAX_CARD_VALUE; i++) {
            res.add(new Card(i));
        }
        return Collections.unmodifiableList(res);
    }

    public static CardStack[] distributeRandomCards(Player[] players) {
//        max 10 players: (104 cards - 4 stacks) / 10 cards per player
        if (Arrays.stream(players).count() > (MAX_CARD_VALUE - MIN_CARD_VALUE - 3) / CARDS_PER_PLAYER) {
            throw new IllegalArgumentException("Too many players");
        }

        List<Card> remainingCards = new ArrayList<>(cards);
        Collections.shuffle(remainingCards, new Random());

        for (Player player : players) {
            PlayerCards playerCards = new PlayerCards(remainingCards.subList(0, CARDS_PER_PLAYER));
            remainingCards.subList(0, CARDS_PER_PLAYER).clear();

            player.setCards(playerCards);
        }

        return IntStream.range(0, 4)
                .mapToObj(i -> new CardStack(remainingCards.remove(0)))
                .toArray(CardStack[]::new);
    }
}
