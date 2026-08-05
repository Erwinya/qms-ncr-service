package com.halukkilincer.qms.repository;

import com.halukkilincer.qms.domain.NcrStatus;
import com.halukkilincer.qms.domain.Nonconformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NonconformanceRepository extends JpaRepository<Nonconformance, Long> {

    Optional<Nonconformance> findByNcrNumber(String ncrNumber);

    List<Nonconformance> findByStatusOrderByCreatedAtDesc(NcrStatus status);

    List<Nonconformance> findAllByOrderByCreatedAtDesc();

    long countByNcrNumberStartingWith(String prefix);
}
