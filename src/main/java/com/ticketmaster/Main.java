package com.ticketmaster;

import com.ticketmaster.gui.LoginFrame;

import javax.swing.*;

public class Main {
    static {
        // Load SQLite JDBC driver early
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded in Main class");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: SQLite JDBC driver not found!");
            System.err.println("Please do the following:");
            System.err.println("1. In IntelliJ: View > Tool Windows > Maven");
            System.err.println("2. Click 'Reload All Maven Projects' button");
            System.err.println("3. Or: File > Invalidate Caches / Restart");
            System.err.println("4. Make sure Run Configuration uses 'Use classpath of module: ticketmaster'");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LoginFrame().setVisible(true);
            }
        });
    }
}

