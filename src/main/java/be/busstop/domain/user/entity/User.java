package be.busstop.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column()
    private String profileImageUrl;

    @Column(nullable = false)
    private Boolean social;

    @Builder
    private User(String password, String nickname, String profileImageUrl, Boolean social) {
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.social = social;
    }
}
