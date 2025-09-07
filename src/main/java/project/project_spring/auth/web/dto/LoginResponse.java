package project.project_spring.auth.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    public Long userId;

    public String memberName;

    public String accessToken;

    public String refreshToken;

    @Builder
    private LoginResponse(Long userId, String memberName, String accessToken, String refreshToken){
        this.userId = userId;
        this.memberName = memberName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(Long userId, String memberName, String accessToken, String refreshToken){

        return LoginResponse.builder()
                .userId(userId)
                .memberName(memberName)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
