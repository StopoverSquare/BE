package be.busstop.domain.user.entity;

import be.busstop.domain.post.entity.Category;
import be.busstop.domain.user.dto.KakaoDto;
import be.busstop.domain.user.dto.mypage.DetailRequestDto;
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

    @Column
    private String age;

    @Column
    private String gender;

    @Column(nullable = false)
    private String password;

    @Column()
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category interest;

    private Integer reportCount;

    private Long mannerTemplate;

    @Column
    private String roomId;
    @Column
    private String sessionId;

    @Builder
    public User(String nickname, String age, String gender, String password, String profileImageUrl, UserRoleEnum role, Category interest) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.interest = interest;
    }

    @Builder
    public User(KakaoDto kakaoDto, String password, String profileImageUrl) {
        this.nickname = kakaoDto.getNickname();
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = UserRoleEnum.USER;
    }
    public void updateNickname(String nickname, Category interest) {
        this.nickname = nickname;
        this.interest = interest;
    }
    public void update(DetailRequestDto detailRequestDto) {
        this.nickname = detailRequestDto.getNickname();
        this.interest = Category.valueOf(detailRequestDto.getInterest());
        this.age = detailRequestDto.getAge();
        this.gender = detailRequestDto.getGender();
    }
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
    public void setRoomId(String roomId){
        this.roomId = roomId;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public User kakaoIdUpdate(KakaoDto kakaoDto){
        return this;
    }

    public void setRole(UserRoleEnum userRoleEnum) {
        this.role = userRoleEnum;
    }


    public void increaseReportCount() {
        if (this.reportCount == null) {
            this.reportCount = 0;
        }
        this.reportCount++;
    }

}
