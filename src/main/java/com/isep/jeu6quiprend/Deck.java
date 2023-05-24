package com.isep.jeu6quiprend;

public class Deck {
    private final Card[] cards = new Card[104];
    private int size;

    public Deck() {
        size = 104;
        for (int value = 1; value <= size; value++) {
            cards[value] = new Card(value);
        }
    }

    public Card get(int index) {
        return cards[index];
    }

    public void shuffle() {
        for (int i = 0; i < size; i++) {
            int randomIndex = (int) (Math.random() * size);
            Card temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }
    }

//    public Card draw() {
//        Card card = cards[0];
//        size--;
//        for (int i = 0; i < size; i++) {
//            cards[i] = cards[i + 1];
//        }
//        return card;
//    }
}
