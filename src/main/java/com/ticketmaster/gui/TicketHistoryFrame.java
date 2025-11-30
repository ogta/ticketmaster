package com.ticketmaster.gui;

import com.ticketmaster.model.Ticket;
import com.ticketmaster.model.TicketUpdate;
import com.ticketmaster.model.User;
import com.ticketmaster.service.TicketService;
import com.ticketmaster.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketHistoryFrame extends JFrame {
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private Ticket ticket;
    private UserService userService;
    private TicketService ticketService;

    public TicketHistoryFrame(Ticket ticket) {
        this.ticket = ticket;
        this.userService = new UserService();
        this.ticketService = new TicketService();
        initializeComponents();
        setupLayout();
        loadHistory();
    }

    private void initializeComponents() {
        setTitle("Ticket Güncelleme Geçmişi - " + ticket.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);

        String[] columnNames = {"Tarih/Saat", "Kullanıcı", "Durum", "Açıklama"};
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(50);
        historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        historyTable.setForeground(ModernTheme.TEXT_PRIMARY);
        historyTable.setFont(ModernTheme.FONT_BODY);
        ModernTheme.styleTable(historyTable);

        // Set column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(0).setMinWidth(180);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(1).setMinWidth(150);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(2).setMinWidth(150);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(500);
        historyTable.getColumnModel().getColumn(3).setMinWidth(300);

        // Custom renderer for all columns
        javax.swing.table.DefaultTableCellRenderer defaultRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(ModernTheme.FONT_BODY);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(ModernTheme.TEXT_PRIMARY);
                }
                return c;
            }
        };

        for (int i = 0; i < 3; i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(defaultRenderer);
        }

        // Word wrap for description column
        historyTable.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea(value != null ? value.toString() : "");
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setOpaque(true);
                textArea.setFont(ModernTheme.FONT_BODY);
                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                    textArea.setForeground(table.getSelectionForeground());
                } else {
                    textArea.setBackground(table.getBackground());
                    textArea.setForeground(ModernTheme.TEXT_PRIMARY);
                }
                return textArea;
            }
        });

        // Header styling
        JTableHeader header = historyTable.getTableHeader();
        header.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
        header.setVisible(true);
        header.setReorderingAllowed(false);
        header.setBackground(ModernTheme.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(ModernTheme.FONT_SUBHEADING);
        header.setOpaque(true);

        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(ModernTheme.PRIMARY_COLOR);
                label.setForeground(Color.WHITE);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setFont(ModernTheme.FONT_SUBHEADING);
                label.setOpaque(true);
                return label;
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(ModernTheme.BACKGROUND_COLOR);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(ModernTheme.PRIMARY_COLOR);
        titlePanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Ticket Güncelleme Geçmişi: " + ticket.getTitle());
        titleLabel.setFont(ModernTheme.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JButton closeButton = new JButton("Kapat");
        ModernTheme.applyModernButtonStyle(closeButton);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        titlePanel.add(closeButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = ModernTheme.createCardPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void loadHistory() {
        historyTableModel.setRowCount(0);
        List<TicketUpdate> updates = ticketService.getTicketUpdates(ticket.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (TicketUpdate update : updates) {
            String timestamp = update.getUpdatedAt().format(formatter);
            String username = update.getUpdatedBy();
            String status = update.getStatus().getDisplayName();
            String description = update.getDescription() != null ? update.getDescription() : "";
            
            // Get user info to show ID and name
            User user = userService.findByUsername(username);
            String userDisplay = username;
            if (user != null && user.getId() != null) {
                userDisplay = user.getId() + " - " + username;
            }
            
            historyTableModel.addRow(new Object[]{timestamp, userDisplay, status, description});
        }
    }
}

