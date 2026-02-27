package com.Document.DocAudit.service;

import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.entity.UserEntity;
import com.Document.DocAudit.enums.DocumentStatus;
import com.Document.DocAudit.repository.DocumentRepository;
import com.Document.DocAudit.repository.UserRepository;
import com.Document.DocAudit.securityConfig.SecurityConfig;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    public Document CreateDocument(String title,String Content){
       String currentUser = SecurityConfig.GetCurrentUser();
       if(currentUser == null){
           throw new IllegalArgumentException("You need to be logged in first");
       }
       if(title == null  || Content == null || title.isEmpty() || Content.isEmpty()){
           throw new IllegalArgumentException("Please Enter All Fields");
       }

        UserEntity creator = userRepository.findByname(currentUser).orElseThrow(() -> new RuntimeException("User not found"));

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
        UserEntity user = userRepository.findByname(currentUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Load document
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        if (doc.getCreatedBy().equals(user)) {
            doc.setContent(content);
            doc.setUpdatedAt(LocalDateTime.now()); // or use @LastModifiedDate
            return documentRepository.save(doc);
        } else {
            throw new AccessDeniedException("You don't have permission to edit this document");
        }
    }

    private void DeleteDocument(Long id) {
        Document doc = documentRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Document not found"));
        doc.setStatus(DocumentStatus.DELETED);
        doc.setUpdatedAt(LocalDateTime.now());
        documentRepository.save(doc);
    }


}
