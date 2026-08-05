package com.halukkilincer.qms.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NcrStatusTest {

    @Test
    void allowsExpectedTransitions() {
        assertThat(NcrStatus.OPEN.canTransitionTo(NcrStatus.UNDER_REVIEW)).isTrue();
        assertThat(NcrStatus.OPEN.canTransitionTo(NcrStatus.CLOSED)).isFalse();
        assertThat(NcrStatus.CLOSED.canTransitionTo(NcrStatus.OPEN)).isFalse();
    }
}
