package be.busstop.domain.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PostApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long turn;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private Long userId;
    private String nickname;
    private Integer age;
    private String gender;
    private String profileImageUrl;
    private Integer reportCount;

    public PostApplicant(Long userId, String nickname, String age, String gender, String profileImageUrl, Integer reportCount, Post post) {
        this.userId = userId;
        this.nickname = nickname;
        this.age = Integer.valueOf(age);
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.reportCount = reportCount;
        this.post = post;
    }
}
