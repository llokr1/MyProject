package project.project_spring.auth.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.project_spring.member.domain.Member;
import project.project_spring.auth.web.dto.LoginRequest;
import project.project_spring.auth.web.dto.LoginResponse;
import project.project_spring.auth.web.dto.SignupRequest;
import project.project_spring.member.service.MemberService;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormAuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @Override
    public void signup(SignupRequest request) {

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Member newMember = Member.of(request, encryptedPassword);

        memberService.saveMember(newMember);

        log.info("회원가입 완료 - 사용자 이름 : {}", newMember.getMemberName());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }

}
