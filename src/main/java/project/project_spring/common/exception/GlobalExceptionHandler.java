package project.project_spring.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.project_spring.common.response.ErrorCode;
import project.project_spring.common.response.GlobalResponse;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse> handleGenericException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Unexpected Error Occured");
        log.error(e.getMessage(), e);
        log.error(e.getClass().getSimpleName());

        return GlobalResponse.onFailure(errorCode);
    }

    @ExceptionHandler(GeneralException.class)
    public Object handleGeneralException(GeneralException e) {
        ErrorCode errorCode = e.getErrorCode();

        return GlobalResponse.onFailure(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // Valid 검증 실패시 예외처리
    public ResponseEntity<GlobalResponse> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        // 모든 검증 오류를 하나의 문자열로 결합
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return GlobalResponse.onFailure(errorCode, errorMessage);
    }

}
