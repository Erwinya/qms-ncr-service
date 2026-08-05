package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateNcrStatusRequest(
        @NotNull NcrStatus status,
        @Size(max = 2000) String note
) {
}
