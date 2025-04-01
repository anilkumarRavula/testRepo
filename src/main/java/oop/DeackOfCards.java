package oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeackOfCards {
    public static void main(String[] args) {
        Deck deck = new Deck();

        System.out.println("Deck size: " + deck.size());


        deck.sort(deck.getCards());
        System.out.println(deck.getCards());
    }
     static class Card {
        private Suit suit;
        private Rank rank;

        public Card(Suit suit, Rank rank) {
            this.suit = suit;
            this.rank = rank;
        }

        public Suit getSuit() {
            return suit;
        }

        public Rank getRank() {
            return rank;
        }

        @Override
        public String toString() {
            return rank + " of " + suit;
        }
    }
     enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

     enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }
    public static class Deck {
        private List<Card> cards;

        public Deck() {
            cards = new ArrayList<>();
            initializeDeck();
            shuffle();
        }

        private void initializeDeck() {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }

        public void shuffle() {
            Collections.shuffle(cards);
        }
        public void sort(List<Card> cards) {
            Collections.sort(cards, Comparator.comparing(Card::getSuit)
                    .thenComparing(Card::getRank));
        }
        public Card drawCard() {
            if (cards.isEmpty()) {
                throw new IllegalStateException("The deck is empty.");
            }
            return cards.remove(cards.size() - 1);
        }

        public int size() {
            return cards.size();
        }
        public List<Card> getCards() {
            return cards;
        }
    }

}
