package com.hyuseinlesho.powerlog.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception exception) {
        HttpStatus status;
        String description;

        logger.error("Exception caught: ", exception);

        description = switch (exception) {
            case BadCredentialsException badCredentialsException -> {
                status = HttpStatus.UNAUTHORIZED;
                yield "The username or password is incorrect.";
            }
            case AccountStatusException accountStatusException -> {
                status = HttpStatus.FORBIDDEN;
                yield "The account is locked.";
            }
            case AccessDeniedException accessDeniedException -> {
                status = HttpStatus.FORBIDDEN;
                yield "You are not authorized to access this resource.";
            }
            case SignatureException signatureException -> {
                status = HttpStatus.FORBIDDEN;
                yield "The JWT signature is invalid.";
            }
            case ExpiredJwtException expiredJwtException -> {
                status = HttpStatus.FORBIDDEN;
                yield "The JWT token has expired.";
            }
            case NoResourceFoundException noResourceFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested resource was not found.";
            }
            case ExerciseAlreadyExistsException exerciseAlreadyExistsException -> {
                status = HttpStatus.BAD_REQUEST;
                yield "The requested exercise already exists.";
            }
            case ExerciseNotFoundException exerciseNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested exercise was not found.";
            }
            case PhotoNotFoundException photoNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested photo was not found.";
            }
            case RoutineNotFoundException routineNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested routine was not found.";
            }
            case UserNotFoundException userNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested user was not found.";
            }
            case WeightLogNotFoundException weightLogNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested weight log was not found.";
            }
            case WorkoutNotFoundException workoutNotFoundException -> {
                status = HttpStatus.NOT_FOUND;
                yield "The requested workout was not found.";
            }
            case MaxUploadSizeExceededException maxUploadSizeExceededException -> {
                status = HttpStatus.PAYLOAD_TOO_LARGE;
                yield "The uploaded file size exceeds the allowable limit.";
            }
            case null, default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                yield "Unknown internal server error.";
            }
        };

        ModelAndView mav = new ModelAndView("error/default");
        mav.addObject("status", status.value());
        mav.addObject("error", description);
        return mav;
    }
}
