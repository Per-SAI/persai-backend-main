package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.request.CreateStudySetExcel;
import com.exe.persai.model.request.CreateStudySetRequest;
import com.exe.persai.model.request.CreateUserQuestionNoteRequest;
import com.exe.persai.model.response.BasicStudySetResponse;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.StudySetResponse;
import com.exe.persai.model.response.UserQuestionNoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/study-set")
public interface StudySetController {

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Create new study set"
    )
    @PostMapping
    ResponseEntity<StudySetResponse> createStudySet(
            @Schema(description = "Send image as form data with key \"image\"")
            @RequestPart("image") MultipartFile multipartFile,
            @RequestPart("create_study_set_request") @Valid CreateStudySetRequest request
    );

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Create new study set from excel format"
    )
    @PostMapping("/excel")
    ResponseEntity<StudySetResponse> createStudySetFromExcel(
            @Schema(description = "Send image as form data with key \"image\"")
            @RequestPart("image") MultipartFile multipartFile,
            @RequestPart("create_study_set_request") @Valid CreateStudySetExcel request,
            @RequestPart("excel") MultipartFile multipartExcelFile
    );

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Parse questions from excel file"
    )
    @PostMapping("/excel/parse-questions")
    ResponseEntity<List<CreateStudySetRequest.CreateQuestionRequest>> parseQuestionsSetFromExcel(
            @RequestParam("excel") MultipartFile multipartExcelFile
    );

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Update pre-exist study set (under development)"
    )
    @PutMapping("/{study_set_id}")
    ResponseEntity<StudySetResponse> updateStudySet(@PathVariable("study_set_id") Integer studySetId);

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Get study set by ID"
    )
    @GetMapping("/{study_set_id}")
    ResponseEntity<StudySetResponse> getStudySetById(@PathVariable("study_set_id") Integer studySetId);

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Get all study sets"
    )
    @GetMapping
    ResponseEntity<List<BasicStudySetResponse>> getAllStudySets(@RequestParam(value = "search", required = false) String search);

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Get all study sets of current user"
    )
    @GetMapping("/current")
    ResponseEntity<List<BasicStudySetResponse>> getAllStudySetsOfCurrentUser(@RequestParam(value = "search", required = false) String search);

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Delete study set by ID"
    )
    @DeleteMapping("/{study_set_id}")
    ResponseEntity<GeneralResponse> deleteStudySet(@PathVariable("study_set_id") Integer studySetId);

    @Tag(name = SwaggerApiTag.STUDY_SET)
    @Operation(
            summary = "Create or update note for a question of a study set (for current logged in user)"
    )
    @PutMapping("/question/{question_id}/note")
    @PreAuthorize("hasAuthority('STUDENT')")
    ResponseEntity<UserQuestionNoteResponse> createNoteOfCurrentQuestionForCurrentUser(@PathVariable("question_id") Integer questionId,
                                                                                       @RequestBody CreateUserQuestionNoteRequest request);
}
