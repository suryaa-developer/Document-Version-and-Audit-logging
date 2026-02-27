package com.Document.DocAudit.securityConfig;

import com.Document.DocAudit.service.UserOAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            UserOAuthService customOAuth2UserService) {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.oidcUserService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/login/user", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                );

        return http.build();
    }

    public static String GetCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName():null;
    }
}

