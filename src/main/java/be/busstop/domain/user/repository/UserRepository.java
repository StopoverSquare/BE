package be.busstop.domain.user.repository;


import be.busstop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String username);

    User findBySessionId(String sessionId);
}
