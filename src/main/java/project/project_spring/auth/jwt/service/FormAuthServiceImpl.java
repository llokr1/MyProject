package project.project_spring.auth.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.project_spring.user.domain.User;
import project.project_spring.user.repository.UserRepository;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormAuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void signup(SignupRequest request) {

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = User.of(request, encryptedPassword);

        userRepository.save(newUser);

        log.info("회원가입 완료 - 사용자 이름 : {}", newUser.getUserName());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }

}
