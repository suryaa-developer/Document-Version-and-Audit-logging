package com.Document.DocAudit.repository;

import com.Document.DocAudit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog,Long> {
}
