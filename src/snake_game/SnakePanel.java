/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amcoder.snake_game;

import com.amcoder.dao.UserDao;
import com.amcoder.dto.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Aman Kumar Singh
 */
public class SnakePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH=1200;
    static final int SCREEN_HEIGHT=1000;
    static final int UNIT_SIZE=25;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY=100;
    int bodyParts=2;
    int x[]=new int[GAME_UNITS];
    int y[]=new int[GAME_UNITS];
    int eatenfood=0;
    int foodx;
    int foody;
    char direction='R';
    boolean running=false;
    Timer timer;
    Random random;
    User user=null;
    SnakePanel(User user)
    {
        this.user=user;
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new SnakeKeyAdapter());
        gameStart();
    }
    public void gameStart()
    {
        newFood();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        if(running)
        {
            g.setColor(Color.red);
            g.fillOval(foodx, foody, UNIT_SIZE, UNIT_SIZE);
            
            for(int i=0;i<bodyParts;++i)
            {
                if(i==0)
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else
                {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics=getFontMetrics(g.getFont());
            g.drawString("Score: "+(eatenfood*10),0,g.getFont().getSize());
            if(eatenfood*10>user.getUserscore())
                user.setUserscore(eatenfood*10);
            g.drawString("HighScore: "+user.getUserscore(),SCREEN_WIDTH-metrics.stringWidth("HighScore: "+user.getUserscore()),g.getFont().getSize());
        }
        else
        {
            try
            {
                if(UserDao.getUserScore(user)<eatenfood*10)
                {
                    UserDao.updateUserScore(user);
                }
            }
            catch(SQLException sqle)
            {
                sqle.printStackTrace();
            }
            gameOver(g);
        }
    }
    public void newFood()
    {
        foodx=random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foody=random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move()
    {
        for(int i=bodyParts;i>0;--i)
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction)
        {
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
        }
    }
    public void checkFood()
    {
        if(x[0]==foodx && y[0]==foody)
        {
            bodyParts++;
            eatenfood++;
            newFood();
        }
    }
    public void checkCollision()
    {
        //check if head collides with body
        for(int i=bodyParts;i>0;--i)
        {
            if(x[0]==x[i] && y[0]==y[i])
                running=false;
        }
        
        //check if head collides with left boundary
        if(x[0]<0)
            running=false;
        
        //check if(head collides with right boundary
        if(x[0]>SCREEN_WIDTH)
            running=false;
        
        //check if head collides with upper boundary
        if(y[0]<0)
            running=false;
        
        //check if head collide with bottom boundary
        if(y[0]>SCREEN_HEIGHT)
            running=false;
    }
    public void gameOver(Graphics g)
    {
        //For Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics1=getFontMetrics(g.getFont());
        g.drawString("Score: "+(eatenfood*10),(SCREEN_WIDTH-metrics1.stringWidth("Score: "+(eatenfood*10)))/2,g.getFont().getSize());
        
        //For GameOver text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics2=getFontMetrics(g.getFont());
        g.drawString("GAME OVER",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        
        //For restart text
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free",Font.BOLD,20));
        FontMetrics metrics3=getFontMetrics(g.getFont());
        g.drawString("Press enter to play again!",(SCREEN_WIDTH - metrics3.stringWidth("Press enter to play again!"))-2, SCREEN_HEIGHT-g.getFont().getSize());
        g.drawString("Press 0 to exit!",0, SCREEN_HEIGHT-g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }
    public class SnakeKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {   
            if(!running && e.getKeyCode()==KeyEvent.VK_ENTER)
            {
                Login.closeFrame(new SnakeFrame(user));
            }
            else if(!running && e.getKeyCode()==KeyEvent.VK_0)
            {
                new Login().setVisible(true);
                Login.closeFrame(null);
            }
            switch(e.getKeyCode()) 
            {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') 
                    {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') 
                    {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') 
                    {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') 
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
