package project.project_spring.auth.jwt.service;

import project.project_spring.auth.web.dto.LoginRequest;
import project.project_spring.auth.web.dto.LoginResponse;
import project.project_spring.auth.web.dto.SignupRequest;

public interface AuthService {

    public void signup(SignupRequest request);

    public LoginResponse login(LoginRequest request);


}
