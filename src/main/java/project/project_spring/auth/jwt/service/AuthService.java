package project.project_spring.auth.jwt.service;

import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;

public interface AuthService {

    public void signup(SignupRequest request);

    public LoginResponse login(LoginRequest request);


}
