package com.halukkilincer.qms.repository;

import com.halukkilincer.qms.domain.NcrStatus;
import com.halukkilincer.qms.domain.Nonconformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NonconformanceRepository extends JpaRepository<Nonconformance, Long> {

    List<Nonconformance> findByStatusOrderByCreatedAtDesc(NcrStatus status);

    List<Nonconformance> findAllByOrderByCreatedAtDesc();

    long countByNcrNumberStartingWith(String prefix);
}
