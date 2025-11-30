package com.ticketmaster.gui;

import com.ticketmaster.model.Ticket;
import com.ticketmaster.model.TicketStatus;
import com.ticketmaster.model.User;
import com.ticketmaster.service.TicketService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

public class TicketDetailFrame extends JFrame {
    private Ticket ticket;
    private User currentUser;
    private TicketService ticketService;
    private JTextArea newUpdateArea;
    private JComboBox<TicketStatus> statusCombo;
    private JButton updateButton;
    private JLabel statusLabel;

    public TicketDetailFrame(Ticket ticket, User user) {
        this.ticket = ticket;
        this.currentUser = user;
        this.ticketService = new TicketService();
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("Ticket Detayı - " + ticket.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        
        newUpdateArea = new JTextArea(4, 40);
        newUpdateArea.setLineWrap(true);
        newUpdateArea.setWrapStyleWord(true);
        statusCombo = new JComboBox<>(TicketStatus.values());
        statusCombo.setSelectedItem(ticket.getStatus());
        updateButton = new JButton("Güncelle");
        
        ModernTheme.applyModernTextAreaStyle(newUpdateArea);
        ModernTheme.applyModernComboBoxStyle(statusCombo);
        ModernTheme.applyModernButtonStyle(updateButton);
        
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top panel with ticket info
        JPanel infoPanel = ModernTheme.createCardPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleTitle = new JLabel("Başlık");
        titleTitle.setFont(ModernTheme.FONT_BODY);
        titleTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        titleTitle.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(titleTitle, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel titleLabel = new JLabel(ticket.getTitle());
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        infoPanel.add(titleLabel, gbc);

        JLabel statusTitle = new JLabel("Durum");
        statusTitle.setFont(ModernTheme.FONT_BODY);
        statusTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        statusTitle.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        infoPanel.add(statusTitle, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        statusLabel = new JLabel(ticket.getStatus().getDisplayName());
        statusLabel.setFont(ModernTheme.FONT_SUBHEADING);
        statusLabel.setForeground(ModernTheme.PRIMARY_COLOR);
        infoPanel.add(statusLabel, gbc);
        
        // History button
        JButton historyButton = new JButton("Geçmiş");
        ModernTheme.applyModernButtonStyle(historyButton);
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TicketHistoryFrame(ticket).setVisible(true);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        infoPanel.add(historyButton, gbc);

        JLabel critTitle = new JLabel("Kritiklik");
        critTitle.setFont(ModernTheme.FONT_BODY);
        critTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        critTitle.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        infoPanel.add(critTitle, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel critLabel = new JLabel(ticket.getCriticalityLevel().getDisplayName());
        critLabel.setFont(ModernTheme.FONT_BODY);
        critLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        infoPanel.add(critLabel, gbc);

        JLabel creatorTitle = new JLabel("Oluşturan");
        creatorTitle.setFont(ModernTheme.FONT_BODY);
        creatorTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        creatorTitle.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        infoPanel.add(creatorTitle, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JLabel creatorLabel = new JLabel(ticket.getCreatedBy());
        creatorLabel.setFont(ModernTheme.FONT_BODY);
        creatorLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        infoPanel.add(creatorLabel, gbc);

        JLabel dateTitle = new JLabel("Oluşturulma Tarihi");
        dateTitle.setFont(ModernTheme.FONT_BODY);
        dateTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        dateTitle.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        infoPanel.add(dateTitle, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        JLabel dateLabel = new JLabel(ticket.getCreatedAt().format(formatter));
        dateLabel.setFont(ModernTheme.FONT_BODY);
        dateLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        infoPanel.add(dateLabel, gbc);

        add(infoPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout(5, 5));
        
        // Description panel
        JPanel descPanel = ModernTheme.createCardPanel();
        descPanel.setLayout(new BorderLayout());
        JLabel descTitle = new JLabel("Açıklama");
        descTitle.setFont(ModernTheme.FONT_SUBHEADING);
        descTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        descTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        descPanel.add(descTitle, BorderLayout.NORTH);
        JTextArea descriptionArea = new JTextArea(ticket.getDescription() != null ? ticket.getDescription() : "");
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        ModernTheme.applyModernTextAreaStyle(descriptionArea);
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        mainContentPanel.add(descPanel, BorderLayout.CENTER);

        // New update panel
        JPanel updatePanel = ModernTheme.createCardPanel();
        updatePanel.setLayout(new BorderLayout(10, 10));
        updatePanel.setPreferredSize(new Dimension(0, 300));
        updatePanel.setMinimumSize(new Dimension(0, 300));
        JLabel updateTitle = new JLabel("Yeni Güncelleme");
        updateTitle.setFont(ModernTheme.FONT_SUBHEADING);
        updateTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        updateTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        updatePanel.add(updateTitle, BorderLayout.NORTH);
        
        JPanel updateFormPanel = new JPanel(new GridBagLayout());
        updateFormPanel.setBackground(Color.WHITE);
        updateFormPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints ugbc = new GridBagConstraints();
        ugbc.insets = new Insets(8, 10, 8, 10);
        ugbc.anchor = GridBagConstraints.WEST;
        
        JLabel statusLabel = new JLabel("Yeni Durum:");
        statusLabel.setFont(ModernTheme.FONT_BODY);
        ugbc.gridx = 0;
        ugbc.gridy = 0;
        ugbc.fill = GridBagConstraints.NONE;
        ugbc.weightx = 0;
        updateFormPanel.add(statusLabel, ugbc);
        
        ugbc.gridx = 1;
        ugbc.fill = GridBagConstraints.HORIZONTAL;
        ugbc.weightx = 1.0;
        updateFormPanel.add(statusCombo, ugbc);
        
        JLabel updateLabel = new JLabel("Açıklama:");
        updateLabel.setFont(ModernTheme.FONT_BODY);
        ugbc.gridx = 0;
        ugbc.gridy = 1;
        ugbc.fill = GridBagConstraints.NONE;
        ugbc.weightx = 0;
        ugbc.anchor = GridBagConstraints.NORTHWEST;
        updateFormPanel.add(updateLabel, ugbc);
        
        ugbc.gridx = 1;
        ugbc.gridy = 1;
        ugbc.fill = GridBagConstraints.BOTH;
        ugbc.weightx = 1.0;
        ugbc.weighty = 1.0;
        newUpdateArea.setRows(8);
        newUpdateArea.setColumns(50);
        newUpdateArea.setPreferredSize(new Dimension(0, 180));
        newUpdateArea.setMinimumSize(new Dimension(0, 180));
        newUpdateArea.setEditable(true);
        JScrollPane updateScrollPane = new JScrollPane(newUpdateArea);
        updateScrollPane.setPreferredSize(new Dimension(0, 180));
        updateScrollPane.setMinimumSize(new Dimension(0, 180));
        updateFormPanel.add(updateScrollPane, ugbc);
        
        ugbc.gridx = 1;
        ugbc.gridy = 2;
        ugbc.fill = GridBagConstraints.NONE;
        ugbc.weightx = 0;
        ugbc.weighty = 0;
        ugbc.anchor = GridBagConstraints.EAST;
        updateFormPanel.add(updateButton, ugbc);
        
        updatePanel.add(updateFormPanel, BorderLayout.CENTER);
        
        // Add update panel directly
        mainContentPanel.add(updatePanel, BorderLayout.SOUTH);
        
        // Images panel
        if (ticket.getImagePaths() != null && !ticket.getImagePaths().isEmpty()) {
            JPanel imagesPanel = new JPanel(new BorderLayout());
            imagesPanel.setBorder(BorderFactory.createTitledBorder("Ekran Görüntüleri"));
            JPanel imageGrid = new JPanel(new GridLayout(0, 3, 5, 5));
            
            for (String imagePath : ticket.getImagePaths()) {
                try {
                    File imageFile = new File(imagePath);
                    if (!imageFile.exists()) {
                        // Try relative path
                        imageFile = new File("./" + imagePath);
                    }
                    
                    final File finalImageFile = imageFile; // Make final for inner class
                    
                    if (finalImageFile.exists()) {
                        BufferedImage img = ImageIO.read(finalImageFile);
                        if (img != null) {
                            ImageIcon icon = new ImageIcon(img.getScaledInstance(200, 150, Image.SCALE_SMOOTH));
                            JLabel imageLabel = new JLabel(icon);
                            imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                            imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            
                            // Add click listener to open image in new window
                            imageLabel.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    showImageFullScreen(finalImageFile);
                                }
                            });
                            
                            imageGrid.add(imageLabel);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error loading image: " + imagePath);
                    e.printStackTrace();
                }
            }
            
            imagesPanel.add(new JScrollPane(imageGrid), BorderLayout.CENTER);
            
            // Use split pane to show images alongside main content
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
                mainContentPanel, imagesPanel);
            splitPane.setDividerLocation(450);
            splitPane.setResizeWeight(0.6);
            remove(mainContentPanel);
            add(splitPane, BorderLayout.CENTER);
        } else {
            // If no images, just add main content panel
            add(mainContentPanel, BorderLayout.CENTER);
        }
        
        setupListeners();
    }
    
    
    private void setupListeners() {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TicketStatus newStatus = (TicketStatus) statusCombo.getSelectedItem();
                String updateDescription = newUpdateArea.getText().trim();
                
                if (updateDescription.isEmpty()) {
                    JOptionPane.showMessageDialog(TicketDetailFrame.this,
                            "Lütfen açıklama giriniz.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!ticketService.canChangeStatus(currentUser.getProfile(), ticket.getStatus(), newStatus)) {
                    JOptionPane.showMessageDialog(TicketDetailFrame.this,
                            "Bu kullanıcı profili bu duruma geçiş yapamaz!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                ticketService.updateTicketStatusAndResult(ticket.getId(), currentUser.getUsername(), newStatus, updateDescription);
                
                // Refresh ticket data
                Ticket updatedTicket = ticketService.getTicketById(ticket.getId());
                if (updatedTicket != null) {
                    ticket = updatedTicket;
                    statusLabel.setText(ticket.getStatus().getDisplayName());
                    statusCombo.setSelectedItem(ticket.getStatus());
                    newUpdateArea.setText("");
                }
                
                JOptionPane.showMessageDialog(TicketDetailFrame.this,
                        "Ticket durumu ve açıklama güncellendi!",
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void showImageFullScreen(File imageFile) {
        try {
            BufferedImage img = ImageIO.read(imageFile);
            if (img != null) {
                JFrame imageFrame = new JFrame("Resim - " + imageFile.getName());
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                imageFrame.add(new JScrollPane(imageLabel));
                imageFrame.setSize(800, 600);
                imageFrame.setLocationRelativeTo(null);
                imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                imageFrame.setVisible(true);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Resim yüklenirken hata oluştu: " + e.getMessage(),
                    "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}

