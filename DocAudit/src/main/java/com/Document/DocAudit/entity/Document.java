package com.Document.DocAudit.entity;

import com.Document.DocAudit.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;
    @Lob
    private String Content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity CreatedBy;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
    private DocumentStatus Status;
}
