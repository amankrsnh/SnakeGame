/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amcoder.snake_game;

import com.amcoder.dto.User;
import javax.swing.JFrame;

/**
 *
 * @author Aman Kumar Singh
 */
public class SnakeFrame extends JFrame
{
    SnakeFrame(User user)
    {
        this.add(new SnakePanel(user));
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
