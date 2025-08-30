package project.project_spring.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import project.project_spring.common.response.ErrorCode;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{ //런타임에 발생하는 예외라고 정의한다.

    private ErrorCode errorCode;

}