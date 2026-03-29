package com.Document.DocAudit.service;

import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.entity.UserEntity;
import com.Document.DocAudit.enums.DocumentAction;
import com.Document.DocAudit.enums.DocumentStatus;
import com.Document.DocAudit.repository.DocumentRepository;
import com.Document.DocAudit.repository.UserRepository;
import com.Document.DocAudit.securityConfig.SecurityConfig;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public Document CreateDocument(String title,String Content){
       String currentUser = SecurityConfig.GetCurrentUser();
       if(currentUser == null){
           throw new IllegalArgumentException("You need to be logged in first");
       }

        // Change findByname to findByEmail
        UserEntity creator = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + currentUser));

       Document newDoc = new Document();
       newDoc.setTitle(title);
       newDoc.setContent(Content);
       newDoc.setCreatedBy(creator);
       newDoc.setCreatedAt(LocalDateTime.now());
       newDoc.setUpdatedAt(LocalDateTime.now());
       newDoc.setStatus(DocumentStatus.ACTIVE);

       documentRepository.save(newDoc);
       return newDoc;
    }

    @Transactional
    public Document updateDocument(Long id, String content) {
        // Get current authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to edit a document");
        }

        String currentUser = auth.getName();
        UserEntity user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Load document
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        String PreviousContent = doc.getContent();
        if (doc.getCreatedBy().equals(user)) {
            doc.setContent(content);
            doc.setUpdatedAt(LocalDateTime.now()); // or use @LastModifiedDate
            documentRepository.save(doc);
            auditLogService.CreateDocumentLog(id, DocumentAction.UPDATED,user,PreviousContent,content);
            return doc;
        } else {
            throw new AccessDeniedException("You don't have permission to edit this document");
        }
    }

    @Transactional
    public void DeleteDocument(Long id) {
        Document doc = documentRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Document not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to edit a document");
        }

        String currentUser = auth.getName();
        UserEntity user = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (doc.getCreatedBy().equals(user)) {
            doc.setStatus(DocumentStatus.DELETED);
            doc.setUpdatedAt(LocalDateTime.now());
            documentRepository.save(doc);
        }else{
            throw new AccessDeniedException("You don't have permission to delete this document");
        }
    }

    // Get all active documents
    public Page<Document> getActiveDocuments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return documentRepository.findByStatus(DocumentStatus.ACTIVE,pageable);
    }

    // Get active document by ID
    public Optional<Document> getActiveDocumentById(Long id) {
        return documentRepository.findById(id)
                .filter(doc -> DocumentStatus.ACTIVE.equals(doc.getStatus()));
    }


}
