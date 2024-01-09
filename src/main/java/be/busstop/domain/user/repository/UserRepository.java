package be.busstop.domain.user.repository;


import be.busstop.domain.post.entity.Post;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);

    Optional<User> findByUserCode(String userCode);
    Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
    Page<User> findByNicknameContainingIgnoreCaseAndRole(String nickname, UserRoleEnum role, Pageable pageable);


    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findPostsByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    Page<User> findAllByBlackUser(@Param("role") UserRoleEnum BLACK, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByUser(@Param("role") UserRoleEnum USER);

    boolean existsByNickname(String nickname);

    List<User> findAllByCreatedAt(LocalDateTime createdAt);
}
