package project.project_spring.user.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    public Long userId;

    public String userName;

    public String accessToken;

    public String refreshToken;

    @Builder
    private LoginResponse(Long userId, String userName, String accessToken, String refreshToken){
        this.userId = userId;
        this.userName = userName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(Long userId, String userName, String accessToken, String refreshToken){

        return LoginResponse.builder()
                .userId(userId)
                .userName(userName)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
