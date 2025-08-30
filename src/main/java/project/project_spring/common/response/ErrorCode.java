package project.project_spring.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMON400", "유효하지 않는 값입니다."),

    // AUTH
    INVALIDATE_TOKEN(HttpStatus.BAD_REQUEST, "AUTH400", "토큰 정보가 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH401", "AccessToken 유효 기간이 만료되었습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "AUTH402", "토큰을 찾을 수 없습니다."),

    // MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER400", "존재하지 않는 회원 ID입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

}
