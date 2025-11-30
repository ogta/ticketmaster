package com.ticketmaster.model;

public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private UserProfile profile;
    private boolean passwordChanged;

    public User() {
    }

    public User(String username, String passwordHash, UserProfile profile, boolean passwordChanged) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.profile = profile;
        this.passwordChanged = passwordChanged;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public boolean isPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(boolean passwordChanged) {
        this.passwordChanged = passwordChanged;
    }
}

