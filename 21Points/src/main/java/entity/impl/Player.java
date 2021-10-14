package entity.impl;

import entity.GameParticipate;

import java.util.Scanner;

public class Player extends GameParticipate {
    @Override
    public boolean checkIfDrawCard() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("玩家" + this.id + "当前牌为:");
        this.printHand();

        System.out.println("玩家" + this.id + "是否摸牌（输入1摸牌，输入2停牌)");
        String ans = scanner.nextLine();
        if (ans.equals("1")) {
            return true;
        }
        return false;
    }

    @Override
    public int earnMoney(int res) {
        int moneyToEarn = 0;
        if (res == 0) {
            // 平局
            moneyToEarn = this.moneyToBet;
        }
        if (res == 1) {
            moneyToEarn = this.moneyToBet / 2 * 3;
        }
        this.money += moneyToEarn;

        return moneyToEarn;
    }

    @Override
    public int loseMoney() {
        int moneyToLose = this.moneyToBet;

        return moneyToLose;
    }

    @Override
    public boolean isBet() {
        return this.moneyToBet > 0;
    }

    @Override
    public void clear() {
        hand.clearHand();
        moneyToBet = 0;
        isEnd = false;
    }

    public Player (Hand hand, int money, int id) {
        this.hand = hand;
        this.money = money;
        this.id = id;
    }

    @Override
    public void resetMoney(int money){
        this.money = money;
    }
}
