package com.halukkilincer.qms.domain;

import java.util.EnumSet;
import java.util.Set;

public enum NcrStatus {
    OPEN,
    UNDER_REVIEW,
    CONTAINED,
    CLOSED,
    CANCELLED;

    public boolean canTransitionTo(NcrStatus next) {
        return allowedTransitions().contains(next);
    }

    private Set<NcrStatus> allowedTransitions() {
        return switch (this) {
            case OPEN -> EnumSet.of(UNDER_REVIEW, CANCELLED);
            case UNDER_REVIEW -> EnumSet.of(CONTAINED, CLOSED, CANCELLED);
            case CONTAINED -> EnumSet.of(CLOSED, UNDER_REVIEW);
            case CLOSED, CANCELLED -> EnumSet.noneOf(NcrStatus.class);
        };
    }
}
