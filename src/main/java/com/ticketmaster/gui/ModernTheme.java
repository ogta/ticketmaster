package com.ticketmaster.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ModernTheme {
    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Modern Blue
    public static final Color PRIMARY_DARK = new Color(0, 76, 153);
    public static final Color PRIMARY_LIGHT = new Color(51, 153, 255);
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125); // Gray
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color INFO_COLOR = new Color(23, 162, 184);
    
    public static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    public static final Color CARD_COLOR = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(222, 226, 230);
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Borders
    public static final Border CARD_BORDER = new CompoundBorder(
        new LineBorder(BORDER_COLOR, 1, true),
        new EmptyBorder(15, 15, 15, 15)
    );
    
    public static final Border INPUT_BORDER = new CompoundBorder(
        new LineBorder(BORDER_COLOR, 1),
        new EmptyBorder(8, 12, 8, 12)
    );
    
    public static void applyModernButtonStyle(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Calculate button width based on text length
        String text = button.getText();
        int minWidth = 120;
        int calculatedWidth = Math.max(minWidth, text.length() * 8 + 48); // 8 pixels per char + padding
        button.setPreferredSize(new Dimension(calculatedWidth, 38));
        button.setMinimumSize(new Dimension(calculatedWidth, 38));
        button.setBorder(new EmptyBorder(10, 24, 10, 24));
        
        if (button.getText().contains("Giriş") || button.getText().contains("Kaydet") || 
            button.getText().contains("Güncelle") || button.getText().contains("Filtrele") ||
            button.getText().contains("Değiştir") || button.getText().contains("Yenile") ||
            button.getText().contains("Resim Ekle")) {
            // Primary button - solid blue
            button.setBackground(PRIMARY_COLOR);
            button.setForeground(Color.WHITE);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_DARK);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_COLOR);
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(0, 51, 102));
                }
            });
        } else if (button.getText().contains("Temizle") || button.getText().contains("Kaldır") || 
                   button.getText().contains("İptal")) {
            // Secondary button - light gray
            button.setBackground(new Color(248, 249, 250));
            button.setForeground(TEXT_PRIMARY);
            button.setBorder(new EmptyBorder(10, 24, 10, 24));
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(233, 236, 239));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(248, 249, 250));
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(222, 226, 230));
                }
            });
        } else {
            // Default button - white with subtle border
            button.setBackground(Color.WHITE);
            button.setForeground(PRIMARY_COLOR);
            button.setBorder(new CompoundBorder(
                new LineBorder(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 30), 1),
                new EmptyBorder(10, 24, 10, 24)
            ));
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(240, 248, 255));
                    button.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY_LIGHT, 1),
                        new EmptyBorder(10, 24, 10, 24)
                    ));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(new CompoundBorder(
                        new LineBorder(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 30), 1),
                        new EmptyBorder(10, 24, 10, 24)
                    ));
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    button.setBackground(PRIMARY_LIGHT);
                    button.setForeground(Color.WHITE);
                }
            });
        }
    }
    
    public static void applyModernTextFieldStyle(JTextField field) {
        field.setFont(FONT_BODY);
        field.setBorder(INPUT_BORDER);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
    }
    
    public static void applyModernPasswordFieldStyle(JPasswordField field) {
        field.setFont(FONT_BODY);
        field.setBorder(INPUT_BORDER);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
    }
    
    public static void applyModernTextAreaStyle(JTextArea area) {
        area.setFont(FONT_BODY);
        area.setBorder(INPUT_BORDER);
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_PRIMARY);
    }
    
    public static void applyModernComboBoxStyle(JComboBox<?> combo) {
        combo.setFont(FONT_BODY);
        combo.setBorder(INPUT_BORDER);
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_PRIMARY);
    }
    
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(CARD_BORDER);
        return panel;
    }
    
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(40);
        table.setSelectionBackground(new Color(240, 248, 255));
        table.setSelectionForeground(PRIMARY_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(CARD_COLOR);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_SUBHEADING);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        header.setOpaque(true);
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(PRIMARY_COLOR);
                c.setForeground(Color.WHITE);
                ((JLabel) c).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return c;
            }
        });
    }
}

