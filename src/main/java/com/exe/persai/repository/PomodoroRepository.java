package com.exe.persai.repository;

import com.exe.persai.model.entity.Pomodoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PomodoroRepository extends JpaRepository<Pomodoro, Integer> {
    Optional<Pomodoro> findByUser_Id(UUID userId);
}
