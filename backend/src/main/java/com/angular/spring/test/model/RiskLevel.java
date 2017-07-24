package com.angular.spring.test.model;

public enum RiskLevel{
    HIGH_RISK("HIGH_RISK"),
    MEDIUM_RISK("MEDIUM_RISK"),
    LOW_RISK("LOW_RISK"),
    NO_RISK("NO_RISK");

    private String value;

    private RiskLevel(String riskLevel) {
        this.value = riskLevel;
    }

    public String value() {
        return this.value;
    }
}