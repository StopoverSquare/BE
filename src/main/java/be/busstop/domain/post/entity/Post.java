package be.busstop.domain.post.entity;

import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.user.entity.User;
import be.busstop.global.utils.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String location;

    @Column
    private String StartDate;

    @ElementCollection
    @BatchSize(size = 5)
    @Column
    private List<String> imageUrlList = new ArrayList<>(); // 이미지 URL 리스트


    private int views;

    private long reportCount;

    private String profileImageUrl;

    public Post(PostRequestDto postRequestDto, User user, List<String> imageUrlList ) {
        this.user = user;
        this.category = postRequestDto.getCategory();
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.location = postRequestDto.getLocation();
        this.StartDate = postRequestDto.getStartDate();
        this.nickname = this.user.getNickname();
        this.imageUrlList = imageUrlList;
        this.profileImageUrl = this.user.getProfileImageUrl();

    }

    public void increaseViews() {
        this.views++;
    }


}
