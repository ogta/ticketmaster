package com.ticketmaster.model;

public enum TicketStatus {
    YENI_KAYIT("Yeni Kayıt"),
    INCELENIYOR("İnceleniyor"),
    DUZENLEME_YAPILIYOR("Düzenleme Yapılıyor"),
    KONTROLE_GONDERILDI("Kontrole Gönderildi"),
    TAMAMLANDI("Tamamlandı");

    private final String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TicketStatus fromString(String status) {
        for (TicketStatus ts : TicketStatus.values()) {
            if (ts.name().equals(status) || ts.displayName.equals(status)) {
                return ts;
            }
        }
        return null;
    }
}

