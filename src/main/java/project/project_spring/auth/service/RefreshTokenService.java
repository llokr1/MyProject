package project.project_spring.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.project_spring.auth.refresh.entity.RefreshToken;
import project.project_spring.auth.refresh.repository.RefreshTokenRepository;
import project.project_spring.common.exception.GeneralException;
import project.project_spring.common.response.ErrorCode;
import project.project_spring.member.domain.Member;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdateRefreshToken(Member member, String refreshToken){

        Long memberId = member.getId();

        // 없으면 null 처리
        RefreshToken storedRefreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElse(null);

        // null이라면 (없다면)
        if (storedRefreshToken == null){

            // 토큰 새로 생성
            RefreshToken newRefreshToken = RefreshToken.of(memberId, refreshToken);
            // 저장
            refreshTokenRepository.save(newRefreshToken);
            return;
        }
        // 존재한다면
        storedRefreshToken.setToken(refreshToken);
    }

    public RefreshToken findRefreshTokenByMemberId(Long memberId){

        RefreshToken member = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.MEMBER_NOT_FOUND));

        return member;
    }


}