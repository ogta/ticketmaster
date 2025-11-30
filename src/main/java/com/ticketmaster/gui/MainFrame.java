package com.ticketmaster.gui;

import com.ticketmaster.model.User;
import com.ticketmaster.model.UserProfile;
import com.ticketmaster.service.TicketService;
import com.ticketmaster.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private User currentUser;
    private JLabel usernameLabel;
    private TicketService ticketService;
    private UserService userService;

    public MainFrame(User user) {
        this.currentUser = user;
        this.ticketService = new TicketService();
        this.userService = new UserService();
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("TicketMaster - Ana Ekran");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        // Top panel with welcome message and username
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ModernTheme.PRIMARY_COLOR);
        topPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel welcomeLabel = new JLabel("TicketMaster'a Hoş Geldiniz!");
        welcomeLabel.setFont(ModernTheme.FONT_TITLE);
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        usernameLabel = new JLabel("Kullanıcı: " + currentUser.getUsername());
        usernameLabel.setFont(ModernTheme.FONT_SUBHEADING);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(usernameLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(ModernTheme.CARD_COLOR);
        menuBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JMenu ticketMenu = new JMenu("Ticket İşlemleri");
        ticketMenu.setFont(ModernTheme.FONT_BODY);
        ticketMenu.setForeground(ModernTheme.TEXT_PRIMARY);
        
        JMenuItem createTicketItem = new JMenuItem("Yeni Ticket Oluştur");
        createTicketItem.setFont(ModernTheme.FONT_BODY);
        createTicketItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateTicketFrame(currentUser).setVisible(true);
            }
        });

        JMenuItem listTicketsItem = new JMenuItem("Mevcut Ticketları Listele");
        listTicketsItem.setFont(ModernTheme.FONT_BODY);
        listTicketsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TicketListFrame(currentUser).setVisible(true);
            }
        });

        ticketMenu.add(createTicketItem);
        ticketMenu.add(listTicketsItem);
        menuBar.add(ticketMenu);
        
        // User Management menu (only for admin username and BANKA profile)
        if (currentUser.getUsername().equals("admin") || currentUser.getProfile() == UserProfile.BANKA) {
            JMenu userMenu = new JMenu("Kullanıcı Yönetimi");
            userMenu.setFont(ModernTheme.FONT_BODY);
            userMenu.setForeground(ModernTheme.TEXT_PRIMARY);
            
            JMenuItem createUserItem = new JMenuItem("Yeni Kullanıcı Ekle");
            createUserItem.setFont(ModernTheme.FONT_BODY);
            createUserItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new CreateUserFrame().setVisible(true);
                }
            });
            
            userMenu.add(createUserItem);
            menuBar.add(userMenu);
        }
        
        // Settings menu
        JMenu settingsMenu = new JMenu("Ayarlar");
        settingsMenu.setFont(ModernTheme.FONT_BODY);
        settingsMenu.setForeground(ModernTheme.TEXT_PRIMARY);
        JMenuItem settingsItem = new JMenuItem("Kullanıcı Ayarları");
        settingsItem.setFont(ModernTheme.FONT_BODY);
        settingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsFrame(currentUser).setVisible(true);
            }
        });
        settingsMenu.add(settingsItem);
        menuBar.add(settingsMenu);
        
        setJMenuBar(menuBar);

        // Center panel with dashboard cards
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Dashboard cards
        long totalTickets = ticketService.getTotalTicketCount();
        long completedTickets = ticketService.getCompletedTicketCount();
        long userCount = userService.getUserCount();
        
        // Total Tickets Card
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(createDashboardCard("Toplam Ticket", String.valueOf(totalTickets), ModernTheme.PRIMARY_COLOR), gbc);
        
        // Completed Tickets Card
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(createDashboardCard("Tamamlanan Ticket", String.valueOf(completedTickets), new Color(34, 139, 34)), gbc);
        
        // User Count Card
        gbc.gridx = 2;
        gbc.gridy = 0;
        centerPanel.add(createDashboardCard("Kullanıcı Sayısı", String.valueOf(userCount), new Color(255, 140, 0)), gbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardCard(String title, String value, Color accentColor) {
        JPanel card = ModernTheme.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 140));
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ModernTheme.FONT_SUBHEADING);
        titleLabel.setForeground(ModernTheme.TEXT_SECONDARY);
        card.add(titleLabel, BorderLayout.NORTH);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(ModernTheme.FONT_TITLE.getFontName(), Font.BOLD, 48));
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}

