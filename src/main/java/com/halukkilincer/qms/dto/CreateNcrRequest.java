package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateNcrRequest(
        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @NotBlank(message = "description is required")
        @Size(max = 4000, message = "description must be at most 4000 characters")
        String description,

        @NotNull(message = "severity is required")
        NcrSeverity severity,

        @Size(max = 100, message = "lotNumber must be at most 100 characters")
        String lotNumber,

        @Size(max = 100, message = "partNumber must be at most 100 characters")
        String partNumber,

        @NotBlank(message = "reportedBy is required")
        @Size(max = 120, message = "reportedBy must be at most 120 characters")
        String reportedBy
) {
}
