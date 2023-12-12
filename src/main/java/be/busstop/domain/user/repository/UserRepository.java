package be.busstop.domain.user.repository;


import be.busstop.domain.post.entity.Post;
import be.busstop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String username);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findPostsByUserId(@Param("userId") Long userId);

    User findBySessionId(String sessionId);
}
