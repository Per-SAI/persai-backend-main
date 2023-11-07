package com.exe.persai.service;

import com.exe.persai.model.entity.Users;
import com.exe.persai.model.enums.SubscriptionType;
import com.exe.persai.model.enums.UserStatus;
import com.exe.persai.model.exception.ResourceNotFoundException;
import com.exe.persai.model.response.LoginResponse;
import com.exe.persai.repository.UserRepository;
import com.exe.persai.security.UserDetailsImpl;
import com.exe.persai.security.jwt.JwtTokenProvider;
import com.exe.persai.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthenticateService {
    private final JwtTokenProvider jwtTokenProvider;

    public static Users getCurrentUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetailsImpl) principal).getUser();
            }
        }
        throw new ResourceNotFoundException("Current user not found in the security context");
    }

    public static void checkUserStatusIfDeleted(Users user) {
        if (user.getStatus().equals(UserStatus.DELETED))
            throw new AccessDeniedException("Your account is locked");
    }

    public static boolean checkProSubscriptionOrHigher(Users user) {
        //ADMIN is also PRO Subscription
        if (user.getUserSubscription() == null) return true;
        int currentLevel;
        if (user.getUserSubscription().getSubscription().getId().equals(SubscriptionType.BASIC.toString())
                || user.getUserSubscription().getExpiredDatetime().isAfter(Instant.now()))
            currentLevel = user.getUserSubscription().getSubscription().getLevel();
        else currentLevel = SubscriptionType.BASIC.getLevel();
        return currentLevel >= 1;
    }

    public LoginResponse getNewAccessTokenFromRefreshToken(HttpServletRequest request) {
        String refreshToken = JwtUtils.getJwtFromRequest(request);
        if (!StringUtils.hasText(refreshToken) || !jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new AccessDeniedException("Invalid or expired Refresh token");
        }
        String accessToken = jwtTokenProvider.generateAccessToken(refreshToken);
        return new LoginResponse(accessToken, refreshToken);
    }
}
