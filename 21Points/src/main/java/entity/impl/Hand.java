package entity.impl;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    // 存放用户现在的手牌
    public List<Card> hands = new ArrayList<>();
    // 用户当前手牌得分
    public int score = 0;

    public boolean isBlackJack = false;
    public boolean isBomb = false;

    // 手牌中A的数目，用于得分转换
    public int countA = 0;

    // 每一局结束后，用户手牌数据清零
    public void clearHand() {
        hands.clear();
        score = 0;
        isBlackJack = false;
        isBomb = false;
        countA = 0;
    }

    public void addCard(Card card) {
        hands.add(card);
        score += card.value;

        if (card.word.equals("A")) {
            countA++;
        }

        // 如果A算成11时爆掉了，将A改成1
        while (this.score > 21 && this.countA > 0) {
            countA--;
            score -= 10;
        }

        this.resetStatus();
    }

    // 每一次摸牌后，计算当前手牌是否爆掉或是BlackJack
    private void resetStatus() {
        if (this.score > 21) {
            isBomb = true;
        }

        if (this.score == 21 && this.hands.size() == 2) {
            isBlackJack = true;
        }
    }
}
