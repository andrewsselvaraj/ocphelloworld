package com.example.loanmanagement.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {

    private final AtomicLong nextId = new AtomicLong();
    private final ConcurrentSkipListMap<Long, LoanApplication> applications = new ConcurrentSkipListMap<>();

    @PostMapping
    public ResponseEntity<LoanApplication> submit(@Valid @RequestBody CreateLoanApplicationRequest request) {
        long id = nextId.incrementAndGet();
        LoanApplication application = new LoanApplication(
                id,
                request.applicantName().trim(),
                request.loanType().trim(),
                request.requestedAmount(),
                "PENDING",
                Instant.now()
        );
        applications.put(id, application);

        return ResponseEntity.created(URI.create("/api/loan-applications/" + id)).body(application);
    }

    @GetMapping
    public List<LoanApplication> getAll() {
        return List.copyOf(applications.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getById(@PathVariable long id) {
        return Optional.ofNullable(applications.get(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public record CreateLoanApplicationRequest(
            @NotBlank String applicantName,
            @NotBlank String loanType,
            @NotNull
            @Positive BigDecimal requestedAmount
    ) {
    }

    public record LoanApplication(
            long id,
            String applicantName,
            String loanType,
            BigDecimal requestedAmount,
            String status,
            Instant submittedAt
    ) {
    }
}
