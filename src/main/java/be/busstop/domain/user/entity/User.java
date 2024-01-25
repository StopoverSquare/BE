package be.busstop.domain.user.entity;

import be.busstop.domain.post.entity.Category;
import be.busstop.domain.user.dto.KakaoDto;
import be.busstop.domain.user.dto.mypage.DetailRequestDto;
import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String userCode;

    @Column(unique = true)
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

    @Column
    @Enumerated(value = EnumType.STRING)
    private Category interest;

    private Integer reportCount;

    private Double mannerTemplate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @Column
    private String roomId;
    @Column
    private String sessionId;

    @Builder
    public User(String nickname, String userCode, String age, String gender, String password, String profileImageUrl,Double mannerTemplate, UserRoleEnum role, Category interest, Integer reportCount) {
        this.userCode = userCode;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.interest = interest;
        this.mannerTemplate = mannerTemplate;
        this.lastAccessed = LocalDateTime.now();
        this.reportCount = reportCount;
    }

    @Builder
    public User(KakaoDto kakaoDto, String password, String profileImageUrl) {
        this.userCode = kakaoDto.getUserCode();
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.role = UserRoleEnum.USER;
        this.mannerTemplate = 36.5;
        this.reportCount = 0;
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

    public void setRoleBlack() {
        this.role = UserRoleEnum.BLACK;
    }
    public void setRoleUser() {
        this.role = UserRoleEnum.USER;
    }
    public void setRoleAdmin() {
        this.role = UserRoleEnum.ADMIN;
    }


    public void setMannerTemplate(Double mannerTemplate) {
        this.mannerTemplate = mannerTemplate;
    }

    public void increaseReportCount() {
        if (this.reportCount == null) {
            this.reportCount = 0;
        }
        this.reportCount++;
    }
    public void updateLastAccessed() {
        this.lastAccessed = LocalDateTime.now();
    }
}
