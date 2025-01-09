package com.vixr.log_analyzer.repository;

import com.vixr.log_analyzer.parser.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface LogRepository extends JpaRepository<LogEntry, Long> {

    @Query(value = "SELECT timestamp FROM logs ORDER BY id DESC LIMIT 1", nativeQuery = true)
    LocalDateTime findLastLogTimestamp();
}
