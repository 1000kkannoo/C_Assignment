package callbusLab.zaritalk.Assignment.global.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.INTERNAL_SERVER_ERROR;
import static callbusLab.zaritalk.Assignment.global.config.exception.CustomErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public CustomErrorResponse handleException(
            CustomException e,
            HttpServletRequest request
    ) {
        log.error("errorCode : {}, url {}, message: {}",
                e.getCustomErrorCode(), request.getRequestURI(), e.getDetaliMessage());

        return CustomErrorResponse.builder()
                .status(e.getCustomErrorCode())
                .statusMessage(e.getDetaliMessage())
                .build();
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            ConstraintViolationException.class
    })
    public CustomErrorResponse handleBadRequest(
            Exception e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getMessage());

        return CustomErrorResponse.builder()
                .status(INVALID_REQUEST)
                .statusMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomErrorResponse handleException(
            Exception e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getMessage());

        return CustomErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR)
                .statusMessage(INVALID_REQUEST.getStatusMessage())
                .build();
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public CustomErrorResponse handleBadRequest(
            MethodArgumentNotValidException e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());

        return CustomErrorResponse.builder()
                .status(INVALID_REQUEST)
                .statusMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(
            MissingServletRequestParameterException.class
    )
    public CustomErrorResponse handleBadRequest(
            MissingServletRequestParameterException e, HttpServletRequest request
    ) {
        log.error("url {}, message: {}",
                request.getRequestURI(), e.getParameterName() + " 값이 등록되지 않았습니다.");

        return CustomErrorResponse.builder()
                .status(INVALID_REQUEST)
                .statusMessage(e.getParameterName() + " 값이 등록되지 않았습니다.")
                .build();
    }

}
