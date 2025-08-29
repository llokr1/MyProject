package project.project_spring.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.project_spring.auth.jwt.JwtTokenProvider;
import project.project_spring.auth.refresh.entity.RefreshToken;
import project.project_spring.auth.refresh.repository.RefreshTokenRepository;
import project.project_spring.user.domain.Member;
import project.project_spring.user.service.MemberService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveRefreshToken(Member member, String refreshToken){

        Long userId = member.getId();

        RefreshToken newRefreshToken = RefreshToken.of(userId, refreshToken);

        refreshTokenRepository.save(newRefreshToken);
    }



}