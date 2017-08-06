package com.angular.spring.test.model;

public enum RiskLevel{

    HIGH_RISK("HIGH RISK"),
    MEDIUM_RISK("MEDIUM RISK"),
    LOW_RISK("LOW RISK"),
    NO_RISK("NO RISK");

    private String displayName;

    RiskLevel(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }

    // Optionally and/or additionally, toString.
    @Override public String toString() { return displayName; }
}