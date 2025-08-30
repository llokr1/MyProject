package project.project_spring.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    OK(HttpStatus.OK, "COMMON200", "SUCCESS!"),
    CREATED(HttpStatus.CREATED, "COMMON201", "CREATED!"),
    DELETED(HttpStatus.OK, "COMMON200", "DELETED!"),

    OK_FROM_REQUEST_MAIN(HttpStatus.OK, "TEST200", "TEST SUCCESS!");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
