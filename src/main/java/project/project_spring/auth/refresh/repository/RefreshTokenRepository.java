package project.project_spring.auth.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.project_spring.auth.refresh.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long userId);

}
