package be.busstop.domain.post.repository;


import be.busstop.domain.post.entity.Post;
import be.busstop.domain.poststatus.entity.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @EntityGraph(attributePaths = {"applicants"})
    Optional<Post> findById(Long id);


    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.status = :newStatus WHERE p.id = :postId")
    void updateByStatus(@Param("postId") Long postId, @Param("newStatus") Status newStatus);

}
