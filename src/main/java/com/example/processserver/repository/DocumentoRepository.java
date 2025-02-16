package com.example.processserver.repository;

import com.example.processserver.model.Document;
import com.example.processserver.model.DocumentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Document, DocumentId> {
}

