package com.exe.persai.repository;

import com.exe.persai.model.entity.Users;
import com.exe.persai.model.enums.UserStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);
    List<Users> findAllByStatus(UserStatus status, Sort sort);
    List<Users> findAllByReferralCode_UsingReferralCodeTrue(Sort sort);
    List<Users> findAllByStatusAndReferralCode_UsingReferralCodeTrue(UserStatus status, Sort sort);

}
