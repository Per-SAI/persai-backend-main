package com.exe.persai.controller.api;

import com.exe.persai.constants.SwaggerApiTag;
import com.exe.persai.model.enums.UserStatus;
import com.exe.persai.model.request.UpdatePomodoroRequest;
import com.exe.persai.model.request.UpdateUserRequest;
import com.exe.persai.model.response.GeneralResponse;
import com.exe.persai.model.response.PomodoroResponse;
import com.exe.persai.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/user")
public interface UserController {

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Get current logged in user"
    )
    @GetMapping("/current")
    ResponseEntity<UserResponse> getCurrentUser();

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Get user by user ID (only for ADMIN)"
    )
    @GetMapping("/{user_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<UserResponse> getUserById(@PathVariable("user_id") UUID userId);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Get all users (only for ADMIN)"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value = "status", required = false) UserStatus status,
                                                   @ParameterObject Sort sort);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Get all users who have used referral code (only for ADMIN)"
    )
    @GetMapping("/used-referral-code")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<List<UserResponse>> getAllUsersUsedReferralCode(@RequestParam(value = "status", required = false) UserStatus status,
                                                   @ParameterObject Sort sort);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Update current logged in user"
    )
    @PutMapping("/current")
    ResponseEntity<UserResponse> updateCurrentUser(@RequestBody @Valid UpdateUserRequest request);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Update avatar of current logged in user"
    )
    @PutMapping("/current/avatar")
    ResponseEntity<GeneralResponse> updateAvatarOfCurrentUser(@Schema(description = "Send image as form data with key \"image\"")
                                                     @RequestParam("image") MultipartFile multipartFile);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Ban a user (only for ADMIN)"
    )
    @PutMapping("/ban/{user_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<GeneralResponse> banUser(@PathVariable("user_id") UUID uuid);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Unban a user (only for ADMIN)"
    )
    @PutMapping("/unban/{user_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<GeneralResponse> unbanUser(@PathVariable("user_id") UUID uuid);

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Get pomodoro clock settings of current user"
    )
    @GetMapping("/pomodoro-clock")
    @PreAuthorize("hasAuthority('STUDENT')")
    ResponseEntity<PomodoroResponse> getPomodoroClockOfCurrentUser();

    @Tag(name = SwaggerApiTag.USER)
    @Operation(
            summary = "Update pomodoro clock settings of current user"
    )
    @PutMapping("/pomodoro-clock")
    @PreAuthorize("hasAuthority('STUDENT')")
    ResponseEntity<PomodoroResponse> updatePomodoroClockOfCurrentUser(@RequestBody @Valid UpdatePomodoroRequest request);
}
