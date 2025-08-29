package project.project_spring.auth.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.project_spring.auth.service.AuthService;
import project.project_spring.auth.web.dto.TokenResponse;
import project.project_spring.common.response.GlobalResponse;
import project.project_spring.common.response.SuccessCode;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/login")
    public ResponseEntity<GlobalResponse> login(@RequestBody LoginRequest request){

        // Http 응답 객체 생성
        LoginResponse response = authService.login(request);

        return GlobalResponse.onSuccess(SuccessCode.OK, response);

    }

    @PostMapping("/api/signup")
    public ResponseEntity<GlobalResponse> signup(@RequestBody SignupRequest request){

        authService.signup(request);

        return GlobalResponse.onSuccess(SuccessCode.OK);
    }

    @PostMapping("/api/reissue")
    public ResponseEntity<GlobalResponse> reissueToken(HttpServletRequest request, HttpServletResponse response){

        TokenResponse tokenResponse = authService.reissueToken(request, response);

        return GlobalResponse.onSuccess(SuccessCode.OK, tokenResponse);
    }


    /*
        인가 테스트용 API
     */
    @GetMapping("/main")
    public ResponseEntity<GlobalResponse> getMain(){
        return GlobalResponse.onSuccess(SuccessCode.OK_FROM_REQUEST_MAIN);
    }
}