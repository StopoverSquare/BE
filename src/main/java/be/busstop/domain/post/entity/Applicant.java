package be.busstop.domain.post.entity;

import be.busstop.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
