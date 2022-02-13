package com.hesoyam.mercury;

import com.hesoyam.mercury.form.MainJPanel;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        MainJPanel mainPanel = new MainJPanel();

        JFrame frame = new JFrame("App");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
