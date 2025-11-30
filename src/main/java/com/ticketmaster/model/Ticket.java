package com.ticketmaster.model;

import java.time.LocalDateTime;
import java.util.List;

public class Ticket {
    private Long id;
    private String title;
    private String description;
    private CriticalityLevel criticalityLevel;
    private TicketStatus status;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<String> imagePaths;
    private String resultDescription;

    public Ticket() {
    }

    public Ticket(String title, String description, CriticalityLevel criticalityLevel, 
                  TicketStatus status, String createdBy) {
        this.title = title;
        this.description = description;
        this.criticalityLevel = criticalityLevel;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CriticalityLevel getCriticalityLevel() {
        return criticalityLevel;
    }

    public void setCriticalityLevel(CriticalityLevel criticalityLevel) {
        this.criticalityLevel = criticalityLevel;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }
}

