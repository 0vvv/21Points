package app;

import entity.GameParticipate;
import entity.IDealer;
import entity.IDeck;
import entity.impl.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends JComponent implements ActionListener {
    public BufferedImage backgroundImg;
    public static final int PLAYER_COUNT = 5;
    public static int PLAYER_AREA_WIDTH = 300;

    private List<GameParticipate> playerList = new ArrayList<>();
    private IDealer dealer = new Dealer(new Hand(), 500000000, new Deck());

    private int pointer;
    GameParticipate currantPlayer;

    //游戏的主界面一共有五个按钮，分别是HIT STAND EXIT BET RESET
    private JButton btnHit = new JButton("HIT");//玩家点击HIT来获得自己牌
    private JButton btnStand = new JButton("STAND");//玩家点击STAND结束本局
    private JButton btnExit = new JButton("EXIT");//玩家点击EXIT来回到主界面
    private JButton btnBet = new JButton("BET");//下注
    private JButton btnReStart = new JButton("RESET");//重新开始

    private static int n = 1;
    private static IDeck poker;

    static {
        poker = new Deck();
    }

    public Game(){
        //增加每一个按钮的监听
        btnExit.addActionListener(this);
        btnHit.addActionListener(this);
        btnBet.addActionListener(this);
        btnStand.addActionListener(this);
        btnReStart.addActionListener(this);

        repaint();

        dealer.shuffleDeck();

        // 第一轮发牌，每人两张牌
        dealer.dealCard((GameParticipate) dealer);
        dealer.dealCard((GameParticipate) dealer);

        // 初始化每一个player
        for (int i = 0; i < PLAYER_COUNT; i++) {
            GameParticipate player = new Player(new Hand(), 500, i);
            playerList.add(player);

            dealer.dealCard(player);
            dealer.dealCard(player);
        }
        pointer = 0;
    }

    //构建gameFrame的界面
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        if (pointer < playerList.size() && pointer >= 0) {
            currantPlayer = playerList.get(pointer);
        } else{
            currantPlayer = (GameParticipate) dealer;
        }

        if (pointer >= playerList.size()) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.BLACK);
        }
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        g2.drawString("DEALER", 750, 50);

        g2.setColor(Color.BLACK);
        btnHit.setBounds(510, 750, 100, 50);
        btnHit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        btnStand.setBounds(630, 750, 100, 50);
        btnStand.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        btnReStart.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnReStart.setBounds(760, 750, 100, 50);

        btnExit.setBounds(1340, 750, 100, 50);
        btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        btnBet.setBounds(50, 750, 100, 50);
        btnBet.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

        //打印卡牌
        for (int i = 0; i < PLAYER_COUNT; i++) {
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            if (i == currantPlayer.id) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.BLACK);
            }
            g2.drawString("PLAYER" + i, 50 + PLAYER_AREA_WIDTH * i, 300);

            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g2.drawString("PLAYER SCORE: " + playerList.get(i).getScore(), 50 + PLAYER_AREA_WIDTH * i, 350);
            g2.drawString("CURRENT MONEY: " + playerList.get(i).getMoney(), 50 + PLAYER_AREA_WIDTH * i, 400);

            List<Card> currentCardList = playerList.get(i).getCardList();
            for (int j = 0; j < currentCardList.size(); j++) {
                currentCardList.get(j).printCards(g2, false, j, playerList.get(i).id);
            }
        }

        if (pointer >= playerList.size()) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.BLACK);
        }
        List<Card> dealerCardList = ((GameParticipate)dealer).getCardList();

        // 所有玩家都摸完牌，庄家展示所有牌
        if (pointer >= playerList.size()) {
            g2.drawString("DEALER SCORE: " + ((GameParticipate)dealer).getScore(), 50, 80);
            for (int i = 0; i < dealerCardList.size(); i++){
                dealerCardList.get(i).printCards(g2, true, i, -1);
            }
        } else {
            g2.drawString("DEALER SCORE: ****", 50, 80);
            dealerCardList.get(0).printCards(g2, true, 0, -1);
        }

        //将按钮添加到这个frame里面
        super.add(btnBet);
        super.add(btnExit);
        super.add(btnHit);
        super.add(btnStand);
        super.add(btnReStart);

        if (pointer == playerList.size()) {
            for (GameParticipate player : playerList) {
                if (!player.isEnd) {
                    dealer.liquidateAssets(player);
                }
            }
            pointer++;
        }
    }

