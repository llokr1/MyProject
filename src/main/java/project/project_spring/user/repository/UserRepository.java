package project.project_spring.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.project_spring.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
