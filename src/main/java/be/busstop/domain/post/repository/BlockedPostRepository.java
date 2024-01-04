package be.busstop.domain.post.repository;

import be.busstop.domain.post.entity.BlockedPost;
import be.busstop.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface BlockedPostRepository extends JpaRepository <BlockedPost, Long> {
    Optional<BlockedPost> findByPost(Post post);

}