//    //计算整局的输赢
//    public void computeAllScore(){
//        player.computeScore();
//        dealer.computeScore();
//
//        if (player.getScore() > dealer.getScore()){
//            dealer.setLoose(true);
//            player.setMoney(player.getMoney() + 2 * player.getCurrentBet());
//            JOptionPane.showMessageDialog(null, "Player win!");
//            player.setWin(player.getWin() + 1);
//        }
//        else if (player.getScore() < dealer.getScore()){
//            player.setLoose(true);
//            player.setMoney(player.getMoney() - player.getCurrentBet());
//            JOptionPane.showMessageDialog(null, "Dealer win!");
//            dealer.setWin(dealer.getWin() + 1);
//        }
//        else if (player.getScore() == dealer.getScore()){
//            JOptionPane.showMessageDialog(null, "Draw!");
//            player.setLoose(true);
//        }
//        repaint();
//    }

    //单局的重新开始
    public void restart(){
        System.out.println("游戏开始新一局");

        ((GameParticipate) dealer).clear();
        for (GameParticipate player : playerList) {
            player.clear();
        }

        dealer.shuffleDeck();

        // 第一轮发牌，每人两张牌
        dealer.dealCard((GameParticipate) dealer);
        dealer.dealCard((GameParticipate) dealer);

        // 初始化每一个player
        for (int i = 0; i < PLAYER_COUNT; i++) {
            dealer.dealCard(playerList.get(i));
            dealer.dealCard(playerList.get(i));
        }
        pointer = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton selectedButton = (JButton)e.getSource();

        //如果是EXIT则回到主界面
        if (selectedButton == btnExit){
            App.gameFrame.dispose();
            App.currentState = App.STATE.MENU; // 将现在的状态切换成MENU
            App.initMenu();
        }
        //给玩家发牌
        else if (selectedButton == btnHit && pointer < playerList.size()){
            // 判断玩家是否已经下注，如果未下注那么我们将出现提示，下了注之后才能开始游戏
            if (currantPlayer.isBet()){
                dealer.dealCard(currantPlayer);
                repaint();

                //如果玩家爆牌了，直接退出游戏
                if (currantPlayer.isBomb()){
                    JOptionPane.showMessageDialog(null, "Player " + currantPlayer.id + " fail!");
                    dealer.liquidateAssets(currantPlayer);
                    currantPlayer.isEnd = true;
                    pointer++;

                    repaint();
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Player " + currantPlayer.id + " should bet first.");
            }
            repaint();
        }
        //玩家停止抽牌开始比大小
        else if (selectedButton == btnStand){
            // 庄家轮次
            if (pointer >= playerList.size()) {
                JOptionPane.showMessageDialog(null, "庄家摸牌完毕，开始清算。");
                pointer++;
            }
            else if (currantPlayer.isBet()){
                pointer++;
                if (pointer == playerList.size()) {
                    while (((GameParticipate) dealer).checkIfDrawCard()) {
                        dealer.dealCard((GameParticipate)dealer);
                    }
                    JOptionPane.showMessageDialog(null, "Dealer's turn!");
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Player " + currantPlayer.id + " should bet first.");
            }
            repaint();
        }
        //下注，在下注这边，一定要注意玩家的本金不能小于零，并且有能力在输了之后赔钱
        else if (selectedButton == btnBet && pointer < playerList.size()){
            String[] options = new String[] {"1", "5", "10", "25", "100"};
            int response = JOptionPane.showOptionDialog(null, "Please enter your betting amount!", "BETTING",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (response == 0){
                if(currantPlayer.money >= 1){
                    currantPlayer.money -= 1;
                    currantPlayer.moneyToBet = 1;
                }
            }
            else if (response == 1){
                if(currantPlayer.money >= 5){
                    currantPlayer.money -= 5;
                    currantPlayer.moneyToBet = 5;
                }
            }
            else if (response == 2){
                if(currantPlayer.money >= 10){
                    currantPlayer.money -= 10;
                    currantPlayer.moneyToBet = 10;
                }
            }
            else if (response == 3){
                if(currantPlayer.money >= 25){
                    currantPlayer.money -= 25;
                    currantPlayer.moneyToBet = 25;
                }
            }
            else if (response == 4){
                if(currantPlayer.money >= 100){
                    currantPlayer.money -= 100;
                    currantPlayer.moneyToBet = 100;
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "输入不合法！请重新选择");
            }
            repaint();
        }
        //全部重新开始
        else if (selectedButton == btnReStart){
            restart();
            repaint();
        }
    }
}
