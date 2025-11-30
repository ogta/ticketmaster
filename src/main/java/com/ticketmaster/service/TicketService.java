package com.ticketmaster.service;

import com.ticketmaster.dao.TicketDAO;
import com.ticketmaster.model.Ticket;
import com.ticketmaster.model.TicketStatus;
import com.ticketmaster.model.UserProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketService {
    private TicketDAO ticketDAO;
    private static final String TICKET_IMAGES_BASE_DIR = "./data/tickets";

    public TicketService() {
        this.ticketDAO = new TicketDAO();
    }

    public void createTicket(Ticket ticket) {
        ticket.setStatus(TicketStatus.YENI_KAYIT);
        if (ticket.getCreatedAt() == null) {
            ticket.setCreatedAt(LocalDateTime.now());
        }
        
        // Save ticket first to get the ID
        ticketDAO.save(ticket);
        
        // Copy images to data/tickets/{ticketId}/ directory
        if (ticket.getId() != null && ticket.getImagePaths() != null && !ticket.getImagePaths().isEmpty()) {
            List<String> newImagePaths = copyImagesToTicketFolder(ticket.getId(), ticket.getImagePaths());
            ticket.setImagePaths(newImagePaths);
            // Update image paths in database
            ticketDAO.updateTicketImages(ticket.getId(), newImagePaths);
        }
    }
    
    private List<String> copyImagesToTicketFolder(Long ticketId, List<String> originalPaths) {
        List<String> newPaths = new ArrayList<>();
        String ticketFolder = TICKET_IMAGES_BASE_DIR + File.separator + ticketId;
        
        try {
            Path ticketDir = Paths.get(ticketFolder);
            Files.createDirectories(ticketDir);
            
            for (int i = 0; i < originalPaths.size(); i++) {
                String originalPath = originalPaths.get(i);
                File originalFile = new File(originalPath);
                
                if (originalFile.exists()) {
                    String fileName = originalFile.getName();
                    // Preserve file extension
                    String extension = "";
                    int lastDot = fileName.lastIndexOf('.');
                    if (lastDot > 0) {
                        extension = fileName.substring(lastDot);
                        fileName = fileName.substring(0, lastDot);
                    }
                    
                    // Create unique filename if needed (add index if multiple files with same name)
                    String newFileName = fileName + "_" + (i + 1) + extension;
                    Path destinationPath = ticketDir.resolve(newFileName);
                    
                    // Copy file
                    Files.copy(originalFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                    
                    // Store relative path for database
                    String relativePath = "data/tickets/" + ticketId + "/" + newFileName;
                    newPaths.add(relativePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error copying images to ticket folder: " + e.getMessage());
            e.printStackTrace();
        }
        
        return newPaths;
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.findAll();
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketDAO.findById(ticketId);
    }

    public boolean canChangeStatus(UserProfile profile, TicketStatus currentStatus, TicketStatus newStatus) {
        if (profile == UserProfile.FIRMA) {
            return newStatus == TicketStatus.INCELENIYOR ||
                   newStatus == TicketStatus.DUZENLEME_YAPILIYOR ||
                   newStatus == TicketStatus.KONTROLE_GONDERILDI;
        } else if (profile == UserProfile.BANKA) {
            return newStatus == TicketStatus.YENI_KAYIT ||
                   newStatus == TicketStatus.TAMAMLANDI;
        }
        return false;
    }

    public void updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        ticketDAO.updateStatus(ticketId, newStatus);
    }

    public void updateTicketStatusAndResult(Long ticketId, String updatedBy, TicketStatus newStatus, String resultDescription) {
        ticketDAO.updateStatusAndResultDescription(ticketId, updatedBy, newStatus, resultDescription);
    }

    public List<com.ticketmaster.model.TicketUpdate> getTicketUpdates(Long ticketId) {
        return ticketDAO.getTicketUpdates(ticketId);
    }

    public long getTotalTicketCount() {
        return ticketDAO.getTotalTicketCount();
    }

    public long getCompletedTicketCount() {
        return ticketDAO.getCompletedTicketCount();
    }
}

