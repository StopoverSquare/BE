package be.busstop.domain.post.entity;

import be.busstop.domain.post.dto.BlockedPostDto;
import be.busstop.domain.user.entity.User;
import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BlockedPost extends Timestamped {
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
    public BlockedPost( Post post, User admin) {
        this.post = post;
        this.admin = admin;
    }
}

