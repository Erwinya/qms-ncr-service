package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrSeverity;
import jakarta.validation.constraints.Size;

public record UpdateNcrRequest(
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @Size(max = 4000, message = "description must be at most 4000 characters")
        String description,

        NcrSeverity severity,

        @Size(max = 100, message = "lotNumber must be at most 100 characters")
        String lotNumber,

        @Size(max = 100, message = "partNumber must be at most 100 characters")
        String partNumber,

        @Size(max = 2000, message = "containmentAction must be at most 2000 characters")
        String containmentAction,

        @Size(max = 2000, message = "dispositionNotes must be at most 2000 characters")
        String dispositionNotes
) {
}
