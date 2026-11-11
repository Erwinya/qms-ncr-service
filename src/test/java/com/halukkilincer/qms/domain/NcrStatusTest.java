package com.halukkilincer.qms.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NcrStatusTest {

    @Test
    void allowsExpectedTransitions() {
        assertThat(NcrStatus.OPEN.canTransitionTo(NcrStatus.UNDER_REVIEW)).isTrue();
        assertThat(NcrStatus.OPEN.canTransitionTo(NcrStatus.CANCELLED)).isTrue();
        assertThat(NcrStatus.OPEN.canTransitionTo(NcrStatus.CLOSED)).isFalse();
        assertThat(NcrStatus.OPEN.canTransitionTo(NcrStatus.CONTAINED)).isFalse();

        assertThat(NcrStatus.UNDER_REVIEW.canTransitionTo(NcrStatus.CONTAINED)).isTrue();
        assertThat(NcrStatus.UNDER_REVIEW.canTransitionTo(NcrStatus.CLOSED)).isTrue();
        assertThat(NcrStatus.UNDER_REVIEW.canTransitionTo(NcrStatus.CANCELLED)).isTrue();
        assertThat(NcrStatus.UNDER_REVIEW.canTransitionTo(NcrStatus.OPEN)).isFalse();

        assertThat(NcrStatus.CONTAINED.canTransitionTo(NcrStatus.CLOSED)).isTrue();
        assertThat(NcrStatus.CONTAINED.canTransitionTo(NcrStatus.UNDER_REVIEW)).isTrue();
        assertThat(NcrStatus.CONTAINED.canTransitionTo(NcrStatus.OPEN)).isFalse();

        assertThat(NcrStatus.CLOSED.canTransitionTo(NcrStatus.OPEN)).isFalse();
        assertThat(NcrStatus.CANCELLED.canTransitionTo(NcrStatus.UNDER_REVIEW)).isFalse();
    }
}
