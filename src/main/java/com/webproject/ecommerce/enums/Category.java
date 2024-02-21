package com.webproject.ecommerce.enums;

public enum Category {
    COMPUTERS("Computers"),
    PERIPHERALS("Peripherals"),
    ACCESSORIES("Accessories");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
