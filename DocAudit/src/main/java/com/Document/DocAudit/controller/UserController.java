package com.Document.DocAudit.controller;

import com.Document.DocAudit.dto.UserResponse;
import com.Document.DocAudit.entity.UserEntity;
import com.Document.DocAudit.securityConfig.CustomUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class UserController {

    @GetMapping("/user")
    public UserResponse GetUser(@AuthenticationPrincipal CustomUserPrincipal principal){
        UserEntity user = principal.getUser();
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPictureUrl(),
                user.getProvider(),
                user.getStatus()

        );
    }
}
