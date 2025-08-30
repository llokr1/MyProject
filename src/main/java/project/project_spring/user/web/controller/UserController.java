package project.project_spring.user.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.project_spring.auth.service.AuthService;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        // Http 응답 객체 생성
        ResponseEntity<LoginResponse> response = new ResponseEntity<>(authService.login(request), HttpStatus.OK);

        return response;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupRequest request){

        authService.signup(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/main")
    public ResponseEntity<Object> getMain(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
