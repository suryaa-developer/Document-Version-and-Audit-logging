package com.Document.DocAudit.controller;

import com.Document.DocAudit.dto.DocumentRequest;
import com.Document.DocAudit.dto.DocumentResponse;
import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("document")
public class DocumentController {

    @Autowired
    private DocumentService docService;

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest docRequest){
     String title = docRequest.getTitle();
     String content = docRequest.getContent();
        Document doc = docService.CreateDocument(title,content);
        DocumentResponse response = ConvertToResponse(doc);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private DocumentResponse ConvertToResponse(Document doc){
        return  new DocumentResponse(
                doc.getId(),
                doc.getTitle(),
                doc.getContent(),
                doc.getCreatedBy().getName(),
                doc.getCreatedBy().getUserId().toString(),
                doc.getStatus()
        );
    }
}
