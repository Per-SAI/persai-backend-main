package com.exe.persai.service;

import com.exe.persai.model.entity.*;
import com.exe.persai.model.entity.compositeKey.UserQuestionNoteKey;
import com.exe.persai.model.enums.ExcelMode;
import com.exe.persai.model.enums.Role;
import com.exe.persai.model.enums.Visibility;
import com.exe.persai.model.exception.BadRequestException;
import com.exe.persai.model.exception.ResourceAlreadyExistsException;
import com.exe.persai.model.exception.ResourceNotFoundException;
import com.exe.persai.model.request.CreateStudySetExcel;
import com.exe.persai.model.request.CreateStudySetRequest;
import com.exe.persai.model.request.CreateUserQuestionNoteRequest;
import com.exe.persai.model.response.BasicStudySetResponse;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.StudySetResponse;
import com.exe.persai.model.response.UserQuestionNoteResponse;
import com.exe.persai.repository.*;
import com.exe.persai.service.mapper.StudySetMapper;
import com.exe.persai.service.mapper.UserQuestionNoteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StudySetService {
    private final StudySetMapper studySetMapper;
    private final int BASIC_MAX_STUDY_SETS_NUMBER = 10;
    private final int BASIC_MAX_NOTE_PER_STUDY_SET_NUMBER = 5;
    private final StudySetRepository studySetRepository;
    private final QuestionsRepository questionsRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final UserQuestionNoteRepository userQuestionNoteRepository;
    private final FileService fileService;
    private final GptService gptService;

    public StudySetResponse createNewStudySet(MultipartFile multipartFile, CreateStudySetRequest request) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);

        checkUserSubscriptionToCreateNewStudySet(currentUser, request.getVisibility());
        validateStudySetRequest(request);

        return addNewStudySet(currentUser, multipartFile, request);
    }

    public StudySetResponse createNewStudySetExcel(MultipartFile multipartFile, CreateStudySetExcel request, MultipartFile multipartExcelFile) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);

        checkUserSubscriptionToCreateNewStudySet(currentUser, request.getVisibility());

        return addNewStudySetExcel(currentUser, multipartFile, request, multipartExcelFile);
    }

    public List<CreateStudySetRequest.CreateQuestionRequest> parseQuestionsSetFromExcel(MultipartFile multipartExcelFile) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);

        return fileService.processExcelFile(multipartExcelFile, ExcelMode.PARSE);
    }

    public StudySetResponse getStudySetById(Integer studySetId) {
        StudySet studySet = studySetRepository.findByIdAndStatusTrue(studySetId)
                .orElseThrow(() -> new ResourceNotFoundException("Study set ID not found"));
        try {
            Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
            if (currentUser.getRole().equals(Role.ADMIN)
                    || studySet.getUser().getId().equals(currentUser.getId())
                    || studySet.getVisibility().equals(Visibility.PUBLIC)
            ) {
                StudySetResponse studySetResponse = StudySetMapper.INSTANCE.toStudySetResponse(studySet);
                if (currentUser.getRole().equals(Role.STUDENT))
                    return addNoteToQuestionResponse(currentUser.getId(), studySetResponse);
                return studySetResponse;
            }
            throw new AccessDeniedException("You don't have permission to access this resource");
        }
        catch (ResourceNotFoundException e) {
            //Non login user
            if (studySet.getVisibility().equals(Visibility.PUBLIC))
                return StudySetMapper.INSTANCE.toStudySetResponse(studySet);
            throw new ResourceNotFoundException("Study set ID not found");
        }
    }

    public List<BasicStudySetResponse> getAllStudySets(String search) {
        List<StudySet> studySets;
        try {
            Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
            if (currentUser.getRole().equals(Role.ADMIN)) {
                studySets = studySetRepository.findAllByStudySetNameContainsIgnoreCaseAndStatusTrue(search);
            }
            else studySets = studySetRepository.findAllByVisibilityAndStudySetNameContainsIgnoreCaseAndStatusTrue(Visibility.PUBLIC, search);
        }
        catch (ResourceNotFoundException e) {
            //Non login user
            studySets = studySetRepository.findAllByVisibilityAndStudySetNameContainsIgnoreCaseAndStatusTrue(Visibility.PUBLIC, search);
        }
        Comparator<StudySet> idComparator = Comparator.comparing(StudySet::getId).reversed();
        studySets.sort(idComparator);
        return studySets.stream().map(StudySetMapper.INSTANCE::toBasicStudySetResponse).toList();
    }

    public List<BasicStudySetResponse> getAllStudySetsOfCurrentUser(String search) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        List<StudySet> studySets = studySetRepository.findAllByUser_IdAndStudySetNameContainsIgnoreCaseAndStatusTrue(currentUser.getId(), search);
        return studySets.stream().map(StudySetMapper.INSTANCE::toBasicStudySetResponse).toList();
    }

    public GeneralResponse deleteStudySetById(Integer studySetId) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        StudySet studySet = null;
        if (currentUser.getRole().equals(Role.ADMIN)) {
            studySet = studySetRepository.findByIdAndStatusTrue(studySetId)
                    .orElseThrow(() -> new ResourceNotFoundException("Study set ID not found"));
        }
        else {
            studySet = studySetRepository.findByIdAndUser_IdAndStatusTrue(studySetId, currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Study set ID not found"));
        }
        studySet.setStatus(false);
        return new GeneralResponse("Delete study set successfully");
    }

    public UserQuestionNoteResponse createNoteOfCurrentQuestionForCurrentUser(Integer questionId, CreateUserQuestionNoteRequest request) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Questions question = questionsRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question ID not found"));
        checkUserSubscriptionToCreateNote(currentUser, question.getStudySet().getId());
        Optional<UserQuestionNote> isExistNote = userQuestionNoteRepository.findByUser_IdAndQuestion_Id(currentUser.getId(), questionId);
        UserQuestionNote note;
        if (isExistNote.isPresent()) {
            note = isExistNote.get();
            note.setNote(request.getNote());
        }
        else {
            note = UserQuestionNote.builder()
                    .id(new UserQuestionNoteKey(null, null))
                    .user(currentUser)
                    .question(question)
                    .note(request.getNote())
                    .build();
            note = userQuestionNoteRepository.save(note);
        }
        return UserQuestionNoteMapper.INSTANCE.toUserQuestionNoteResponse(note);
    }

    private void checkUserSubscriptionToCreateNewStudySet(Users currentUser, Visibility visibilityRequest) {
        Users user = userRepository.findById(currentUser.getId()).get();
        boolean check = AuthenticateService.checkProSubscriptionOrHigher(user);
        if (!check) {
            if (visibilityRequest.equals(Visibility.PRIVATE))
                throw new AccessDeniedException("Basic subscription user can only create PUBLIC set");
            int numberOfStudySets = user.getStudySets().size();
            if (numberOfStudySets >= BASIC_MAX_STUDY_SETS_NUMBER)
                throw new AccessDeniedException("Basic subscription user can only create at most " + BASIC_MAX_STUDY_SETS_NUMBER + " study sets");
        }
    }

    private void checkUserSubscriptionToCreateNote(Users currentUser, Integer studySetId) {
        Users user = userRepository.findById(currentUser.getId()).get();
        boolean check = AuthenticateService.checkProSubscriptionOrHigher(user);
        if (!check) {
            int numberOfNote = userQuestionNoteRepository.findByUser_IdAndQuestion_StudySet_Id(user.getId(), studySetId).size();
            if (numberOfNote >= BASIC_MAX_NOTE_PER_STUDY_SET_NUMBER)
                throw new AccessDeniedException("Basic subscription user can only create at most " + BASIC_MAX_NOTE_PER_STUDY_SET_NUMBER + " notes per study set");
        }
    }

    private void validateStudySetRequest(CreateStudySetRequest request) {
//        if (studySetRepository.existsByStudySetNameAndStatusTrue(request.getStudySetName()))
//            throw new ResourceAlreadyExistsException("Study set name has been taken");
        if (request.getQuestionsList().stream().anyMatch(Objects::isNull))
            throw new BadRequestException("Question list can not contain null value");
        request.getQuestionsList().forEach(question -> {
            if (Arrays.stream(question.getAnswers()).anyMatch(answer -> !StringUtils.hasText(answer)))
                throw new BadRequestException("Each question can not contain blank answer");
            if (StringUtils.hasText(question.getCorrectAnswer())) {
                if (Arrays.stream(question.getAnswers()).noneMatch(answer -> answer.equalsIgnoreCase(question.getCorrectAnswer())))
                    throw new BadRequestException("Correct answer " + question.getCorrectAnswer()
                            + " is not included in the list of answers " + Arrays.toString(question.getAnswers()));
                if (question.isGptGenerated())
                    throw new BadRequestException("Correct answer has been inputted. GPT only generates answer for non-input correct answer");
            }
            else if (!question.isGptGenerated())
                throw new BadRequestException("Please choose to input correct answer or let GPT generates");
        });
        int numberOfQuestionsGptGeneratesAnswer = request.getQuestionsList().stream().filter(CreateStudySetRequest.CreateQuestionRequest::isGptGenerated).toList().size();
        if (numberOfQuestionsGptGeneratesAnswer > 3)
            throw new BadRequestException("Current PerSAI version only supports at most 3 GPT-generated answers");
    }

    private StudySetResponse addNewStudySet(Users currentUser, MultipartFile multipartFile, CreateStudySetRequest request) {
        String s3ImageName = fileService.uploadImage(multipartFile);
        StudySet studySet = persistStudySetToDB(currentUser, s3ImageName, request.getStudySetName(), request.getVisibility(), request.getQuestionsList());
        return StudySetMapper.INSTANCE.toStudySetResponse(studySet);
    }

    private StudySetResponse addNewStudySetExcel(Users currentUser, MultipartFile multipartFile, CreateStudySetExcel request, MultipartFile multipartExcelFile) {
        List<CreateStudySetRequest.CreateQuestionRequest> questionRequestList = fileService.processExcelFile(multipartExcelFile, ExcelMode.CREATE);
        int numberOfQuestionsGptGeneratesAnswer = questionRequestList.stream().filter(CreateStudySetRequest.CreateQuestionRequest::isGptGenerated).toList().size();
        if (numberOfQuestionsGptGeneratesAnswer > 3)
            throw new BadRequestException("Current PerSAI version only supports at most 3 GPT-generated answers");
        String s3ImageName = fileService.uploadImage(multipartFile);
        StudySet studySet = persistStudySetToDB(currentUser, s3ImageName, request.getStudySetName(), request.getVisibility(), questionRequestList);
        return StudySetMapper.INSTANCE.toStudySetResponse(studySet);
    }

    private StudySet persistStudySetToDB(Users currentUser, String s3ImageName, String studySetName, Visibility visibility,
                                         List<CreateStudySetRequest.CreateQuestionRequest> questionRequestList) {
        Image image = Image.builder()
                .feImageName("StudySet-" + System.currentTimeMillis())
                .s3ImageName(s3ImageName)
                .build();
        image = imageRepository.save(image);

        StudySet studySet = StudySet.builder()
                .studySetName(studySetName)
                .image(image)
                .user(currentUser)
                .visibility(visibility)
                .build();
        studySet = studySetRepository.save(studySet);

        List<Questions> questionsList = new ArrayList<>();
        for (CreateStudySetRequest.CreateQuestionRequest question : questionRequestList) {
            String correctAnswer;
            String fullGptAnswer = null;
            if (StringUtils.hasText(question.getCorrectAnswer())) correctAnswer = question.getCorrectAnswer();
            else {
                //Call GPT to generate answer
                correctAnswer = gptService.callGptToGenerateCorrectAnswer(question.getQuestion(), question.getAnswers());
                fullGptAnswer = "Current PerSAI version does not support this GPT feature";
                if (Arrays.stream(question.getAnswers()).noneMatch(answer -> answer.equalsIgnoreCase(correctAnswer))) {
//                    throw new BadRequestException("GPT can not generate correct answer for question: " + question.getQuestion() + " | Maybe the list of choices does not include correct answer.");
                    question.setAnswers(appendGptAnswerToCurrentAnswers(question.getAnswers(), correctAnswer));
                }
            }
            Questions newQuestion = Questions.builder()
                    .question(question.getQuestion())
                    .answers(question.getAnswers())
                    .correctAnswer(correctAnswer)
                    .isGptGenerated(question.isGptGenerated())
                    .fullGptAnswer(fullGptAnswer)
                    .studySet(studySet)
                    .build();
            questionsList.add(newQuestion);
        }
        questionsList = questionsRepository.saveAll(questionsList);
        studySet.setQuestionsList(questionsList);
        return studySet;
    }

    private String[] appendGptAnswerToCurrentAnswers(String[] answers, String newCorrectAnswer) {
        String[] newAnswers = Arrays.copyOf(answers, answers.length + 1);
        newAnswers[newAnswers.length - 1] = newCorrectAnswer;
        return newAnswers;
    }

    private StudySetResponse addNoteToQuestionResponse(UUID userId, StudySetResponse response) {
        response.getQuestionResponses().forEach(questionResponse -> {
            Optional<UserQuestionNote> note = userQuestionNoteRepository.findByUser_IdAndQuestion_Id(userId, questionResponse.getId());
            note.ifPresent(userQuestionNote -> questionResponse.setNote(userQuestionNote.getNote()));
        });
        return response;
    }
}
