package com.Document.DocAudit.dto;

import com.Document.DocAudit.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private Long Id;
    private String Title;
    private String Content;
    private String CreatedBy;
    private String CreatedByUserId;
    private DocumentStatus Status;
    private String changeSummary;   
}
