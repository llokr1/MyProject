package project.project_spring.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.project_spring.auth.jwt.JwtTokenProvider;
import project.project_spring.user.domain.Member;
import project.project_spring.user.service.MemberService;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public void signup(SignupRequest request) {

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Member newMember = Member.of(request, encryptedPassword);

        memberService.saveMember(newMember);

        log.info("회원가입 완료 - 사용자 이름 : {}", newMember.getMemberName());

    }

    @Transactional
    public LoginResponse login(LoginRequest request){

        // DB에서 사용자를 조회 (아이디 검증)
        Member member = memberService.findMemberByEmail(request.getEmail());

        // DB에 저장된 비밀번호 값과 비교 (비밀번호 검증)
        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 두 정보가 모두 일치하면 Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getEmail(), null, Collections.singleton(()-> member.getRole().name())
        );

        // accessToken 생성
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // DTO 반환
        return LoginResponse.of(member.getId(), member.getMemberName(), accessToken);

    }
}