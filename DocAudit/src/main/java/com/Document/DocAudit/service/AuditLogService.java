package com.Document.DocAudit.service;

import com.Document.DocAudit.entity.AuditLog;
import com.Document.DocAudit.entity.UserEntity;
import com.Document.DocAudit.enums.DocumentAction;
import com.Document.DocAudit.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {
    @Autowired
    AuditRepository auditRepository;

    public void CreateDocumentLog(Long docId, DocumentAction docAction, UserEntity user,
                                  String previousContent, String newContent,long currentVersion,String summary){
        AuditLog log = new  AuditLog();
        log.setDocumentId(docId);
        log.setAction(docAction);
        log.setPreviousContent(previousContent);
        log.setNewContent(newContent);
        log.setModifiedBy(user);
        log.setModifiedAt(LocalDateTime.now());
        log.setVersion(currentVersion);
        log.setChangeSummary(summary);
        auditRepository.save(log);
    }

    public Page<AuditLog> getDocumentVersions(int page, int size,long docId) {
        Pageable pageable = PageRequest.of(page, size);
        return auditRepository.findAllByDocumentId(pageable,docId);
    }


}
