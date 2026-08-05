package com.halukkilincer.qms.dto;

import com.halukkilincer.qms.domain.NcrSeverity;
import com.halukkilincer.qms.domain.NcrStatus;
import com.halukkilincer.qms.domain.Nonconformance;

import java.time.Instant;

public record NcrResponse(
        Long id,
        String ncrNumber,
        String title,
        String description,
        NcrSeverity severity,
        NcrStatus status,
        String lotNumber,
        String partNumber,
        String reportedBy,
        String containmentAction,
        String dispositionNotes,
        Instant createdAt,
        Instant updatedAt
) {
    public static NcrResponse from(Nonconformance entity) {
        return new NcrResponse(
                entity.getId(),
                entity.getNcrNumber(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getSeverity(),
                entity.getStatus(),
                entity.getLotNumber(),
                entity.getPartNumber(),
                entity.getReportedBy(),
                entity.getContainmentAction(),
                entity.getDispositionNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
