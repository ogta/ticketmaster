package com.ticketmaster.gui;

import com.ticketmaster.model.User;
import com.ticketmaster.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsFrame extends JFrame {
    private User currentUser;
    private UserService userService;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton changePasswordButton;
    private JLabel usernameLabel;
    private JLabel profileLabel;

    public SettingsFrame(User user) {
        this.currentUser = user;
        this.userService = new UserService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Kullanıcı Ayarları");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        changePasswordButton = new JButton("Şifreyi Değiştir");
        
        ModernTheme.applyModernPasswordFieldStyle(currentPasswordField);
        ModernTheme.applyModernPasswordFieldStyle(newPasswordField);
        ModernTheme.applyModernPasswordFieldStyle(confirmPasswordField);
        ModernTheme.applyModernButtonStyle(changePasswordButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // User info panel
        JPanel userInfoPanel = ModernTheme.createCardPanel();
        userInfoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userInfoTitle = new JLabel("Kullanıcı Bilgileri");
        userInfoTitle.setFont(ModernTheme.FONT_SUBHEADING);
        userInfoTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        userInfoPanel.add(userInfoTitle, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel usernameTitle = new JLabel("Kullanıcı Adı:");
        usernameTitle.setFont(ModernTheme.FONT_BODY);
        usernameTitle.setForeground(ModernTheme.TEXT_SECONDARY);
        userInfoPanel.add(usernameTitle, gbc);
        gbc.gridx = 1;
        usernameLabel = new JLabel(currentUser.getUsername());
        usernameLabel.setFont(ModernTheme.FONT_SUBHEADING);
        usernameLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        userInfoPanel.add(usernameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel profileTitle = new JLabel("Profil:");
        profileTitle.setFont(ModernTheme.FONT_BODY);
        profileTitle.setForeground(ModernTheme.TEXT_SECONDARY);
        userInfoPanel.add(profileTitle, gbc);
        gbc.gridx = 1;
        profileLabel = new JLabel(currentUser.getProfile().getDisplayName());
        profileLabel.setFont(ModernTheme.FONT_SUBHEADING);
        profileLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        userInfoPanel.add(profileLabel, gbc);

        add(userInfoPanel, BorderLayout.NORTH);

        // Password change panel
        JPanel passwordPanel = ModernTheme.createCardPanel();
        passwordPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel passwordTitle = new JLabel("Şifre Değiştir");
        passwordTitle.setFont(ModernTheme.FONT_SUBHEADING);
        passwordTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        passwordPanel.add(passwordTitle, gbc);

        JLabel currentLabel = new JLabel("Mevcut Şifre");
        currentLabel.setFont(ModernTheme.FONT_SUBHEADING);
        currentLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        passwordPanel.add(currentLabel, gbc);
        
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordPanel.add(currentPasswordField, gbc);

        JLabel newLabel = new JLabel("Yeni Şifre");
        newLabel.setFont(ModernTheme.FONT_SUBHEADING);
        newLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        passwordPanel.add(newLabel, gbc);
        
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordPanel.add(newPasswordField, gbc);

        JLabel confirmLabel = new JLabel("Yeni Şifre Tekrar");
        confirmLabel.setFont(ModernTheme.FONT_SUBHEADING);
        confirmLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        passwordPanel.add(confirmLabel, gbc);
        
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordPanel.add(confirmPasswordField, gbc);

        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.insets = new Insets(20, 15, 10, 15);
        passwordPanel.add(changePasswordButton, gbc);

        add(passwordPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPassword = new String(currentPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(SettingsFrame.this,
                            "Lütfen tüm alanları doldurunuz.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Verify current password
                User user = userService.login(currentUser.getUsername(), currentPassword);
                if (user == null) {
                    JOptionPane.showMessageDialog(SettingsFrame.this,
                            "Mevcut şifre hatalı!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    currentPasswordField.setText("");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(SettingsFrame.this,
                            "Yeni şifreler eşleşmiyor!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    newPasswordField.setText("");
                    confirmPasswordField.setText("");
                    return;
                }

                if (newPassword.length() < 3) {
                    JOptionPane.showMessageDialog(SettingsFrame.this,
                            "Şifre en az 3 karakter olmalıdır.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (currentPassword.equals(newPassword)) {
                    JOptionPane.showMessageDialog(SettingsFrame.this,
                            "Yeni şifre mevcut şifre ile aynı olamaz!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                userService.changePassword(currentUser.getUsername(), newPassword);
                JOptionPane.showMessageDialog(SettingsFrame.this,
                        "Şifre başarıyla değiştirildi!",
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                currentPasswordField.setText("");
                newPasswordField.setText("");
                confirmPasswordField.setText("");
            }
        });

        confirmPasswordField.addActionListener(e -> changePasswordButton.doClick());
    }
}

