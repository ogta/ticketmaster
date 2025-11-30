package com.ticketmaster.gui;

import com.ticketmaster.model.User;
import com.ticketmaster.model.UserProfile;
import com.ticketmaster.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateUserFrame extends JFrame {
    private JTextField usernameField;
    private JComboBox<UserProfile> profileCombo;
    private JButton createButton;
    private JButton cancelButton;
    private UserService userService;

    public CreateUserFrame() {
        this.userService = new UserService();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Yeni Kullanıcı Ekle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        usernameField = new JTextField(20);
        profileCombo = new JComboBox<>(UserProfile.values());
        createButton = new JButton("Kullanıcı Oluştur");
        cancelButton = new JButton("İptal");

        ModernTheme.applyModernTextFieldStyle(usernameField);
        ModernTheme.applyModernComboBoxStyle(profileCombo);
        ModernTheme.applyModernButtonStyle(createButton);
        ModernTheme.applyModernButtonStyle(cancelButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ModernTheme.PRIMARY_COLOR);
        titlePanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        JLabel titleLabel = new JLabel("Yeni Kullanıcı Ekle", SwingConstants.CENTER);
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

        JLabel profileLabel = new JLabel("Profil");
        profileLabel.setFont(ModernTheme.FONT_SUBHEADING);
        profileLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        cardPanel.add(profileLabel, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cardPanel.add(profileCombo, gbc);

        JLabel infoLabel = new JLabel("<html><div style='text-align: center;'>" +
                "Yeni kullanıcının varsayılan şifresi 'test123' olacaktır.<br/>" +
                "Kullanıcı ilk girişinde şifresini değiştirmek zorunda kalacaktır.</div></html>");
        infoLabel.setFont(ModernTheme.FONT_SMALL);
        infoLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20, 20, 15, 20);
        cardPanel.add(infoLabel, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 10, 15, 10);
        cardPanel.add(createButton, gbc);

        gbc.gridx = 1;
        cardPanel.add(cancelButton, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        centerPanel.add(cardPanel);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateUserFrame.this,
                            "Lütfen kullanıcı adı giriniz.",
                            "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userService.isUsernameExists(username)) {
                    JOptionPane.showMessageDialog(CreateUserFrame.this,
                            "Bu kullanıcı adı zaten kullanılıyor.",
                            "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UserProfile profile = (UserProfile) profileCombo.getSelectedItem();
                if (profile == null) {
                    JOptionPane.showMessageDialog(CreateUserFrame.this,
                            "Lütfen bir profil seçiniz.",
                            "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = userService.createUser(username, profile);
                if (success) {
                    JOptionPane.showMessageDialog(CreateUserFrame.this,
                            "Kullanıcı başarıyla oluşturuldu.\n" +
                            "Kullanıcı adı: " + username + "\n" +
                            "Varsayılan şifre: test123",
                            "Başarılı",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CreateUserFrame.this,
                            "Kullanıcı oluşturulurken bir hata oluştu.",
                            "Hata",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}

