package com.halukkilincer.qms.service;

import com.halukkilincer.qms.domain.NcrStatus;
import com.halukkilincer.qms.domain.Nonconformance;
import com.halukkilincer.qms.dto.CreateNcrRequest;
import com.halukkilincer.qms.dto.NcrResponse;
import com.halukkilincer.qms.dto.UpdateNcrRequest;
import com.halukkilincer.qms.dto.UpdateNcrStatusRequest;
import com.halukkilincer.qms.exception.BadRequestException;
import com.halukkilincer.qms.exception.NotFoundException;
import com.halukkilincer.qms.repository.NonconformanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
public class NcrService {

    private final NonconformanceRepository repository;

    public NcrService(NonconformanceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public NcrResponse create(CreateNcrRequest request) {
        Nonconformance ncr = new Nonconformance();
        ncr.setNcrNumber(nextNcrNumber());
        ncr.setTitle(request.title().trim());
        ncr.setDescription(request.description().trim());
        ncr.setSeverity(request.severity());
        ncr.setStatus(NcrStatus.OPEN);
        ncr.setLotNumber(trimToNull(request.lotNumber()));
        ncr.setPartNumber(trimToNull(request.partNumber()));
        ncr.setReportedBy(request.reportedBy().trim());
        return NcrResponse.from(repository.save(ncr));
    }

    @Transactional(readOnly = true)
    public NcrResponse getById(Long id) {
        return NcrResponse.from(find(id));
    }

    @Transactional(readOnly = true)
    public List<NcrResponse> list(NcrStatus status) {
        List<Nonconformance> items = status == null
                ? repository.findAllByOrderByCreatedAtDesc()
                : repository.findByStatusOrderByCreatedAtDesc(status);
        return items.stream().map(NcrResponse::from).toList();
    }

    @Transactional
    public NcrResponse update(Long id, UpdateNcrRequest request) {
        Nonconformance ncr = find(id);
        if (ncr.getStatus() == NcrStatus.CLOSED || ncr.getStatus() == NcrStatus.CANCELLED) {
            throw new BadRequestException("Closed or cancelled NCRs cannot be edited");
        }
        if (request.title() != null && !request.title().isBlank()) {
            ncr.setTitle(request.title().trim());
        }
        if (request.description() != null && !request.description().isBlank()) {
            ncr.setDescription(request.description().trim());
        }
        if (request.severity() != null) {
            ncr.setSeverity(request.severity());
        }
        if (request.lotNumber() != null) {
            ncr.setLotNumber(trimToNull(request.lotNumber()));
        }
        if (request.partNumber() != null) {
            ncr.setPartNumber(trimToNull(request.partNumber()));
        }
        if (request.containmentAction() != null) {
            ncr.setContainmentAction(trimToNull(request.containmentAction()));
        }
        if (request.dispositionNotes() != null) {
            ncr.setDispositionNotes(trimToNull(request.dispositionNotes()));
        }
        return NcrResponse.from(repository.save(ncr));
    }

    @Transactional
    public NcrResponse updateStatus(Long id, UpdateNcrStatusRequest request) {
        Nonconformance ncr = find(id);
        NcrStatus next = request.status();
        if (!ncr.getStatus().canTransitionTo(next)) {
            throw new BadRequestException(
                    "Invalid status transition: " + ncr.getStatus() + " -> " + next
            );
        }
        ncr.setStatus(next);
        if (request.note() != null && !request.note().isBlank()) {
            String existing = ncr.getDispositionNotes();
            String addition = "[" + next + "] " + request.note().trim();
            ncr.setDispositionNotes(existing == null || existing.isBlank()
                    ? addition
                    : existing + "\n" + addition);
        }
        return NcrResponse.from(repository.save(ncr));
    }

    private Nonconformance find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("NCR not found: " + id));
    }

    private String nextNcrNumber() {
        String prefix = "NCR-" + Year.now().getValue() + "-";
        long count = repository.countByNcrNumberStartingWith(prefix);
        return prefix + String.format("%04d", count + 1);
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
