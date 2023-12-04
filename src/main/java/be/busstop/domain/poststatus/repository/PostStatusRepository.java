package be.busstop.domain.poststatus.repository;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.poststatus.entity.PostStatus;
import be.busstop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostStatusRepository extends JpaRepository<PostStatus, Long> {

    Optional<PostStatus> findByPostAndUser(Post post, User user);

}
