package app;

import javax.swing.*;

public class App {

    //整个游戏有两个状态，一个是MENU一个是MAIN，用于不同frame的切换
    enum STATE{
        MENU, GAME
    };

    public static JFrame menuFrame = new JFrame();
    public static JFrame gameFrame = new JFrame();

    public static STATE currentState = STATE.MENU;

    public static void main(String[] args) {
        if (currentState == STATE.MENU)//当现在的状态为MENU的时候就启动menuFrame
        {
            initMenu();
        }
    }

    //启动MENU FRAME
    public static void initMenu(){
        menuFrame.setTitle("BLACKJACK!"); //将框框的标题设置为BLACKJACK
        menuFrame.setSize(1530, 865);
        menuFrame.setLocationRelativeTo(null); //把frame定位到中央
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false); //user不能自定义frame的大小

        Menu menu = new Menu();//new一个menuComponent对象
        menuFrame.add(menu);//将menuComponent 加入menuFrame这个框中
        menuFrame.setVisible(true);//可见
    }

    //启动GAME FRAME
    public static void initGame(){
        gameFrame.setTitle("BLACKJACK!");
        gameFrame.setSize(1530, 865);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        Game game = new Game();
        gameFrame.add(game);
        gameFrame.setVisible(true);
    }

}
