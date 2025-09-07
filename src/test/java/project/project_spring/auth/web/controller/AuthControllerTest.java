package project.project_spring.auth.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project.project_spring.auth.service.AuthService;
import project.project_spring.auth.web.dto.LoginRequest;
import project.project_spring.auth.web.dto.LoginResponse;
import project.project_spring.auth.web.dto.SignupRequest;
import project.project_spring.auth.web.dto.SignupResponse;
import project.project_spring.common.exception.GeneralException;
import project.project_spring.common.response.ErrorCode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
//@Import(AuthControllerTest.TestConfig.class)
@AutoConfigureMockMvc(addFilters = false) // SecurityFilter 비활성화
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

//    @TestConfiguration
//    static class TestConfig {
//        // JPA Auditing 대신 쓸 Dummy Bean
//        @Bean
//        AuditorAware<String> auditorProvider() {
//            return () -> Optional.of("test-user");
//        }
//    }

    @Nested
    @DisplayName("로그인 기능은")
    public class LoginTest{

        @Test
        @DisplayName("성공 시 200 OK를 반환한다.")
        public void 로그인_성공() throws Exception{

            // given
            LoginRequest loginRequest = new LoginRequest("test@example.com", "1234");
            LoginResponse loginResponse =
                    LoginResponse.of(1L, "홍길동", "mockAccessToken", "mockRefreshToken");

            // when
            // 요청이 들어오고, Service 계층을 호출할 때 해당 작업을 정의한다.
            when(authService.login(any(), any())).thenReturn(loginResponse);

            mockMvc.perform(post("/login")
                        //.with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
            //then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isSuccess").value(true))
                    .andExpect(jsonPath("$.code").value("COMMON200"))
                    .andExpect(jsonPath("$.message").value("SUCCESS!"))
                    .andExpect(jsonPath("$.result.userId").value(1))
                    .andExpect(jsonPath("$.result.userName").value("홍길동"))
                    .andExpect(jsonPath("$.result.accessToken").value("mockAccessToken"))
                    .andExpect(jsonPath("$.result.refreshToken").value("mockRefreshToken"));

//            mockMvcTester.post().uri("/login")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .assertThat()
//                    .apply(print())
//                    .hasStatus2xxSuccessful()
//                    .bodyJson()
//                    .isStrictlyEqualTo("{\"isSuccess\": true, " +
//                                                "\"code\": \"COMMON200\", " +
//                                                "\"result\": {" +
//                                                "\"userId\": 1"+
//                                                "\"accessToken\": mockAccessToken"+
//                                                "\"refreshToken\": mockRefreshToken");

        }

        @Test
        @DisplayName("비밀번호가 일치하지 않을 경우 AUTH403을 반환한다.")
        public void 비밀번호_불일치() throws Exception{

            //given
            LoginRequest loginRequest = new LoginRequest("ghdwlsrl100@gmail.com", "invalidPassword");

            //when
            when(authService.login(any(), any())).thenThrow(new GeneralException(ErrorCode.INVALIDATE_PASSWORD));

            //then
            mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.isSuccess").value(false))
                    .andExpect(jsonPath("$.code").value("AUTH403"))
                    .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));

        }

        @Test
        @DisplayName("아이디가 존재하지 않을 경우 MEMBER400을 반환한다.")
        public void 존재하지_않는_회원() throws Exception{

            //given
            LoginRequest loginRequest = new LoginRequest("invalidId@example.com", "1234");

            //when
            when(authService.login(any(), any())).thenThrow(new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

            //then
            mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.isSuccess").value(false))
                    .andExpect(jsonPath("$.code").value("MEMBER400"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 회원 ID입니다."));

        }

        @Test
        @DisplayName("이메일 형식이 잘못된 경우 400 BAD_REQUEST를 반환한다.")
        public void 올바르지_않은_이메일_형식() throws Exception{

            //given
            LoginRequest loginRequest = new LoginRequest("invalidId-example-com", "1234");

            //when
            when(authService.login(any(), any())).thenThrow(new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

            //then
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("$.isSuccess").value(false))
                    .andExpect(jsonPath("$.code").value("MEMBER400"))
                    .andExpect(jsonPath("$.message").value("존재하지 않는 회원 ID입니다."));
        }

    }

    @Nested
    @DisplayName("회원가입 기능은")
    public class signupTest{

        @Test
        @DisplayName("성공 시 200 OK를 반환한다.")
        public void 회원가입_성공() throws Exception{

            //given
            SignupRequest signupRequest =
                    new SignupRequest(
                            "홍진기",
                            "test@example.com",
                            "FEMALE",
                            "1234",
                            24,
                            "경기도 성남시",
                            "수정대로 1342"
                    );

            SignupResponse signupResponse = SignupResponse.of("test@example.com", "홍진기");

            //when
            when(authService.signup(any())).thenReturn(signupResponse);

            //then
            mockMvc.perform(post("/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signupRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isSuccess").value(true))
                    .andExpect(jsonPath("$.code").value("COMMON200"))
                    .andExpect(jsonPath("$.message").value("SUCCESS!"))
                    .andExpect(jsonPath("$.result.email").value("test@example.com"))
                    .andExpect(jsonPath("$.result.memberName").value("홍진기"));
        }

    }

}
