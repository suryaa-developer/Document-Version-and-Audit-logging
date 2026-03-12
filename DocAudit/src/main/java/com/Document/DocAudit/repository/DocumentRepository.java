package com.Document.DocAudit.repository;

import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByStatus(DocumentStatus status);
}
