package project.project_spring.user.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.project_spring.auth.jwt.service.AuthService;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/main")
    public String mainPage(){
        return "main";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        // 입력 받은 데이터를 저장할 빈 DTO 생성
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequest request){

        authService.signup(request);
        return "login";
    }
}
