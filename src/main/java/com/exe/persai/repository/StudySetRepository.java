package com.exe.persai.repository;

import com.exe.persai.model.entity.StudySet;
import com.exe.persai.model.enums.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudySetRepository extends JpaRepository<StudySet, Integer> {
    boolean existsByStudySetNameAndStatusTrue(String name);
    Optional<StudySet> findByIdAndStatusTrue(Integer id);
    Optional<StudySet> findByIdAndUser_IdAndStatusTrue(Integer id, UUID userId);
    List<StudySet> findAllByStudySetNameContainsIgnoreCaseAndStatusTrue(String search);
    List<StudySet> findAllByVisibilityAndStudySetNameContainsIgnoreCaseAndStatusTrue(Visibility visibility, String search);
    List<StudySet> findAllByUser_IdAndStudySetNameContainsIgnoreCaseAndStatusTrue(UUID userId, String search);
}
