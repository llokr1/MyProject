package project.project_spring.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class GlobalResponse {

    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object result;

    public static ResponseEntity<GlobalResponse> onSuccess(SuccessCode successCode, Object result){
        return new ResponseEntity<>(
                new GlobalResponse(true, successCode.getCode(), successCode.getMessage(), result),
                successCode.getStatus());
    }

    public static ResponseEntity<GlobalResponse> onSuccess(SuccessCode successCode){
        return new ResponseEntity<>(
                new GlobalResponse(true, successCode.getCode(), successCode.getMessage(), null),
                successCode.getStatus());
    }

    public static ResponseEntity<GlobalResponse> onFailure(ErrorCode errorCode, Object result){
        return new ResponseEntity<>(
                new GlobalResponse(false, errorCode.getCode(), errorCode.getMessage(), result),
                errorCode.getStatus());
    }

    public static ResponseEntity<GlobalResponse> onFailure(ErrorCode errorCode){
        return new ResponseEntity<>(
                new GlobalResponse(false, errorCode.getCode(), errorCode.getMessage(), null),
                errorCode.getStatus());
    }

}
