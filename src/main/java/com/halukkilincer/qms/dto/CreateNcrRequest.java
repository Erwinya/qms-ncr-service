package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateNcrRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 4000) String description,
        @NotNull NcrSeverity severity,
        @Size(max = 100) String lotNumber,
        @Size(max = 100) String partNumber,
        @NotBlank @Size(max = 120) String reportedBy
) {
}
