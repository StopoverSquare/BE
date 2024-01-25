package be.busstop.domain.poststatus.entity;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_statuses")
public class PostStatus {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status; //

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = CASCADE)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PostStatus(Post post, User user) {
        this.post = post;
        this.user = user;
    }
    public void markInProgress() {
        this.status = Status.IN_PROGRESS;
    }

    public void markInBlocked() {this.status = Status.BLOCKED;}

    public void markCompleted() {
        this.status = Status.COMPLETED;
    }
}


