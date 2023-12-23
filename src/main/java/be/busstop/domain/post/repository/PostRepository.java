package be.busstop.domain.post.repository;


import be.busstop.domain.post.entity.Post;
import be.busstop.domain.poststatus.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select p from Post p left join fetch p.imageUrlList il where p.id = :postId")
    Optional<Post> findDetailPost(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.imageUrlList " +
            "WHERE p.id = :postId")
    Optional<Post> findDetailPostWithParticipants(@Param("postId") Long postId);


    Post findByChatroomId(String roomId);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.status = :newStatus WHERE p.id = :postId")
    void updateByStatus(@Param("postId") Long postId, @Param("newStatus") Status newStatus);
}
