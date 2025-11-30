package com.ticketmaster.gui;

import com.ticketmaster.model.User;
import com.ticketmaster.service.UserService;
import com.ticketmaster.util.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserService userService;
    private static final String DEFAULT_PASSWORD = "test123";

    public LoginFrame() {
        userService = new UserService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("TicketMaster - Giriş");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Giriş Yap");
        
        ModernTheme.applyModernTextFieldStyle(usernameField);
        ModernTheme.applyModernPasswordFieldStyle(passwordField);
        ModernTheme.applyModernButtonStyle(loginButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ModernTheme.PRIMARY_COLOR);
        titlePanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        JLabel titleLabel = new JLabel("TicketMaster", SwingConstants.CENTER);
        titleLabel.setFont(ModernTheme.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main card panel
        JPanel cardPanel = ModernTheme.createCardPanel();
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Kullanıcı Adı");
        usernameLabel.setFont(ModernTheme.FONT_SUBHEADING);
        usernameLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cardPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Şifre");
        passwordLabel.setFont(ModernTheme.FONT_SUBHEADING);
        passwordLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        cardPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cardPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 20, 15, 20);
        cardPanel.add(loginButton, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        centerPanel.add(cardPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Lütfen kullanıcı adı ve şifre giriniz.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = userService.login(username, password);
                if (user == null) {
                    // Try default password
                    if (password.equals(DEFAULT_PASSWORD)) {
                        user = userService.findByUsername(username);
                        if (user != null && !user.isPasswordChanged()) {
                            // First login, need to change password
                            dispose();
                            new ChangePasswordFrame(username).setVisible(true);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Kullanıcı adı veya şifre hatalı!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!user.isPasswordChanged() && password.equals(DEFAULT_PASSWORD)) {
                    dispose();
                    new ChangePasswordFrame(username).setVisible(true);
                } else {
                    dispose();
                    new MainFrame(user).setVisible(true);
                }
            }
        });

        // Enter key support
        passwordField.addActionListener(e -> loginButton.doClick());
    }
}

