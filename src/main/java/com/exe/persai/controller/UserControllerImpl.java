package com.exe.persai.controller;

import com.exe.persai.controller.api.UserController;
import com.exe.persai.model.enums.UserStatus;
import com.exe.persai.model.request.UpdatePomodoroRequest;
import com.exe.persai.model.request.UpdateUserRequest;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.PomodoroResponse;
import com.exe.persai.model.response.UserResponse;
import com.exe.persai.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {
    private final UserService userService;


    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers(UserStatus status, Sort sort) {
        return ResponseEntity.ok(userService.getAllUsers(status, sort));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsersUsedReferralCode(UserStatus status, Sort sort) {
        return ResponseEntity.ok(userService.getAllUsersUsedReferralCode(status, sort));
    }

    @Override
    public ResponseEntity<UserResponse> updateCurrentUser(UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @Override
    public ResponseEntity<GeneralResponse> updateAvatarOfCurrentUser(MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.updateAvatarOfCurrentUser(multipartFile));
    }

    @Override
    public ResponseEntity<GeneralResponse> banUser(UUID uuid) {
        return ResponseEntity.ok(userService.banUser(uuid));
    }

    @Override
    public ResponseEntity<GeneralResponse> unbanUser(UUID uuid) {
        return ResponseEntity.ok(userService.unbanUser(uuid));
    }

    @Override
    public ResponseEntity<PomodoroResponse> getPomodoroClockOfCurrentUser() {
        return ResponseEntity.ok(userService.getPomodoroClockOfCurrentUser());
    }

    @Override
    public ResponseEntity<PomodoroResponse> updatePomodoroClockOfCurrentUser(UpdatePomodoroRequest request) {
        return ResponseEntity.ok(userService.updatePomodoroOfCurrentUser(request));
    }
}
