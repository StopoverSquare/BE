package be.busstop.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String password;

    @Column()
    private String profileImageUrl;

    @Column(nullable = false)
    private Boolean social;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(String nickname, String password, String profileImageUrl, Boolean social, UserRoleEnum role) {
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.social = social;
        this.role = role;
    }

}
