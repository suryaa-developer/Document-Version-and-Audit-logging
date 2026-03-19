package com.Document.DocAudit.repository;

import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.enums.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);
}
