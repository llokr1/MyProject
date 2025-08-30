package project.project_spring.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.project_spring.user.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long memberId);
}
