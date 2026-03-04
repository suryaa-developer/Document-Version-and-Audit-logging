package com.Document.DocAudit.dto;

import com.Document.DocAudit.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String pictureUrl;
    private String provider;
    private UserStatus status;
    private String token;
}
