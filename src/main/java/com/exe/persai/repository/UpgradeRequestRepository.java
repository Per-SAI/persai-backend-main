package com.exe.persai.repository;

import com.exe.persai.model.entity.UpgradeRequest;
import com.exe.persai.model.enums.UpgradeRequestStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpgradeRequestRepository extends JpaRepository<UpgradeRequest, Integer> {

    List<UpgradeRequest> findAllByStatus(UpgradeRequestStatus status, Sort sort);
}
