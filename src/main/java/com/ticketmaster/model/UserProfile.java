package com.ticketmaster.model;

public enum UserProfile {
    FIRMA("Firma"),
    BANKA("Banka");

    private final String displayName;

    UserProfile(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserProfile fromString(String profile) {
        for (UserProfile up : UserProfile.values()) {
            if (up.name().equals(profile) || up.displayName.equals(profile)) {
                return up;
            }
        }
        return null;
    }
}

