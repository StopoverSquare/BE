package be.busstop.domain.post.service;


import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.entity.Post;
import be.busstop.domain.post.repository.PostRepository;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.SuccessCodeEnum;
import be.busstop.global.utils.S3;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static be.busstop.global.utils.ResponseUtils.ok;
import static be.busstop.global.utils.ResponseUtils.okWithMessage;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3 s3;

    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return ok(postRepository.searchPostByPage(condition, pageable));
    }

    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, List<MultipartFile> images) {
        List<String> imageUrlList = s3.uploads(images);
        postRequestDto.setImageUrlList(imageUrlList);
        postRepository.save(new Post(postRequestDto, imageUrlList));
        log.info("새로운 게시물을 생성했습니다.");
        return okWithMessage(SuccessCodeEnum.POST_CREATE_SUCCESS);
    }
}
