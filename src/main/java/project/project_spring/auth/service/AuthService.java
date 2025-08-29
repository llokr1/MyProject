package project.project_spring.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.project_spring.auth.web.dto.TokenResponse;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;
import project.project_spring.user.web.dto.SignupResponse;

public interface AuthService {

    public SignupResponse signup(SignupRequest request);

    public LoginResponse login(LoginRequest request, HttpServletResponse response);

    public TokenResponse reissueToken(HttpServletRequest request, HttpServletResponse response);
}