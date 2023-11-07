package com.exe.persai.controller;

import com.exe.persai.controller.api.StudySetController;
import com.exe.persai.model.request.CreateStudySetExcel;
import com.exe.persai.model.request.CreateStudySetRequest;
import com.exe.persai.model.request.CreateUserQuestionNoteRequest;
import com.exe.persai.model.response.BasicStudySetResponse;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.StudySetResponse;
import com.exe.persai.model.response.UserQuestionNoteResponse;
import com.exe.persai.service.StudySetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudySetControllerImpl implements StudySetController {

    private final StudySetService studySetService;

    @Override
    public ResponseEntity<StudySetResponse> createStudySet(MultipartFile multipartFile, CreateStudySetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studySetService.createNewStudySet(multipartFile, request));
    }

    @Override
    public ResponseEntity<StudySetResponse> createStudySetFromExcel(MultipartFile multipartFile, CreateStudySetExcel request, MultipartFile multipartExcelFile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studySetService.createNewStudySetExcel(multipartFile, request, multipartExcelFile));
    }

    @Override
    public ResponseEntity<List<CreateStudySetRequest.CreateQuestionRequest>> parseQuestionsSetFromExcel(MultipartFile multipartExcelFile) {
        return ResponseEntity.ok(studySetService.parseQuestionsSetFromExcel(multipartExcelFile));
    }

    @Override
    public ResponseEntity<StudySetResponse> updateStudySet(Integer studySetId) {
        return null;
    }

    @Override
    public ResponseEntity<StudySetResponse> getStudySetById(Integer studySetId) {
        return ResponseEntity.ok(studySetService.getStudySetById(studySetId));
    }

    @Override
    public ResponseEntity<List<BasicStudySetResponse>> getAllStudySets(String search) {
        if (!StringUtils.hasText(search)) search = "";
        return ResponseEntity.ok(studySetService.getAllStudySets(search));
    }

    @Override
    public ResponseEntity<List<BasicStudySetResponse>> getAllStudySetsOfCurrentUser(String search) {
        if (!StringUtils.hasText(search)) search = "";
        return ResponseEntity.ok(studySetService.getAllStudySetsOfCurrentUser(search));
    }

    @Override
    public ResponseEntity<GeneralResponse> deleteStudySet(Integer studySetId) {
        return ResponseEntity.ok(studySetService.deleteStudySetById(studySetId));
    }

    @Override
    public ResponseEntity<UserQuestionNoteResponse> createNoteOfCurrentQuestionForCurrentUser(Integer questionId, CreateUserQuestionNoteRequest request) {
        return ResponseEntity.ok(studySetService.createNoteOfCurrentQuestionForCurrentUser(questionId, request));
    }
}
