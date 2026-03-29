package com.Document.DocAudit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentRequest {
    @NotBlank(message = "Title must not be empty")
    private String title;
    @NotBlank(message = "Content must not be empty")
    @Size(max = 5000,message = "Content should not exceed 5000 characters")
    private String content;
}
