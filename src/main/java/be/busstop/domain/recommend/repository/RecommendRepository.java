package be.busstop.domain.recommend.repository;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.recommend.entity.Recommend;
import be.busstop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    Optional<Recommend> findByPostAndUser(Post post, User user);
}
