package entity;

import entity.impl.Card;
import entity.impl.Hand;

import java.util.ArrayList;
import java.util.List;

public abstract class GameParticipate {
    // 用户手牌
    protected Hand hand;
    // 用户资产
    public int money;
    // 用户在第一轮发牌时是否已经拿到BlackJack
    public boolean isEnd = false;
    // 用户id
    public int id = -1;
    // 用户赌注
    public int moneyToBet = 0;
    //一轮只能下一次注
    public boolean flag = true;

    // 判断当前用户是否摸牌
    public abstract boolean checkIfDrawCard();

    // 赢钱
    public abstract int earnMoney(int res);

    // 输钱
    public abstract int loseMoney();

    public abstract boolean isBet();

    // 摸牌
    public void drawCard(Card card) {
        hand.addCard(card);
    }

    // 打印当前手牌
    public void printHand() {
        for (Card card : hand.hands) {
            System.out.print(card.word + " ");
        }
        System.out.print("\n");
    }

    // 手牌数
    public int getHandSize() {
        return hand.hands.size();
    }

    // 当前得分
    public int getScore() {
        return hand.score;
    }

    public int getMoney() {
        return money;
    }

    public List<Card> getCardList() {
        return hand.hands;
    }

    // 用户是否已经爆掉
    public boolean isBomb() {
        return hand.isBomb;
    }

    // 用户是否拿到BlackJack
    public boolean isBlackJack() {
        return hand.isBlackJack;
    }

    // 重置用户状态
    public void clear() {
        hand.clearHand();
        isEnd = false;
        flag = true;
    }

    //重置用户资产
    public void resetMoney(int money){
    }
}
