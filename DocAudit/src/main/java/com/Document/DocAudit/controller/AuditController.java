package com.Document.DocAudit.controller;

import com.Document.DocAudit.entity.AuditLog;
import com.Document.DocAudit.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuditController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/version/{id}")
    public Page<AuditLog> GetVersionByDocId(@RequestParam int page , @RequestParam int size, @PathVariable Long id){
        return auditLogService.getDocumentVersions(page,size,id);
    }
}
