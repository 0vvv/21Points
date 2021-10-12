package app;

import entity.GameParticipate;
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
    // 总玩家数
    public static final int PLAYER_COUNT = 5;
    // 每个player在画面上所占的区域
    public static final int PLAYER_AREA_WIDTH = 300;

    private List<GameParticipate> playerList;
    private Dealer dealer;
    private BufferedImage backgroundImg;

    // 标识当前回合在哪个玩家
    private int pointer;
    private boolean isEnd;
    GameParticipate currantPlayer;


    //游戏的主界面一共有五个按钮，分别是HIT STAND EXIT BET RESET
    private JButton btnHit = new JButton("HIT"); // 玩家点击HIT来获得自己牌
    private JButton btnStand = new JButton("STAND"); // 玩家点击STAND结束本局
    private JButton btnExit = new JButton("EXIT"); // 玩家点击EXIT来回到主界面
    private JButton btnBet = new JButton("BET"); // 下注
    private JButton btnReStart = new JButton("RESET"); // 重新开始

    public Game() {
        //增加每一个按钮的监听
        btnExit.addActionListener(this);
        btnHit.addActionListener(this);
        btnBet.addActionListener(this);
        btnStand.addActionListener(this);
        btnReStart.addActionListener(this);

        playerList = new ArrayList<>();
        dealer = new Dealer(new Hand(), 500000000, new Deck());

        for (int i = 0; i < PLAYER_COUNT; i++) {
            GameParticipate player = new Player(new Hand(), 500, i);
            playerList.add(player);
        }

        restart();
        repaint();
    }

    //构建gameFrame的界面
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        setButton();

        try {
            backgroundImg = ImageIO.read(new File("resources/background.png")); //读取背景
        } catch (IOException e) {
            System.out.println("背景图片读取失败");
        }
        g2.drawImage(backgroundImg, 0, 0, 1530, 865, null);

        if (pointer < playerList.size()) {
            currantPlayer = playerList.get(pointer);
        } else {
            currantPlayer = dealer;
        }

        if (pointer >= playerList.size()) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.drawString("DEALER", 750, 50);

        //打印卡牌
        for (int i = 0; i < PLAYER_COUNT; i++) {
            g2.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            if (i == currantPlayer.id) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.WHITE);
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

        List<Card> dealerCardList = dealer.getCardList();
        // 所有玩家都摸完牌，庄家展示所有牌
        if (pointer >= playerList.size()) {
            g2.setColor(Color.RED);
            g2.drawString("DEALER SCORE: " + dealer.getScore(), 50, 80);
            for (int i = 0; i < dealerCardList.size(); i++) {
                dealerCardList.get(i).printCards(g2, true, i, -1);
            }
        } else {
            g2.setColor(Color.WHITE);
            g2.drawString("DEALER SCORE: ****", 50, 80);
            dealerCardList.get(0).printCards(g2, true, 0, -1);
        }

        //将按钮添加到这个frame里面
        super.add(btnBet);
        super.add(btnExit);
        super.add(btnHit);
        super.add(btnStand);
        super.add(btnReStart);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton selectedButton = (JButton) e.getSource();

        //如果是EXIT则回到主界面
        if (selectedButton == btnExit) {
            App.gameFrame.dispose();
            App.currentState = App.STATE.MENU; // 将现在的状态切换成MENU
            App.initMenu();
        }

        //全部重新开始
        if (selectedButton == btnReStart) {
            restart();
            repaint();
        }

        if (this.isEnd) {
            JOptionPane.showMessageDialog(null, "请点击reset开始新一局，或exit退出游戏");
            return;
        }

        //给玩家发牌
        if (selectedButton == btnHit && pointer < playerList.size()) {
            // 判断玩家是否已经下注，如果未下注那么我们将出现提示，下了注之后才能开始游戏
            if (currantPlayer.isBet()) {
                dealer.dealCard(currantPlayer);
                repaint();
                currantPlayer.flag = false;
                //如果玩家爆牌了，直接退出游戏
                if (currantPlayer.isBomb()) {
                    JOptionPane.showMessageDialog(null, "Player " + currantPlayer.id + " fail!");
                    dealer.liquidateAssets(currantPlayer);
                    currantPlayer.isEnd = true;
                    pointer++;

                    checkIfDealerTurn();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Player " + currantPlayer.id + " should bet first.");
            }
            repaint();
        }

        //玩家停止抽牌开始比大小
        if (selectedButton == btnStand) {
            currantPlayer.flag = true;
            // 庄家轮次
            if (pointer >= playerList.size()) {
                JOptionPane.showMessageDialog(null, "庄家摸牌完毕，开始清算。");
                for (GameParticipate player : playerList) {
                    if (!player.isEnd) {
                        dealer.liquidateAssets(player);
                    }
                }
                this.isEnd = true;

                repaint();
            } else if (currantPlayer.isBet()) {
                currantPlayer.flag = false;
                pointer++;
                checkIfDealerTurn();
            } else {
                JOptionPane.showMessageDialog(null, "Player " + currantPlayer.id + " should bet first.");
            }
            repaint();
        }

        //下注，在下注这边，一定要注意玩家的本金不能小于零，并且有能力在输了之后赔钱
        if (selectedButton == btnBet && pointer < playerList.size()) {
            String[] options = new String[]{"1", "5", "10", "25", "100"};
            int response = JOptionPane.showOptionDialog(null, "Please enter your betting amount!", "BETTING",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (response == 0 && currantPlayer.flag) {
                if (currantPlayer.money >= 1) {
                    currantPlayer.money -= 1;
                    currantPlayer.moneyToBet = 1;
                    currantPlayer.flag = false;
                }
            } else if (response == 1 && currantPlayer.flag) {
                if (currantPlayer.money >= 5) {
                    currantPlayer.money -= 5;
                    currantPlayer.moneyToBet = 5;
                    currantPlayer.flag = false;
                }
            } else if (response == 2 && currantPlayer.flag) {
                if (currantPlayer.money >= 10) {
                    currantPlayer.money -= 10;
                    currantPlayer.moneyToBet = 10;
                    currantPlayer.flag = false;
                }
            } else if (response == 3 && currantPlayer.flag) {
                if (currantPlayer.money >= 25) {
                    currantPlayer.money -= 25;
                    currantPlayer.moneyToBet = 25;
                    currantPlayer.flag = false;
                }
            } else if (response == 4 && currantPlayer.flag) {
                if (currantPlayer.money >= 100) {
                    currantPlayer.money -= 100;
                    currantPlayer.moneyToBet = 100;
                    currantPlayer.flag = false;
                }
            } else if (currantPlayer.flag == false) {
                JOptionPane.showMessageDialog(null, "玩家一轮只能下一次注！请摸牌或者摊牌！");
            } else {
                JOptionPane.showMessageDialog(null, "输入不合法！请重新选择");
            }
            repaint();
        }
    }

    private void checkIfDealerTurn() {
        if (pointer == playerList.size()) {
            while (((GameParticipate) dealer).checkIfDrawCard()) {
                dealer.dealCard(dealer);
            }
            JOptionPane.showMessageDialog(null, "Dealer's turn!");
        }
        repaint();
    }

    //单局的重新开始
    private void restart() {
        this.isEnd = false;
        System.out.println("游戏开始新一局");

        // 清零数据
        dealer.clear();
        for (GameParticipate player : playerList) {
            player.clear();
            player.flag = true;
        }

        // 洗牌
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

    private void setButton() {
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
    }
}
