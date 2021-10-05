package entity.impl;

import app.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class Card {
    // 牌对应的数值
    public int value;
    // 牌面
    public String word;
    // 花色
    public Suit suit;

    private static int allCardsWidth = 950;
    private static int allCardsHeight = 392;
    private static int cardWidth = allCardsWidth/13;
    private static int cardHeight = allCardsHeight/4;

    private int x = 0;
    private int y = 0;

    private static BufferedImage allCards;
    private BufferedImage img;

    // 初始化牌面到数值的映射
    private static HashMap<String, Integer> mappper;
    static {
        mappper = new HashMap<>();
        for (int i = 2; i <= 10; i++) {
            mappper.put(Integer.toString(i), i);
        }
        mappper.put("A", 1);
        mappper.put("J", 11);
        mappper.put("Q", 12);
        mappper.put("K", 13);

        try {
            allCards = ImageIO.read(new File("resources/cardSpriteSheet.png"));
        } catch (Exception e) {
            System.out.println("读取图片错误");
        }
    }

    //打印卡牌，庄家牌和玩家牌处于不同的位置
    public void printCards(Graphics2D g, boolean dealerTurn, int cardNumber, int id){
        if (dealerTurn) {
            y = 100; //如果是dealerTurn那么就放在90
            x = 400 + (cardWidth+5) * cardNumber;//所有牌的横坐标都是400+
        } else {
            y = 450 + cardHeight * (cardNumber / 2) + 5;
            x = 50 + Game.PLAYER_AREA_WIDTH * id + (cardWidth + 5) * (cardNumber % 2);
        }
        g.drawImage(img, x, y, null);//打印这张牌
    }

    public Card(String word, String suit) {
        this.word = word.toUpperCase();
        if (this.word.equals("A")) {
            this.value = 11;
        } else {
            this.value = mappper.get(this.word) > 10 ? 10 : mappper.get(this.word);
        }
        this.suit = Suit.valueOf(suit);
        this.img = allCards.getSubimage((mappper.get(this.word) - 1) * cardWidth, this.suit.ordinal() * cardHeight, cardWidth, cardHeight);
    }

    enum Suit {
        CLUB, SPADE, HEART, DIAMOND;
    }
}
