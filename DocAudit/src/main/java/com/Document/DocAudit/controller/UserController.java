package com.Document.DocAudit.controller;

import com.Document.DocAudit.dto.ApiResponse;
import com.Document.DocAudit.dto.UserResponse;
import com.Document.DocAudit.entity.UserEntity;
import com.Document.DocAudit.securityConfig.CustomUserPrincipal;
import com.Document.DocAudit.securityConfig.JwtUtils;
import com.Document.DocAudit.securityConfig.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/usercreate")
    public ResponseEntity<ApiResponse<UserResponse>> GetUser(@AuthenticationPrincipal CustomUserPrincipal principal){
        UserEntity user = principal.getUser();
        // 1. Generate the token specifically for this logged-in user
        String jwtToken = jwtUtils.generateToken(user.getName());
        UserResponse userResponse= new      UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPictureUrl(),
                user.getProvider(),
                user.getStatus(),
                jwtToken
        );
        ApiResponse<UserResponse> response =
                new ApiResponse<>(true, "User fetched successfully", userResponse);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<String>> LoginSuccess(){
        String Username = SecurityConfig.GetCurrentUser();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Login successful", Username)
        );
    }
}
