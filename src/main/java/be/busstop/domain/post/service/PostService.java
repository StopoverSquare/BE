package be.busstop.domain.post.service;

import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.entity.Post;
import be.busstop.domain.post.repository.PostRepository;
import be.busstop.domain.poststatus.repository.PostStatusRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.jwt.JwtUtil;
import be.busstop.global.utils.S3;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static be.busstop.global.stringCode.ErrorCodeEnum.*;
import static be.busstop.global.stringCode.SuccessCodeEnum.POST_CREATE_SUCCESS;
import static be.busstop.global.stringCode.SuccessCodeEnum.POST_DELETE_SUCCESS;
import static be.busstop.global.utils.ResponseUtils.ok;
import static be.busstop.global.utils.ResponseUtils.okWithMessage;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostStatusRepository postStatusRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final S3 s3;

    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return ok(postRepository.searchPostByPage(condition, pageable));
    }

    @Transactional
    public ApiResponse<?> getSinglePost(Long postId, HttpServletRequest req) {
        String token = jwtUtil.getAccessJwtFromHeader(req);
        String subStringToken;
        boolean isComplete = false;
        if (token != null) {
            subStringToken = jwtUtil.substringToken(token);
            Claims userInfo = jwtUtil.getUserInfoFromToken(subStringToken);
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("?"));
            User user = userRepository.findByNickname(userInfo.getSubject()).orElseThrow(() -> new IllegalArgumentException("?"));

            if (postStatusRepository.findByPostAndUser(post, user).isPresent()) {
                isComplete = true;
            }
        }
        Post post = postRepository.findDetailPost(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
        log.info("게시물 ID '{}' 조회 성공", postId);
        post.increaseViews();
        return ok(new PostResponseDto(post, isComplete));
    }

    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, List<MultipartFile> images, User user) {
        List<String> imageUrlList = s3.uploads(images);
        postRequestDto.setImageUrlList(imageUrlList);
        postRepository.save(new Post(postRequestDto, user, imageUrlList));
        log.info("'{}'님이 새로운 게시물을 생성했습니다.", user.getNickname());
        return okWithMessage(POST_CREATE_SUCCESS);
    }
    @Transactional
    public ApiResponse<?> deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        deleteImage(post);
        postRepository.delete(post);
        log.info("'{}'님이 게시물 ID '{}'를 삭제했습니다.", user.getNickname(), postId);
        return okWithMessage(POST_DELETE_SUCCESS);
    }

    private void deleteImage(Post post) {
        List<String> imageUrlList = post.getImageUrlList();
        if (StringUtils.hasText(String.valueOf(imageUrlList))) {
            s3.delete(imageUrlList);
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private Post confirmPost(Long postId, User user) throws InvalidConditionException {
        Post post = findPost(postId);
        log.info("Confirming post access: postId={}, user={}, postUser={}, userRole={}",
                postId, user.getId(), post.getUser().getId(), user.getRole());
        if (!user.getId().equals(post.getUser().getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new InvalidConditionException(USER_NOT_MATCH);
        }

        log.info("Post access confirmed: postId={}, user={}", postId, user.getId());
        return post;
    }

}
