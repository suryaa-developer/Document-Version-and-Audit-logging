package com.Document.DocAudit.securityConfig;

import com.Document.DocAudit.service.UserOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, UserOAuthService customOAuth2UserService) {
        http.csrf(AbstractHttpConfigurer::disable)
                // Change to STATELESS if you want pure JWT,
                // but since you use OAuth2 Login, keep it IF_REQUIRED
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/oauth2/**", "/login/success", "/get-my-token").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOAuth2UserService))
                        .defaultSuccessUrl("/login/success", true))
                // This is the most important line:
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public static String GetCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        if (auth.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
            org.springframework.security.oauth2.core.user.OAuth2User oAuth2User =
                    (org.springframework.security.oauth2.core.user.OAuth2User) auth.getPrincipal();
            return oAuth2User.getAttribute("name"); // This gets the email instead of the ID
        }

        return auth.getName();
    }
}

