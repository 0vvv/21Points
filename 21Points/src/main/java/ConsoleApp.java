import entity.GameParticipate;
import entity.IDealer;
import entity.impl.Dealer;
import entity.impl.Deck;
import entity.impl.Hand;
import entity.impl.Player;

import java.util.ArrayList;
import java.util.List;

public class ConsoleApp {
    public static final int PLAYER_COUNT = 5;

    public static void main(String[] args) {
        List<GameParticipate> playerList = new ArrayList<>();
        IDealer dealer = new Dealer(new Hand(), 500000000, new Deck());

        dealer.shuffleDeck();

        // 第一轮发牌，每人两张牌
        dealer.dealCard((GameParticipate) dealer);
        dealer.dealCard((GameParticipate) dealer);

        for (int i = 0; i < PLAYER_COUNT; i++) {
            GameParticipate player = new Player(new Hand(), 500, i);
            playerList.add(player);

            dealer.dealCard(player);
            dealer.dealCard(player);

            if (player.isBlackJack()) {
                System.out.println("玩家" + player.id + "是BlackJack，该玩家游戏结束！");
                player.isEnd = true;
                dealer.liquidateAssets(player);
            }
        }

        if (((GameParticipate) dealer).isBlackJack()) {
            System.out.println("庄家是BlackJack，游戏结束！清算并开始下一局");

            for (GameParticipate player : playerList) {
                if (!player.isEnd) {
                    dealer.liquidateAssets(player);
                }
                player.clear();
            }

            ((GameParticipate) dealer).clear();
            return;
        }

        // 第二轮发牌
        for (GameParticipate player : playerList) {
            while (!player.isEnd && player.checkIfDrawCard()) {
                dealer.dealCard(player);
            }
        }

        while (((GameParticipate) dealer).checkIfDrawCard()) {
            dealer.dealCard((GameParticipate) dealer);
        }

        System.out.println("庄家牌为：");
        ((GameParticipate) dealer).printHand();

        for (GameParticipate player : playerList) {
            if (!player.isEnd) {
                dealer.liquidateAssets(player);
            }
        }

        System.out.println("游戏结束！开始下一局");
        ((GameParticipate) dealer).clear();
        for (GameParticipate player : playerList) {
            player.clear();
        }
    }
}
