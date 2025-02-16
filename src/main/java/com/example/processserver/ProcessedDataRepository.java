package com.example.processserver;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedDataRepository extends JpaRepository<ProcessedData, Long> {
}
