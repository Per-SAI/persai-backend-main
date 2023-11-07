package com.exe.persai.repository;

import com.exe.persai.model.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReferralCodeRepository extends JpaRepository<ReferralCode, UUID> {
    boolean existsByCode(String code);
    Optional<ReferralCode> findByCodeAndUserIdNotAndUser_EnabledTrue(String code, UUID userId);
}
