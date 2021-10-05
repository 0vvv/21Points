package entity.impl;

import entity.IDeck;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements IDeck {
    List<Card> cardList;
    int deckPointer;

    @Override
    public Card deal() {
        Card cardToDeal = null;
        if (deckPointer < cardList.size()) {
            cardToDeal = cardList.get(deckPointer++);
        }
        else {
            System.out.println("牌已发光");
        }
        return cardToDeal;
    }

    @Override
    public void shuffle() {
        this.deckPointer = 0;
        Collections.shuffle(cardList);
    }

    public Deck() {
        this.cardList = new ArrayList<>();
        this.deckPointer = 0;
        generateDeck();
    }

    private void generateDeck(){
        for (int i = 2; i <= 10; i++) {
            generateSuitsOfCard(Integer.toString(i));
        }
        generateSuitsOfCard("A");
        generateSuitsOfCard("J");
        generateSuitsOfCard("Q");
        generateSuitsOfCard("K");
    }

    private void generateSuitsOfCard(String word) {
        cardList.add(new Card(word, "HEART"));
        cardList.add(new Card(word, "SPADE"));
        cardList.add(new Card(word, "DIAMOND"));
        cardList.add(new Card(word, "CLUB"));
    }
}
