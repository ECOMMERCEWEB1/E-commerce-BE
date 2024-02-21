package com.webproject.ecommerce.enums;

public enum Brand {
    APPLE("Apple"),
    DELL("Dell"),
    HP("HP"),
    LENOVO("Lenovo"),
    ASUS("Asus"),
    MICROSOFT("Microsoft"),
    SAMSUNG("Samsung"),
    LOGITECH("Logitech"),
    CANON("Canon"),
    EPSON("Epson"),
    SONY("Sony"),
    TOSHIBA("Toshiba"),
    ACER("Acer"),
    LG("LG"),
    GOOGLE("Google"),
    RAZER("Razer"),
    MICROSOFT_XBOX("Microsoft Xbox"),
    MICROSOFT_SURFACE("Microsoft Surface"),
    NVIDIA("NVIDIA"),
    INTEL("Intel");

    private final String displayName;

    Brand(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

