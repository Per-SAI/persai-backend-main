package com.exe.persai.service;

import com.exe.persai.constants.MessageConstants;
import com.exe.persai.model.entity.Image;
import com.exe.persai.model.entity.Pomodoro;
import com.exe.persai.model.entity.Users;
import com.exe.persai.model.enums.Role;
import com.exe.persai.model.enums.UserStatus;
import com.exe.persai.model.exception.BadRequestException;
import com.exe.persai.model.exception.ResourceNotFoundException;
import com.exe.persai.model.request.UpdatePomodoroRequest;
import com.exe.persai.model.request.UpdateUserRequest;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.PomodoroResponse;
import com.exe.persai.model.response.UserResponse;
import com.exe.persai.repository.ImageRepository;
import com.exe.persai.repository.PomodoroRepository;
import com.exe.persai.repository.UserRepository;
import com.exe.persai.service.mapper.PomodoroMapper;
import com.exe.persai.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PomodoroRepository pomodoroRepository;
    private final FileService fileService;

    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email of user not found"));
    }

    public UserResponse getCurrentUser() {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        return UserMapper.INSTANCE.toUserResponse(currentUser);
    }

    public UserResponse getUserById(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ID not found"));
        return UserMapper.INSTANCE.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers(UserStatus status, Sort sort) {
        List<Users> users;
        if (status == null) users = userRepository.findAll(sort);
        else users = userRepository.findAllByStatus(status, sort);
        return users.stream().map(UserMapper.INSTANCE::toUserResponse).toList();
    }

    public List<UserResponse> getAllUsersUsedReferralCode(UserStatus status, Sort sort) {
        List<Users> users;
        if (status == null) users = userRepository.findAllByReferralCode_UsingReferralCodeTrue(sort);
        else users = userRepository.findAllByStatusAndReferralCode_UsingReferralCodeTrue(status, sort);
        return users.stream().map(UserMapper.INSTANCE::toUserResponse).toList();
    }

    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Users user = userRepository.findById(currentUser.getId()).get();
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getTheme() != null) {
            if (!AuthenticateService.checkProSubscriptionOrHigher(user))
                throw new AccessDeniedException("Basic user can not update Theme");
            user.setTheme(request.getTheme());
        }
        return UserMapper.INSTANCE.toUserResponse(user);
    }

    public GeneralResponse updateAvatarOfCurrentUser(MultipartFile multipartFile) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Users user = userRepository.findById(currentUser.getId()).get();

        String s3ImageName = fileService.uploadImage(multipartFile);
        String feImageName = null;
        if (user.getAvatar() != null && user.getAvatar().getS3ImageName() != null && user.getAvatar().getS3ImageName().equals(s3ImageName)) {
            feImageName = user.getAvatar().getFeImageName();
        }
        else feImageName = createNewAvatar(user, s3ImageName);
        return new GeneralResponse(returnAvatarResponse(feImageName));
    }

    public GeneralResponse banUser(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ID not found"));
        if (user.getRole().equals(Role.ADMIN))
            throw new BadRequestException("Server does not support this action to ADMIN");
        user.setStatus(UserStatus.DELETED);
        user.setEnabled(false);
        return new GeneralResponse("Ban user successfully");
    }

    public GeneralResponse unbanUser(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ID not found"));
        if (user.getRole().equals(Role.ADMIN))
            throw new BadRequestException("Server does not support this action to ADMIN");
        user.setStatus(UserStatus.SUCCEED);
        user.setEnabled(true);
        return new GeneralResponse("Unban user successfully");
    }

    public PomodoroResponse getPomodoroClockOfCurrentUser() {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Pomodoro pomodoro = pomodoroRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pomodoro clock with current user ID not found"));
        return PomodoroMapper.INSTANCE.toPomodoroResponse(pomodoro);
    }

    public PomodoroResponse updatePomodoroOfCurrentUser(UpdatePomodoroRequest request) {
        Users currentUser = AuthenticateService.getCurrentUserFromSecurityContext();
        AuthenticateService.checkUserStatusIfDeleted(currentUser);
        Pomodoro pomodoro = pomodoroRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pomodoro clock with current user ID not found"));
        pomodoro.setStudyTime(request.getStudyTime());
        pomodoro.setShortBreak(request.getShortBreak());
        pomodoro.setLongBreak(request.getLongBreak());
        pomodoro.setLongBreakInterval(request.getLongBreakInterval());
        pomodoro.setStatus(request.getStatus());
        return PomodoroMapper.INSTANCE.toPomodoroResponse(pomodoro);
    }

    private String createNewAvatar(Users user, String s3ImageName) {
        Image image = Image.builder()
                .feImageName("Avatar-" + System.currentTimeMillis() + "-" + user.getEmail())
                .s3ImageName(s3ImageName)
                .build();
        image = imageRepository.save(image);
        user.setAvatar(image);
        return user.getAvatar().getFeImageName();
    }

    private String returnAvatarResponse(String feImageName) {
        return MessageConstants.host + MessageConstants.getImageEndpoint + feImageName;
    }
}
