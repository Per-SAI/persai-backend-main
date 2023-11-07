package com.exe.persai.repository;

import com.exe.persai.model.entity.UserQuestionNote;
import com.exe.persai.model.entity.compositeKey.UserQuestionNoteKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserQuestionNoteRepository extends JpaRepository<UserQuestionNote, UserQuestionNoteKey> {

    Optional<UserQuestionNote> findByUser_IdAndQuestion_Id(UUID userId, Integer questionId);
    List<UserQuestionNote> findByUser_IdAndQuestion_StudySet_Id(UUID userId, Integer studySetId);
}
