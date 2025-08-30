package project.project_spring.auth.web.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponse {

    private String email;
    private String memberName;

    @Builder
    private SignupResponse(String email, String memberName){
        this.email = email;
        this.memberName = memberName;
    }

    public static SignupResponse of(String email, String memberName){
        return SignupResponse.builder()
                .email(email)
                .memberName(memberName)
                .build();
    }

}
