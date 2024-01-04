package be.busstop.domain.post.entity;

import be.busstop.domain.post.dto.BlockedPostDto;
import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.poststatus.entity.Status;
import be.busstop.domain.user.entity.User;
import be.busstop.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostApplicant> applicants = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "age", nullable = false)
    private String age;

    @Column(name = "gender", nullable = false)
    private String gender;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String location;

    @Column
    private String locationDetail;

    @Column
    private String endDate;

    @Column
    private String endTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @BatchSize(size = 5)
    @Column
    private List<String> imageUrlList = new ArrayList<>(); // 이미지 URL 리스트

    private String thumbnailImageUrl;

    private int views;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    @OneToMany(mappedBy = "post")
    private List<BlockedPost> blockedPosts = new ArrayList<>();

    @Column
    private String chatroomId;


    public Post(PostRequestDto postRequestDto, List<String> imageUrlList, User user, String chatroomId) {
        this.user = user;
        this.category = postRequestDto.getCategory();
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.location = postRequestDto.getLocation();
        this.locationDetail = postRequestDto.getLocationDetail();
        this.endDate = postRequestDto.getEndDate();
        this.endTime = postRequestDto.getEndTime();
        this.nickname = this.user.getNickname();
        this.age = this.user.getAge();
        this.gender = this.user.getGender();
        this.profileImageUrl = this.user.getProfileImageUrl();
        this.imageUrlList = imageUrlList;
        this.thumbnailImageUrl = postRequestDto.getThumbnailImageUrl();
        this.chatroomId = chatroomId;

    }

    public void increaseViews() {
        this.views++;
    }

    public void removeApplicant(String applicantNickname) {
        applicants.removeIf(applicant -> applicant.getNickname().equals(applicantNickname));
    }

    public boolean isUserAlreadyApplicant(String applicantNickname) {
        return applicants.stream()
                .anyMatch(applicant -> applicant.getNickname().equals(applicantNickname));
    }

    public void markInProgress() {
        this.status = Status.IN_PROGRESS;
        // 차단이 해제될 때 [차단 된 게시물]이라는 문자열을 title에서 제거
        if (this.title != null && this.title.startsWith("[차단 된 게시물] ")) {
            this.title = this.title.substring("[차단 된 게시물] ".length());
        }
    }

    public void markClosed() {
        this.status = Status.COMPLETED;
    }
    public void markBlocked() {
        this.status = Status.BLOCKED;
        this.title = "[차단 된 게시물] " + this.title;
    }
}
