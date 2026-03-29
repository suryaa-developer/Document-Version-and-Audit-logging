package com.Document.DocAudit.controller;

import com.Document.DocAudit.dto.ApiResponse;
import com.Document.DocAudit.dto.DocumentRequest;
import com.Document.DocAudit.dto.DocumentResponse;
import com.Document.DocAudit.entity.Document;
import com.Document.DocAudit.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService docService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<DocumentResponse>> createDocument(@Valid @RequestBody DocumentRequest docRequest){
     String title = docRequest.getTitle();
     String content = docRequest.getContent();
        Document doc = docService.CreateDocument(title,content);
        DocumentResponse response = ConvertToResponse(doc);
        ApiResponse<DocumentResponse> apiResponse =
                new ApiResponse<>(true, "Document created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocument(@PathVariable  Long id,@Valid  @RequestBody DocumentRequest docRequest){
       String content = docRequest.getContent();
        Document doc = docService.updateDocument(id,content);
        DocumentResponse response = ConvertToResponse(doc);
        ApiResponse<DocumentResponse> apiResponse =
                new ApiResponse<>(true, "Document Updated successfully", response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> DeleteDocument(@PathVariable Long id){
        docService.DeleteDocument(id);
        return ResponseEntity.noContent().build();
    }
        @GetMapping("/active")
        public ResponseEntity<Page<Document>> GetAllDocument(@RequestParam int page,@RequestParam int size){
            Page<Document> doc =  docService.getActiveDocuments(page, size);
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
