package be.busstop.domain.user.entity;

import be.busstop.domain.user.dto.KakaoDto;
import be.busstop.domain.user.dto.mypage.DetailRequestDto;
import be.busstop.domain.user.dto.mypage.NicknameRequestDto;
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

    @Builder
    public User(KakaoDto kakaoDto, String password, String profileImageUrl) {
        this.nickname = kakaoDto.getNickname();
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = UserRoleEnum.USER;
    }

    public void update(DetailRequestDto detailRequestDto) {
      this.age = detailRequestDto.getAge();
      this.gender = detailRequestDto.getGender();
    }
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    public void setRoomId(String roomId){
        this.roomId = roomId;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public User kakaoIdUpdate(KakaoDto kakaoDto){
        return this;
    }

}
