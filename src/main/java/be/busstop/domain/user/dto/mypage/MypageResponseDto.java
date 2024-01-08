package be.busstop.domain.user.dto.mypage;

import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.entity.Category;
import be.busstop.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class MypageResponseDto {

    private Long userId;
    private String nickname;
    private String age;
    private String gender;
    private String profileImageUrl;
    private Category interest;
    private List<PostResponseDto> userPosts;
    private Double mannerTemplate;


    public MypageResponseDto(Long userId, String nickname, String age, String gender, String profileImageUrl, Category interest, Double mannerTemplate, List<Post> userPosts) {
        this.userId = userId;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.interest = interest;
        this.mannerTemplate = mannerTemplate;
        // 사용자의 게시물 리스트를 PostResponseDto 리스트로 변환하여 설정합니다.
        this.userPosts = userPosts.stream()
                .map(post -> new PostResponseDto(
                        post.getId(), post.getUser().getId(),
                        post.getCategory(), post.getStatus().name(),
                        post.getTitle(), post.getUser().getNickname(),
                        post.getUser().getAge(), post.getUser().getGender(),post.getLocation(),post.getLocationDetail(),
                        post.getThumbnailImageUrl(),post.getUser().getProfileImageUrl(),
                        post.getViews(), post.getCreatedAt()))
                .collect(Collectors.toList());

    }
}
