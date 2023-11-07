package com.exe.persai.security.jwt;

import com.exe.persai.config.WebSecurityConfig;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (checkAuthWhiteListUrl(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = JwtUtils.getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateAccessToken(jwt)) {
                UserDetails userDetails = jwtTokenProvider.getUserDetailsFromAccessToken(jwt);

                // If username is valid, set auth for security context
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch (Exception e) {
            log.warn("Fail on set user authentication");
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkAuthWhiteListUrl(HttpServletRequest request) {
        return Arrays.stream(WebSecurityConfig.AUTH_WHITE_LIST).anyMatch(url -> (new AntPathRequestMatcher(url)).matches(request));
    }
}
