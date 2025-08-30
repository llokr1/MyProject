package project.project_spring.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
// 요청(request) 당 한 번 실행되는 OncePerRequestFilter를 상속
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 실제 인증 로직이 들어가는 메소드이다.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 요청에서 토큰을 추출한다.
        String token = jwtTokenProvider.getTokenFromRequest(request);


        if(StringUtils.hasText(token)) {

            /*
             토큰 재발급 경로는 인증이 필요 없음
              :이미 인증이 안돼서 토큰을 재발급 받는 것이기 때문
            */
            if (!request.getRequestURI().equals("/api/reissue")) {
                
                 // 토큰을 검증한다.
                jwtTokenProvider.validateJwtToken(token);
                
                // 토큰을 바탕으로 Authentication 객체를 생성한다.
                Authentication authentication = jwtTokenProvider.getAuthenticationFromToken(token);

                if (authentication != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {
                    //SecurityContextHolder에 추가한다.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("사용자 정보 저장 완료 : {}", authentication.getName());
                } else {
                    log.debug("JWT 토큰이 없거나 유효하지 않습니다.");
                }
            }
        }
        // 다음 필터로 요청 정보와 응답 정보를 넘긴다.
        filterChain.doFilter(request, response);
    }
}