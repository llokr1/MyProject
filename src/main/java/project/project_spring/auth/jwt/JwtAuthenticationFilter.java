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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenProvider.getTokenFromRequest(request);

        if(StringUtils.hasText(token)){
            jwtTokenProvider.validateJwtToken(token);

            Authentication authentication = jwtTokenProvider.getAuthenticationFromToken(token);

            if (authentication != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("사용자 정보 저장 완료 : {}", authentication.getName());
            } else {
                log.debug("JWT 토큰이 없거나 유효하지 않습니다.");
            }
        }
        filterChain.doFilter(request, response);
    }
}
