package com.exe.persai.service;

import com.exe.persai.model.entity.ReferralCode;
import com.exe.persai.model.entity.Users;
import com.exe.persai.repository.ReferralCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReferralCodeService {
    private final ReferralCodeRepository referralCodeRepository;

    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(24);
        } while (referralCodeRepository.existsByCode(code));
        return code;
    }

    public ReferralCode createReferralCodeForNewUser(Users user) {
        ReferralCode referralCode = ReferralCode.builder()
                .user(user)
                .code(generateUniqueCode())
                .build();
        return referralCodeRepository.save(referralCode);
    }
}
