package be.busstop.domain.post.controller;

import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.service.PostService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/random")
    public ApiResponse<?> getRandomPosts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         Pageable pageable) {
        return postService.getRandomPosts(pageable);
    }

    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPost(condition, pageable);
    }
    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId, HttpServletRequest req) {
        return postService.getSinglePost(postId, req);
    }
    @PostMapping
    public ApiResponse<?> createPost(@Valid @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file") List<MultipartFile> images,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.createPost(postRequestDto, images, userDetailsImpl.getUser());
    }
    @DeleteMapping ("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.deletePost(postId, userDetailsImpl.getUser());
    }
}
