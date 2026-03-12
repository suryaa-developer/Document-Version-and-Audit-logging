package com.Document.DocAudit.repository;

import com.Document.DocAudit.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByProviderAndProviderId(String provider,String providerId);
    Optional<UserEntity> findByEmail(String email);
}
