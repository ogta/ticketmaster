package com.ticketmaster.dao;

import com.ticketmaster.model.CriticalityLevel;
import com.ticketmaster.model.Ticket;
import com.ticketmaster.model.TicketStatus;
import com.ticketmaster.model.TicketUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private DatabaseManager dbManager;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TicketDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void save(Ticket ticket) {
        String sql = "INSERT INTO tickets (title, description, criticality_level, status, created_by, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ticket.getTitle());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setString(3, ticket.getCriticalityLevel().name());
            pstmt.setString(4, ticket.getStatus().name());
            pstmt.setString(5, ticket.getCreatedBy());
            pstmt.setString(6, ticket.getCreatedAt().format(formatter));
            pstmt.executeUpdate();

            // Get the generated ID using last_insert_rowid() for SQLite
            try (ResultSet rs = conn.createStatement().executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    ticket.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveTicketImages(Long ticketId, List<String> imagePaths) {
        String sql = "INSERT INTO ticket_images (ticket_id, image_path) VALUES (?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String imagePath : imagePaths) {
                pstmt.setLong(1, ticketId);
                pstmt.setString(2, imagePath);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTicketImages(Long ticketId, List<String> imagePaths) {
        // First delete existing images
        String deleteSql = "DELETE FROM ticket_images WHERE ticket_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setLong(1, ticketId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Then insert new images
        saveTicketImages(ticketId, imagePaths);
    }

    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT id, title, description, criticality_level, status, created_by, created_at, result_description " +
                     "FROM tickets ORDER BY created_at DESC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getLong("id"));
                ticket.setTitle(rs.getString("title"));
                ticket.setDescription(rs.getString("description"));
                ticket.setCriticalityLevel(CriticalityLevel.valueOf(rs.getString("criticality_level")));
                ticket.setStatus(TicketStatus.valueOf(rs.getString("status")));
                ticket.setCreatedBy(rs.getString("created_by"));
                ticket.setCreatedAt(LocalDateTime.parse(rs.getString("created_at"), formatter));
                ticket.setResultDescription(rs.getString("result_description"));
                ticket.setImagePaths(getTicketImages(ticket.getId()));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public long getTotalTicketCount() {
        String sql = "SELECT COUNT(*) FROM tickets";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getCompletedTicketCount() {
        String sql = "SELECT COUNT(*) FROM tickets WHERE status = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TicketStatus.TAMAMLANDI.name());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Ticket findById(Long ticketId) {
        String sql = "SELECT id, title, description, criticality_level, status, created_by, created_at, result_description " +
                     "FROM tickets WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getLong("id"));
                ticket.setTitle(rs.getString("title"));
                ticket.setDescription(rs.getString("description"));
                ticket.setCriticalityLevel(CriticalityLevel.valueOf(rs.getString("criticality_level")));
                ticket.setStatus(TicketStatus.valueOf(rs.getString("status")));
                ticket.setCreatedBy(rs.getString("created_by"));
                ticket.setCreatedAt(LocalDateTime.parse(rs.getString("created_at"), formatter));
                ticket.setResultDescription(rs.getString("result_description"));
                ticket.setImagePaths(getTicketImages(ticket.getId()));
                return ticket;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getTicketImages(Long ticketId) {
        List<String> imagePaths = new ArrayList<>();
        String sql = "SELECT image_path FROM ticket_images WHERE ticket_id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                imagePaths.add(rs.getString("image_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return imagePaths;
    }

    public void updateStatus(Long ticketId, TicketStatus status) {
        String sql = "UPDATE tickets SET status = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setLong(2, ticketId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatusAndResultDescription(Long ticketId, String updatedBy, TicketStatus status, String resultDescription) {
        // Update ticket status
        String updateSql = "UPDATE tickets SET status = ? WHERE id = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setString(1, status.name());
            updateStmt.setLong(2, ticketId);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Insert update record into ticket_updates table
        addTicketUpdate(ticketId, updatedBy, status, resultDescription);
    }

    public void addTicketUpdate(Long ticketId, String updatedBy, TicketStatus status, String description) {
        String sql = "INSERT INTO ticket_updates (ticket_id, updated_at, updated_by, status, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String timestamp = java.time.LocalDateTime.now().format(formatter);
            pstmt.setLong(1, ticketId);
            pstmt.setString(2, timestamp);
            pstmt.setString(3, updatedBy);
            pstmt.setString(4, status.name());
            pstmt.setString(5, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TicketUpdate> getTicketUpdates(Long ticketId) {
        List<TicketUpdate> updates = new ArrayList<>();
        String sql = "SELECT id, ticket_id, updated_at, updated_by, status, description FROM ticket_updates WHERE ticket_id = ? ORDER BY updated_at DESC";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ticketId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TicketUpdate update = new TicketUpdate();
                update.setId(rs.getLong("id"));
                update.setTicketId(rs.getLong("ticket_id"));
                update.setUpdatedAt(LocalDateTime.parse(rs.getString("updated_at"), formatter));
                update.setUpdatedBy(rs.getString("updated_by"));
                update.setStatus(TicketStatus.valueOf(rs.getString("status")));
                update.setDescription(rs.getString("description"));
                updates.add(update);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updates;
    }
}

