package project.project_spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.project_spring.auth.jwt.JwtAuthenticationFilter;
import project.project_spring.auth.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        // 회원가입 경로 접근 허용
                        .requestMatchers("/signup").permitAll()
                        // 관리자 페이지는 관리자만 접근 가능하도록 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 다른 모든 경로에 인증을 요구
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        // 로그인 Form을 "/login" 페이지로 설정
                        .loginPage("/login")
                        // 로그인 성공 시 리디렉션 화면 설정
                        .defaultSuccessUrl("/main", true)
                        .permitAll())
                .logout(logout -> logout
                        // 로그아웃 URL 설정
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}