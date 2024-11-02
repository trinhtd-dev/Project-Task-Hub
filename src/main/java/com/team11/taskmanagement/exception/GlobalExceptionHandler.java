package com.team11.taskmanagement.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
        log.error("Handle ResourceNotFoundException", ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/not-found";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        log.error("Handle unexpected exception", ex);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error/general-error";
    }
}