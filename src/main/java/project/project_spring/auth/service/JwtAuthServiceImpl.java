package project.project_spring.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.project_spring.auth.jwt.JwtTokenProvider;
import project.project_spring.auth.refresh.entity.RefreshToken;
import project.project_spring.auth.refresh.repository.RefreshTokenRepository;
import project.project_spring.auth.web.dto.TokenResponse;
import project.project_spring.common.exception.GeneralException;
import project.project_spring.common.response.ErrorCode;
import project.project_spring.user.domain.Member;
import project.project_spring.user.repository.MemberRepository;
import project.project_spring.user.service.MemberService;
import project.project_spring.user.web.dto.LoginRequest;
import project.project_spring.user.web.dto.LoginResponse;
import project.project_spring.user.web.dto.SignupRequest;
import project.project_spring.user.web.dto.SignupResponse;

import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthServiceImpl implements AuthService{

    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public SignupResponse signup(SignupRequest request) {

        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Member newMember = Member.of(request, encryptedPassword);

        memberService.saveMember(newMember);

        log.info("회원가입 완료 - 사용자 이름 : {}", newMember.getMemberName());

        return SignupResponse.of(newMember.getEmail(), newMember.getMemberName());
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
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        // refreshToken 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

        // refreshToken 저장
        refreshTokenService.saveRefreshToken(member, refreshToken);

        // DTO 반환
        return LoginResponse.of(member.getId(), member.getMemberName(), accessToken, refreshToken);

    }

    @Transactional
    public TokenResponse reissueToken(HttpServletRequest request, HttpServletResponse response) {

        // 요청의 쿠키로부터 Refresh Token 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            throw new GeneralException(ErrorCode.TOKEN_MISSING);
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .toString();

        // 가져온 Refresh Token 유효성 검사
        if(jwtTokenProvider.validateRefreshToken(refreshToken)){
            throw new GeneralException(ErrorCode.INVALIDATE_TOKEN);
        };

        // 요청에서 가져온 RefreshToken으로 userId를 찾는다.
        Long memberId = jwtTokenProvider.extractUserIdFromRefreshToken(refreshToken);

        // userId를 통해 DB에서 기존에 저장된 user의 Refresh Token를 찾는다.
        RefreshToken storedRefreshToken = refreshTokenRepository.findByUserId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.TOKEN_MISSING));

        // 요청 받은 Refresh Token과 저장된 Refresh Token 비교
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)){
            throw new GeneralException(ErrorCode.INVALIDATE_TOKEN);
        }

        Member member = memberService.findMemberById(memberId);

        // 새로운 AccessToken 발급
        String newAccessToken = jwtTokenProvider.regenerateAccessToken(member);

        // RTR(Refresh Token Rotation)
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);

        refreshTokenService.saveRefreshToken(member, newRefreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(JwtTokenProvider.refreshExpiration / 1000)
                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

}
