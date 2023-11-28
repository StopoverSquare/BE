package be.busstop.domain.post.dto;

import be.busstop.domain.post.entity.Category;
import be.busstop.domain.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {

    private Long id;
    private Long userId;
    private Category category;
    private String title;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrlList;
    private String StartDate;
    private Boolean isRecommend;
    private long Recommends;
    private int views;
    private String location;
    private String profileImageUrl;

    // 전체 조회
    @QueryProjection
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.category = post.getCategory();
        this.views = post.getViews();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.imageUrlList = post.getImageUrlList().stream().limit(1)
                .map(String::new)
                .collect(Collectors.toList());
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.location = post.getLocation();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
    }

    // 상세 조회
    public PostResponseDto(Post post, Boolean isRecommend){
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.category = post.getCategory();
        this.views = post.getViews();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.content = post.getContent();
        this.StartDate = post.getStartDate();
        this.createdAt = post.getCreatedAt();
        this.imageUrlList = post.getImageUrlList().stream()
                .map(String::new)
                .collect(Collectors.toList());
        this.location = post.getLocation();
        this.isRecommend = isRecommend;
        this.Recommends = post.getRecommends();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
    }
}

