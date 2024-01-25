package be.busstop.domain.post.entity;

import be.busstop.domain.post.dto.BlockedPostDto;
import be.busstop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BlockedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
    private String content;
    public BlockedPost(BlockedPostDto blockedPostDto, Post post, User admin) {
        this.post = post;
        this.admin = admin;
        this.content = blockedPostDto.getContent();
    }
}

