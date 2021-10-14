package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JComponent implements ActionListener {
    //一共有两个按钮一个PLAY和EXIT
    private JButton btnExit = new JButton("Exit");//退出游戏
    private JButton btnPlay = new JButton("Play");//开始游戏

    private static BufferedImage backgroundImage;
    private BufferedImage menuBGImg;

    public Menu(){
        btnExit.addActionListener(this);
        btnPlay.addActionListener(this);
        try{
            menuBGImg = ImageIO.read(new File("resources/menubg.jpg"));
        }
        catch(IOException e) {
            System.out.println("背景图片读取失败");
        }
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        g2.setColor(Color.BLACK);

        g2.drawImage(menuBGImg,0,0,1530,865,null);


        btnPlay.setBounds(500, 600, 150, 80); //set button bounds
        btnExit.setBounds(800, 600, 150, 80);

        btnPlay.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
        btnExit.setFont(new Font("Comic Sans MS", Font.BOLD, 40));

        super.add(btnPlay);
        super.add(btnExit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton selectedButton = (JButton)e.getSource();

        if (selectedButton == btnExit) {
            System.exit(0);
        } else if (selectedButton == btnPlay){
            //关闭menuFrame打开mainFrame
            App.currentState = App.STATE.GAME;
            App.menuFrame.dispose();
            App.initGame();
        }
    }
}
