package com.ticketmaster.gui;

import com.ticketmaster.model.User;
import com.ticketmaster.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordFrame extends JFrame {
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton changeButton;
    private UserService userService;
    private String username;

    public ChangePasswordFrame(String username) {
        this.username = username;
        userService = new UserService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Şifre Değiştir");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        changeButton = new JButton("Şifreyi Değiştir");
        
        ModernTheme.applyModernPasswordFieldStyle(newPasswordField);
        ModernTheme.applyModernPasswordFieldStyle(confirmPasswordField);
        ModernTheme.applyModernButtonStyle(changeButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ModernTheme.PRIMARY_COLOR);
        titlePanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        JLabel titleLabel = new JLabel("İlk Giriş - Şifre Değiştirme", SwingConstants.CENTER);
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main card panel
        JPanel cardPanel = ModernTheme.createCardPanel();
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel newPassLabel = new JLabel("Yeni Şifre");
        newPassLabel.setFont(ModernTheme.FONT_SUBHEADING);
        newPassLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(newPassLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cardPanel.add(newPasswordField, gbc);

        JLabel confirmLabel = new JLabel("Şifre Tekrar");
        confirmLabel.setFont(ModernTheme.FONT_SUBHEADING);
        confirmLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        cardPanel.add(confirmLabel, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cardPanel.add(confirmPasswordField, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 20, 20, 20);
        cardPanel.add(changeButton, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        centerPanel.add(cardPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(ChangePasswordFrame.this,
                            "Lütfen tüm alanları doldurunuz.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(ChangePasswordFrame.this,
                            "Şifreler eşleşmiyor!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (newPassword.length() < 3) {
                    JOptionPane.showMessageDialog(ChangePasswordFrame.this,
                            "Şifre en az 3 karakter olmalıdır.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                userService.changePassword(username, newPassword);
                JOptionPane.showMessageDialog(ChangePasswordFrame.this,
                        "Şifre başarıyla değiştirildi!",
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);

                User user = userService.findByUsername(username);
                dispose();
                new MainFrame(user).setVisible(true);
            }
        });

        confirmPasswordField.addActionListener(e -> changeButton.doClick());
    }
}

