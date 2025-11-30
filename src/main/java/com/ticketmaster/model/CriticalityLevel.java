package com.ticketmaster.model;

public enum CriticalityLevel {
    DUSUK("Düşük"),
    ORTA("Orta"),
    YUKSEK("Yüksek"),
    KRITIK("Kritik");

    private final String displayName;

    CriticalityLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CriticalityLevel fromString(String level) {
        for (CriticalityLevel cl : CriticalityLevel.values()) {
            if (cl.name().equals(level) || cl.displayName.equals(level)) {
                return cl;
            }
        }
        return null;
    }
}

