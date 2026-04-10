package com.Document.DocAudit.repository;

import com.Document.DocAudit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog,Long> {
    Page<AuditLog> findAllByDocumentId(Pageable pageable,Long documentId);
}
