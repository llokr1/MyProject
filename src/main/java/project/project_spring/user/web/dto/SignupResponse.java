package project.project_spring.user.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponse {

    private String email;
    private String userName;

    @Builder
    private SignupResponse(String email, String userName){
        this.email = email;
        this.userName = userName;
    }

    public static SignupResponse of(String email, String memberName){
        return SignupResponse.builder()
                .email(email)
                .userName(memberName)
                .build();
    }

}
