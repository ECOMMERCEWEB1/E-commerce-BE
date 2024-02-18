package com.webproject.ecommerce.enums;

public enum Status {
    ACTIVE("Available"),
    OUT_OF_STOCK("Out of Stock"),
    BY_COMMAND("By command");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}