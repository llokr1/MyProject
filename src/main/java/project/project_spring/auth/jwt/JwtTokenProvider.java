package project.project_spring.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final String AUTH_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    @Value("${jwt.access.secretKey}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long expiration;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 사용자 정보를 바탕으로 토큰 생성
    public String generateToken(Authentication authentication){
        // Authentication 객체로부터 사용자 정보를 가져온다.
        String email = authentication.getName();
        // Authorities의 두 번째 값이 role 값이다.
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        log.info("토큰 생성 완료");
        return Jwts.builder()
                // 토큰 제목 설정
                .setSubject(email)
                // Payload의 role claim 정보를 입력
                .claim("role", role)
                // 발급 날짜 설정
                .setIssuedAt(new Date())
                // 만료 기한 설정
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                // SecretKey로 서명
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // Jwt 토큰으로 직렬화
                .compact();
    }

    // 토큰의 유효성 검사
    public void validateToken(String token){

        try{
            if (token == null){
                throw new JwtException("토큰이 존재하지 않습니다.");
            }

            Claims claims = parseClaimsFromToken(token);

            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                throw new JwtException("토큰 기한 만료");
            }
        } catch(Exception e){
            log.warn("토큰이 유효하지 않습니다 : {}", token);
        }
    }


    // Jwt 토큰으로부터 Spring Security의 Authentication 객체를 추출
    public Authentication getAuthenticationFromToken(String token){

        // 유효성 검사
        validateToken(token);

        Claims claims = parseClaimsFromToken(token);

        String email = claims.getSubject();
        String role =claims.get("role", String.class);

        User principal = new User(email, "", Collections.singleton(() -> role));
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    public String getTokenFromRequest(HttpServletRequest request){

        String accessToken = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(TOKEN_PREFIX)){
            return accessToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public Claims parseClaimsFromToken(String token){
        return  // 파서 객체 생성
                Jwts.parserBuilder()
                        // SecretKey를 통해 Signature 검증
                        .setSigningKey(getSigningKey())
                        .build()
                        // token 파싱, 검증 및 Base64 디코딩
                        .parseClaimsJws(token)
                        .getBody();
    }

}