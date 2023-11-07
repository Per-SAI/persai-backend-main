package com.exe.persai.controller.exceptionHandler;

import com.exe.persai.model.exception.BadRequestException;
import com.exe.persai.model.exception.ResourceAlreadyExistsException;
import com.exe.persai.model.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import java.time.Instant;

@RestControllerAdvice
public class CommonExceptionHandler implements ProblemHandling {

    @ExceptionHandler
    public ResponseEntity<Problem> handleIllegalStateException (
            IllegalStateException e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }
    @ExceptionHandler
    public ResponseEntity<Problem> handleAuthenticationException (
            AuthenticationException e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.UNAUTHORIZED.getReasonPhrase())
                .withStatus(Status.UNAUTHORIZED)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleAccessDeniedException (
            AccessDeniedException e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.FORBIDDEN.getReasonPhrase())
                .withStatus(Status.FORBIDDEN)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestException (
            BadRequestException e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.BAD_REQUEST.getReasonPhrase())
                .withStatus(Status.BAD_REQUEST)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleResourceNotFoundException (
            ResourceNotFoundException e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.NOT_FOUND.getReasonPhrase())
                .withStatus(Status.NOT_FOUND)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleResourceAlreadyExistsException (
            ResourceAlreadyExistsException e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.CONFLICT.getReasonPhrase())
                .withStatus(Status.CONFLICT)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleGeneralException(Exception e, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .with("timestamp", Instant.now())
                .with("error", Status.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withDetail(e.getMessage())
                .build();
        return create(problem, request);
    }

}
