package com.exe.persai.repository;

import com.exe.persai.model.entity.GptMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GptMessageRepository extends JpaRepository<GptMessage, UUID> {
    List<GptMessage> findAllByUser_IdOrderByCreatedAtAsc(UUID userId);
}
