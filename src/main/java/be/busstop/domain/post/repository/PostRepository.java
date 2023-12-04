package be.busstop.domain.post.repository;


import be.busstop.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p left join fetch p.imageUrlList il where p.id = :postId")
    Optional<Post> findDetailPost(@Param("postId") Long postId);

}
