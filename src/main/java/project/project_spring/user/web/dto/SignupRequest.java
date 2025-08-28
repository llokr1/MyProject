package project.project_spring.user.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {

    public String userName;

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
