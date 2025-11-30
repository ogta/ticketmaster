package com.ticketmaster.gui;

import com.ticketmaster.model.Ticket;
import com.ticketmaster.model.TicketStatus;
import com.ticketmaster.model.User;
import com.ticketmaster.model.UserProfile;
import com.ticketmaster.service.TicketService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketListFrame extends JFrame {
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private TicketService ticketService;
    private User currentUser;
    private JTextField idFilterField;
    private JTextField descriptionFilterField;
    private JComboBox<TicketStatus> statusFilterCombo;
    private JButton filterButton;
    private JButton refreshButton;
    private JButton clearFilterButton;
    private List<Ticket> allTickets;

    public TicketListFrame(User user) {
        this.currentUser = user;
        this.ticketService = new TicketService();
        initializeComponents();
        setupLayout();
        loadTickets();
    }

    private void initializeComponents() {
        setTitle("Ticket Listesi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        String[] columnNames = {"ID", "Ticket Adı", "Oluşturulma Tarihi", "Oluşturan Kullanıcı", "Durum"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ticketTable = new JTable(tableModel);
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.getTableHeader().setReorderingAllowed(false);
        ModernTheme.styleTable(ticketTable);

        idFilterField = new JTextField(10);
        descriptionFilterField = new JTextField(20);
        statusFilterCombo = new JComboBox<>();
        statusFilterCombo.addItem(null); // "Tümü" seçeneği
        for (TicketStatus status : TicketStatus.values()) {
            statusFilterCombo.addItem(status);
        }
        filterButton = new JButton("Filtrele");
        refreshButton = new JButton("Yenile");
        clearFilterButton = new JButton("Filtreyi Temizle");
        
        ModernTheme.applyModernTextFieldStyle(idFilterField);
        ModernTheme.applyModernTextFieldStyle(descriptionFilterField);
        ModernTheme.applyModernComboBoxStyle(statusFilterCombo);
        ModernTheme.applyModernButtonStyle(filterButton);
        ModernTheme.applyModernButtonStyle(refreshButton);
        ModernTheme.applyModernButtonStyle(clearFilterButton);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // Filter panel
        JPanel filterPanel = ModernTheme.createCardPanel();
        filterPanel.setLayout(new GridBagLayout());
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 10, 8, 10);
        fgbc.anchor = GridBagConstraints.WEST;
        
        JLabel filterTitle = new JLabel("Filtreleme");
        filterTitle.setFont(ModernTheme.FONT_SUBHEADING);
        filterTitle.setForeground(ModernTheme.TEXT_PRIMARY);
        fgbc.gridx = 0;
        fgbc.gridy = 0;
        fgbc.gridwidth = 1;
        filterPanel.add(filterTitle, fgbc);
        
        fgbc.gridx = 1;
        fgbc.insets = new Insets(8, 5, 8, 10);
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(ModernTheme.FONT_BODY);
        filterPanel.add(idLabel, fgbc);
        
        fgbc.gridx = 2;
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.weightx = 0.1;
        filterPanel.add(idFilterField, fgbc);
        
        fgbc.gridx = 3;
        fgbc.fill = GridBagConstraints.NONE;
        fgbc.weightx = 0;
        fgbc.insets = new Insets(8, 15, 8, 5);
        JLabel descLabel = new JLabel("Açıklama:");
        descLabel.setFont(ModernTheme.FONT_BODY);
        filterPanel.add(descLabel, fgbc);
        
        fgbc.gridx = 4;
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.weightx = 0.2;
        fgbc.insets = new Insets(8, 5, 8, 10);
        filterPanel.add(descriptionFilterField, fgbc);
        
        fgbc.gridx = 5;
        fgbc.fill = GridBagConstraints.NONE;
        fgbc.weightx = 0;
        fgbc.insets = new Insets(8, 15, 8, 5);
        JLabel statusLabel = new JLabel("Durum:");
        statusLabel.setFont(ModernTheme.FONT_BODY);
        filterPanel.add(statusLabel, fgbc);
        
        fgbc.gridx = 6;
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.weightx = 0.15;
        fgbc.insets = new Insets(8, 5, 8, 10);
        filterPanel.add(statusFilterCombo, fgbc);
        
        fgbc.gridx = 7;
        fgbc.fill = GridBagConstraints.NONE;
        fgbc.weightx = 0;
        fgbc.insets = new Insets(8, 10, 8, 5);
        filterPanel.add(filterButton, fgbc);
        
        fgbc.gridx = 8;
        filterPanel.add(clearFilterButton, fgbc);
        
        fgbc.gridx = 9;
        filterPanel.add(refreshButton, fgbc);
        
        fgbc.gridx = 10;
        fgbc.weightx = 1.0;
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        filterPanel.add(Box.createHorizontalGlue(), fgbc);
        
        add(filterPanel, BorderLayout.NORTH);

        // Table in card
        JPanel tableCard = ModernTheme.createCardPanel();
        tableCard.setLayout(new BorderLayout());
        tableCard.add(new JScrollPane(ticketTable), BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        setupListeners();
    }

    private void setupListeners() {
        // Double-click to view ticket details
        ticketTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = ticketTable.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        Long ticketId = (Long) tableModel.getValueAt(row, 0);
                        Ticket ticket = ticketService.getTicketById(ticketId);
                        if (ticket != null) {
                            new TicketDetailFrame(ticket, currentUser).setVisible(true);
                        }
                    }
                }
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });

        clearFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idFilterField.setText("");
                descriptionFilterField.setText("");
                statusFilterCombo.setSelectedIndex(0);
                loadTickets();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allTickets = null; // Reset to force reload
                loadTickets();
            }
        });
    }

    private void loadTickets() {
        tableModel.setRowCount(0);
        allTickets = ticketService.getAllTickets();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Ticket ticket : allTickets) {
            Object[] row = {
                    ticket.getId(),
                    ticket.getTitle(),
                    ticket.getCreatedAt().format(formatter),
                    ticket.getCreatedBy(),
                    ticket.getStatus().getDisplayName()
            };
            tableModel.addRow(row);
        }
    }

    private void applyFilters() {
        tableModel.setRowCount(0);
        
        String idFilter = idFilterField.getText().trim();
        String descriptionFilter = descriptionFilterField.getText().trim().toLowerCase();
        TicketStatus statusFilter = (TicketStatus) statusFilterCombo.getSelectedItem();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // Load tickets if not already loaded
        if (allTickets == null) {
            allTickets = ticketService.getAllTickets();
        }
        
        List<Ticket> filteredTickets = allTickets;
        
        for (Ticket ticket : filteredTickets) {
            // ID filter
            if (!idFilter.isEmpty()) {
                try {
                    Long filterId = Long.parseLong(idFilter);
                    if (!ticket.getId().equals(filterId)) {
                        continue;
                    }
                } catch (NumberFormatException e) {
                    // Invalid ID, skip this ticket
                    continue;
                }
            }
            
            // Description filter
            if (!descriptionFilter.isEmpty()) {
                String description = ticket.getDescription();
                if (description == null || !description.toLowerCase().contains(descriptionFilter)) {
                    continue;
                }
            }
            
            // Status filter
            if (statusFilter != null && ticket.getStatus() != statusFilter) {
                continue;
            }
            
            // Add row if passed all filters
            Object[] row = {
                    ticket.getId(),
                    ticket.getTitle(),
                    ticket.getCreatedAt().format(formatter),
                    ticket.getCreatedBy(),
                    ticket.getStatus().getDisplayName()
            };
            tableModel.addRow(row);
        }
    }
}

