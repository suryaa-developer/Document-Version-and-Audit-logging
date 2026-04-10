package com.Document.DocAudit.entity;

import com.Document.DocAudit.enums.DocumentAction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long documentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity ModifiedBy;
    private DocumentAction Action;
    @Lob
    private String PreviousContent;
    @Lob
    private String NewContent;
    private LocalDateTime ModifiedAt;
    private long Version;
    private String changeSummary;
}
