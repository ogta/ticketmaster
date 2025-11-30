package com.ticketmaster.gui;

import com.ticketmaster.model.CriticalityLevel;
import com.ticketmaster.model.Ticket;
import com.ticketmaster.model.User;
import com.ticketmaster.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreateTicketFrame extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<CriticalityLevel> criticalityCombo;
    private JList<String> imageList;
    private DefaultListModel<String> imageListModel;
    private JButton addImageButton;
    private JButton removeImageButton;
    private JButton saveButton;
    private TicketService ticketService;
    private User currentUser;
    private List<String> selectedImagePaths;

    public CreateTicketFrame(User user) {
        this.currentUser = user;
        this.ticketService = new TicketService();
        this.selectedImagePaths = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Yeni Ticket Oluştur");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        titleField = new JTextField(30);
        descriptionArea = new JTextArea(7, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setMinimumSize(new Dimension(0, descriptionArea.getFontMetrics(descriptionArea.getFont()).getHeight() * 7));
        criticalityCombo = new JComboBox<>(CriticalityLevel.values());
        imageListModel = new DefaultListModel<>();
        imageList = new JList<>(imageListModel);
        imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addImageButton = new JButton("Resim Ekle");
        removeImageButton = new JButton("Resmi Kaldır");
        saveButton = new JButton("Kaydet");
        
        ModernTheme.applyModernTextFieldStyle(titleField);
        ModernTheme.applyModernTextAreaStyle(descriptionArea);
        ModernTheme.applyModernComboBoxStyle(criticalityCombo);
        ModernTheme.applyModernButtonStyle(addImageButton);
        ModernTheme.applyModernButtonStyle(removeImageButton);
        ModernTheme.applyModernButtonStyle(saveButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel cardPanel = ModernTheme.createCardPanel();
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Ticket Başlığı");
        titleLabel.setFont(ModernTheme.FONT_SUBHEADING);
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cardPanel.add(titleField, gbc);

        // Description
        JLabel descLabel = new JLabel("Açıklama");
        descLabel.setFont(ModernTheme.FONT_SUBHEADING);
        descLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        cardPanel.add(descLabel, gbc);
        
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(0, descriptionArea.getFontMetrics(descriptionArea.getFont()).getHeight() * 7 + 20));
        cardPanel.add(descScroll, gbc);

        // Criticality
        JLabel critLabel = new JLabel("Kritiklik Seviyesi");
        critLabel.setFont(ModernTheme.FONT_SUBHEADING);
        critLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        cardPanel.add(critLabel, gbc);
        
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardPanel.add(criticalityCombo, gbc);

        // Images section
        JLabel imageLabel = new JLabel("Ekran Görüntüleri");
        imageLabel.setFont(ModernTheme.FONT_SUBHEADING);
        imageLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        cardPanel.add(imageLabel, gbc);
        
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.3;
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setBorder(ModernTheme.INPUT_BORDER);
        JScrollPane imageScroll = new JScrollPane(imageList);
        imageScroll.setPreferredSize(new Dimension(0, 150));
        imagePanel.add(imageScroll, BorderLayout.CENTER);
        JPanel imageButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        imageButtonPanel.setBackground(Color.WHITE);
        imageButtonPanel.add(addImageButton);
        imageButtonPanel.add(removeImageButton);
        imagePanel.add(imageButtonPanel, BorderLayout.SOUTH);
        cardPanel.add(imagePanel, gbc);

        // Save button
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 0;
        gbc.insets = new Insets(20, 15, 10, 15);
        cardPanel.add(saveButton, gbc);

        add(cardPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        addImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Resim Dosyaları", "jpg", "jpeg", "png", "gif", "bmp");
                fileChooser.setFileFilter(filter);
                
                int result = fileChooser.showOpenDialog(CreateTicketFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    for (File file : files) {
                        String path = file.getAbsolutePath();
                        selectedImagePaths.add(path);
                        imageListModel.addElement(file.getName());
                    }
                }
            }
        });

        removeImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = imageList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    selectedImagePaths.remove(selectedIndex);
                    imageListModel.remove(selectedIndex);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();
                CriticalityLevel criticality = (CriticalityLevel) criticalityCombo.getSelectedItem();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateTicketFrame.this,
                            "Lütfen ticket başlığı giriniz.",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Ticket ticket = new Ticket();
                ticket.setTitle(title);
                ticket.setDescription(description);
                ticket.setCriticalityLevel(criticality);
                ticket.setCreatedBy(currentUser.getUsername());
                ticket.setImagePaths(selectedImagePaths);

                ticketService.createTicket(ticket);
                JOptionPane.showMessageDialog(CreateTicketFrame.this,
                        "Ticket başarıyla oluşturuldu!",
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
    }
}

