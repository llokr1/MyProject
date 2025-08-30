package project.project_spring.user.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {

    @Email
    @NotNull
    public String email;

    @NotNull
    public String password;

}