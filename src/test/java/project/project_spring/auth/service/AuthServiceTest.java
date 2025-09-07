package project.project_spring.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import project.project_spring.auth.jwt.JwtTokenProvider;
import project.project_spring.auth.web.dto.LoginRequest;
import project.project_spring.auth.web.dto.LoginResponse;
import project.project_spring.member.domain.Member;
import project.project_spring.member.domain.enums.Role;
import project.project_spring.member.service.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private MemberService memberService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMember = Member.of("test@test.com", "encryptedPassword", "홍길동", Role.USER);
    }

    @Test
    @DisplayName("로그인 성공 - 올바른 비밀번호 입력 시 AccessToken, RefreshToken 발급")
    void loginSuccess() {
        LoginRequest request = new LoginRequest("test@test.com", "1234");

        when(memberService.findMemberByEmail(request.getEmail())).thenReturn(testMember);
        when(passwordEncoder.matches(request.getPassword(), testMember.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("mockAccessToken");
        when(jwtTokenProvider.generateRefreshToken(testMember.getId())).thenReturn("mockRefreshToken");

        LoginResponse responseDto = authService.login(request, response);

        assertNotNull(responseDto);
        assertEquals("홍길동", responseDto.getMemberName());
        assertEquals("mockAccessToken", responseDto.getAccessToken());
        assertEquals("mockRefreshToken", responseDto.getRefreshToken());

        verify(refreshTokenService, times(1)).saveOrUpdateRefreshToken(testMember, "mockRefreshToken");
        verify(response, times(1)).addHeader(eq(HttpHeaders.SET_COOKIE), anyString());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void loginFailByWrongPassword() {
        LoginRequest request = new LoginRequest("test@test.com", "wrongPw");

        when(memberService.findMemberByEmail(request.getEmail())).thenReturn(testMember);
        when(passwordEncoder.matches(request.getPassword(), testMember.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request, response));
    }
}
