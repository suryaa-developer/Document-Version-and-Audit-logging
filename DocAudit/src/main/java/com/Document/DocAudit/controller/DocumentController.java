package com.Document.DocAudit.controller;

import com.Document.DocAudit.dto.DocumentRequest;
import com.Document.DocAudit.dto.DocumentResponse;
import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService docService;

    @PostMapping("/create")
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest docRequest){
     String title = docRequest.getTitle();
     String content = docRequest.getContent();
        Document doc = docService.CreateDocument(title,content);
        DocumentResponse response = ConvertToResponse(doc);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<DocumentResponse> updateDocument(@PathVariable  Long id,@Valid  @RequestBody DocumentRequest docRequest){
       String content = docRequest.getContent();
        Document doc = docService.updateDocument(id,content);
        DocumentResponse response = ConvertToResponse(doc);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> DeleteDocument(@PathVariable Long id){
        docService.DeleteDocument(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/active")
    public ResponseEntity<List<Document>> GetAllDocument(){
        List<Document> doc =  docService.getActiveDocuments();
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<Document> GetActiveDocumentById(@PathVariable Long id){

        return docService.getActiveDocumentById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
