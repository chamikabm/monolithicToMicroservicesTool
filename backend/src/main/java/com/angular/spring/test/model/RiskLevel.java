package com.angular.spring.test.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    private static final List<RiskLevel> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static RiskLevel getRiskForService()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}