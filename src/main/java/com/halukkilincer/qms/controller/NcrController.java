package com.halukkilincer.qms.controller;

import com.halukkilincer.qms.domain.NcrStatus;
import com.halukkilincer.qms.dto.CreateNcrRequest;
import com.halukkilincer.qms.dto.NcrResponse;
import com.halukkilincer.qms.dto.UpdateNcrRequest;
import com.halukkilincer.qms.dto.UpdateNcrStatusRequest;
import com.halukkilincer.qms.response.ApiResponse;
import com.halukkilincer.qms.service.NcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ncrs")
@Tag(name = "Nonconformance Reports", description = "Create and manage NCRs")
public class NcrController {

    private final NcrService ncrService;

    public NcrController(NcrService ncrService) {
        this.ncrService = ncrService;
    }

    @PostMapping
    @Operation(summary = "Create a new NCR")
    public ResponseEntity<ApiResponse<NcrResponse>> create(@Valid @RequestBody CreateNcrRequest request) {
        NcrResponse created = ncrService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created, 201));
    }

    @GetMapping
    @Operation(summary = "List NCRs, optionally filtered by status")
    public ResponseEntity<ApiResponse<List<NcrResponse>>> list(
            @RequestParam(required = false) NcrStatus status
    ) {
        return ResponseEntity.ok(ApiResponse.success(ncrService.list(status), 200));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get NCR by id")
    public ResponseEntity<ApiResponse<NcrResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(ncrService.getById(id), 200));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update NCR details (not allowed when closed/cancelled)")
    public ResponseEntity<ApiResponse<NcrResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNcrRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(ncrService.update(id, request), 200));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Transition NCR status along the allowed workflow")
    public ResponseEntity<ApiResponse<NcrResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNcrStatusRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(ncrService.updateStatus(id, request), 200));
    }
}
