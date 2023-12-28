package be.busstop.domain.post.dto;

import be.busstop.domain.post.entity.Category;
import be.busstop.domain.post.entity.Post;
import be.busstop.domain.post.entity.PostApplicant;
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
    private String age;
    private String gender;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrlList;
    private String endDate;
    private String endTime;
    private String status;
    private Boolean isComplete;
    private Boolean isAlreadyApplicant;
    private int views;
    private String location;
    private String locationDetail;
    private String profileImageUrl;
    private String chatroomId;
    private List<String> chatParticipants;
    private List<PostApplicant> applicants;

    // 전체 조회
    @QueryProjection
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.category = post.getCategory();
        this.views = post.getViews();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.age = post.getUser().getAge();
        this.gender = post.getUser().getGender();
        this.imageUrlList = post.getImageUrlList().stream().limit(1)
                .map(String::new)
                .collect(Collectors.toList());
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.endDate = post.getEndDate();
        this.endTime = post.getEndTime();
        this.location = post.getLocation();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
    }

    // 상세 조회
    public PostResponseDto(Post post, Boolean isComplete,Boolean isAlreadyApplicant, List<String> chatParticipants, List<PostApplicant> applicants){
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.category = post.getCategory();
        this.views = post.getViews();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.age = post.getUser().getAge();
        this.gender = post.getUser().getGender();
        this.content = post.getContent();
        this.endDate = post.getEndDate();
        this.endTime = post.getEndTime();
        this.createdAt = post.getCreatedAt();
        this.imageUrlList = post.getImageUrlList().stream()
                .map(String::new)
                .collect(Collectors.toList());
        this.location = post.getLocation();
        this.locationDetail = post.getLocationDetail();
        this.profileImageUrl = post.getUser().getProfileImageUrl();
        this.isComplete = isComplete;
        if (post.getStatus() != null) {
            this.status = post.getStatus().name();
        }
        this.isAlreadyApplicant = isAlreadyApplicant;
        this.chatroomId = post.getChatroomId();
        this.chatParticipants = chatParticipants;
        this.applicants = applicants;
    }

    // 랜덤 조회
    public PostResponseDto(Long id, Long userId, Category category, String title,
                               String nickname, String age, String gender,
                               LocalDateTime createdAt, List<String> imageUrlList,
                               String endDate,String endTime, String location, String profileImageUrl) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.title = title;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.createdAt = createdAt;
        this.imageUrlList = imageUrlList.stream().limit(1)
                .map(String::new)
                .collect(Collectors.toList());
        this.endDate = endDate;
        this.endTime = endTime;
        this.location = location;
        this.profileImageUrl = profileImageUrl;
    }
    // 마이페이지
    public PostResponseDto(Long id, Long userId, Category category,String status, String title,
                           String nickname, String age, String gender,
                       String location,String locationDetail, List<String> imageUrlList, String profileImageUrl,int views, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.status = status;
        this.title = title;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.locationDetail = locationDetail;
        this.imageUrlList = imageUrlList.stream().limit(1)
                .map(String::new)
                .collect(Collectors.toList());
        this.profileImageUrl = profileImageUrl;
        this.views = views;
        this.createdAt = createdAt;
    }
}

