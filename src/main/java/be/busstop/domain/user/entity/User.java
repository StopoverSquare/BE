package be.busstop.domain.user.entity;

import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
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
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private String roomId;
    @Column
    private String sessionId;

    @Builder
    public User(String nickname, String age, String gender, String password, String profileImageUrl, UserRoleEnum role) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    public void setRoomId(String roomId){
        this.roomId = roomId;
    }

}
