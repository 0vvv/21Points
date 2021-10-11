package entity.impl;

import entity.GameParticipate;
import entity.IDealer;
import entity.IDeck;

public class Dealer extends GameParticipate implements IDealer {
    private IDeck deck;

    @Override
    public void dealCard(GameParticipate gameParticipate) {
        // 从牌堆里摸牌
        Card cardToDeal = deck.deal();
        // 牌发光了，重新洗牌
        if (cardToDeal == null) {
            deck.shuffle();
            cardToDeal = deck.deal();
        }
        // 发牌
        gameParticipate.drawCard(cardToDeal);
    }

    @Override
    public void shuffleDeck() {
        deck.shuffle();
    }

    @Override
    public int liquidateAssets(GameParticipate gameParticipate) {
        if (gameParticipate.getScore() <= 21 && this.getScore() < gameParticipate.getScore()) {
            this.giveMoney(gameParticipate, 1);
            System.out.println("庄家点数小于玩家，玩家" + gameParticipate.id + "胜利，玩家余额:" + gameParticipate.money);
            return 1;
        }
        else if (this.getScore() <= 21 && this.getScore() > gameParticipate.getScore()) {
            this.receiveMoney(gameParticipate);
            System.out.println("庄家点数大于玩家，玩家" + gameParticipate.id + "失败，玩家余额:" + gameParticipate.money);
            return 2;
        }
        else if (this.getScore() == 21 && gameParticipate.getScore() == 21) {
            if ((gameParticipate.isBlackJack() && this.isBlackJack()) ) {
                this.giveMoney(gameParticipate, 0);
                System.out.println("庄家和玩家" + gameParticipate.id + "都为BlackJack"  + "平局，玩家余额:" + gameParticipate.money);
                return 0;
            }
            else if (this.getHandSize() == gameParticipate.getHandSize()){
                this.giveMoney(gameParticipate, 0);
                System.out.println("庄家和玩家" + gameParticipate.id + "都为21点且牌数相等" + "平局，玩家余额:" + gameParticipate.money);
                return 0;
            }
            else if (gameParticipate.isBlackJack() || gameParticipate.getHandSize() < this.getHandSize()) {
                this.giveMoney(gameParticipate, 1);
                System.out.println("庄家和玩家都为21点，玩家" + gameParticipate.id + "的牌更好，胜利，玩家余额:" + gameParticipate.money);
                return 1;
            }
            else {
                this.receiveMoney(gameParticipate);
                System.out.println("庄家和玩家" + gameParticipate.id + "都为21点，庄家的牌更好，胜利，玩家余额:" + gameParticipate.money);
                return 2;
            }
        }
        else if (this.isBomb()) {
            this.giveMoney(gameParticipate, 1);
            System.out.println("庄家爆掉，玩家" + gameParticipate.id + "胜利，玩家余额:" + gameParticipate.money);
            return 1;
        }
        else if (gameParticipate.isBomb()){
            this.receiveMoney(gameParticipate);
            System.out.println("玩家" + gameParticipate.id + "爆掉，庄家胜利，玩家余额:" + gameParticipate.money);
            return 2;
        }
        else if (this.getScore() == gameParticipate.getScore()) {
            this.giveMoney(gameParticipate, 0);
            System.out.println("庄家和玩家" + gameParticipate.id + "点数相等，" + "平局，玩家余额:" + gameParticipate.money);
            return 0;
        }
        else {
            System.out.println("清算时出现了处理不了的状况");
            return -1;
        }
    }

    private void receiveMoney(GameParticipate gameParticipate) {
        this.money += gameParticipate.loseMoney();
    }

    private void giveMoney(GameParticipate gameParticipate, int res) {
        if (res == 0) {
            this.money -= gameParticipate.earnMoney(res);
        }
        if (res == 1) {
            this.money -= gameParticipate.earnMoney(res) / 2;
        }
    }

    @Override
    public boolean checkIfDrawCard() {
        if (this.getScore() < 17) {
            return true;
        }
        return false;
    }

    @Override
    public int earnMoney(int res) {
        return 0;
    }

    @Override
    public int loseMoney() {
        return 0;
    }

    @Override
    public boolean isBet() {
        return false;
    }

    public Dealer (Hand hand, int money, IDeck deck) {
        this.hand = hand;
        this.money = money;
        this.deck = deck;
    }
}
