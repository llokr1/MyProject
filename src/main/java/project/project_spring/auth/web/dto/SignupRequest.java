package project.project_spring.auth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    public String memberName;

    @NotNull
    @Email
    public String email;

    @NotNull
    public String gender;

    @NotNull
    public String password;

    @NotNull
    public Integer age;

    @NotNull
    public String address;

    @NotNull
    public String specAddress;

}