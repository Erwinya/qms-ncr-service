package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateNcrStatusRequest(
        @NotNull(message = "status is required")
        NcrStatus status,

        @Size(max = 2000, message = "containmentAction must be at most 2000 characters")
        String containmentAction,

        @Size(max = 2000, message = "note must be at most 2000 characters")
        String note
) {
}
