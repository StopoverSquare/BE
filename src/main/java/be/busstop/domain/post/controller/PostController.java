package be.busstop.domain.post.controller;

import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.service.PostService;
import be.busstop.global.responseDto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPost(condition, pageable);
    }

    @PostMapping
    public ApiResponse<?> createPost(@Valid @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) List<MultipartFile> images) {
        return postService.createPost(postRequestDto, images);
    }
}
