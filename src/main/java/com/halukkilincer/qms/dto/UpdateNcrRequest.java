package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrSeverity;
import jakarta.validation.constraints.Size;

public record UpdateNcrRequest(
        @Size(max = 200) String title,
        @Size(max = 4000) String description,
        NcrSeverity severity,
        @Size(max = 100) String lotNumber,
        @Size(max = 100) String partNumber,
        @Size(max = 2000) String containmentAction,
        @Size(max = 2000) String dispositionNotes
) {
}
