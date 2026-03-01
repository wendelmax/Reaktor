package io.github.wendlmax.reaktor.web;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = ResponseBody.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReaktorGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ReaktorResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ReaktorResponse.Error> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ReaktorResponse.Error("VALIDATION_ERROR", error.getField(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ReaktorResponse.error(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ReaktorResponse<Object> handleAllExceptions(Exception ex) {
        return ReaktorResponse.error(ex.getMessage());
    }
}
