package be.busstop.domain.recommend.service;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.post.repository.PostRepository;
import be.busstop.domain.recommend.entity.Recommend;
import be.busstop.domain.recommend.repository.RecommendRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.busstop.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final PostRepository postRepository;
    public ApiResponse<?> updateRecommend(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));

        String nickname = user.getNickname();
        String postTitle = post.getTitle();   // 게시물 제목 가져오기

        if (!isRecommendedPost(post, user)) {
            createRecommend(post, user);
            post.increaseRecommend();
            log.info("'{}'님이 '{}'에 관심을 추가했습니다.", nickname, postTitle);
        } else {
            removeRecommend(post, user);
            post.decreaseRecommend();
            log.info("'{}'님이 '{}'의 관심을 취소했습니다.", nickname, postTitle);
        }
        String statusMessage = isRecommendedPost(post,user) ? "관심을 추가했습니다." : "관심을 취소했습니다.";
        return ApiResponse.success(statusMessage);
    }

    private boolean isRecommendedPost(Post post, User user) {
        return recommendRepository.findByPostAndUser(post, user).isPresent();
    }

    private void createRecommend(Post post, User user) {
        Recommend recommend = new Recommend(post, user);
        recommendRepository.save(recommend);
    }

    private void removeRecommend(Post post, User user) {
        Recommend recommend = recommendRepository.findByPostAndUser(post, user).orElseThrow();
        recommendRepository.delete(recommend);
    }
}
