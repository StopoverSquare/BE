package be.busstop.domain.post.controller;

import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.service.PostService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import be.busstop.global.utils.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "게시글 관련 API", description = "게시글 작성 및 조회, 수정, 삭제")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 랜덤 조회 ?size={갯수}")
    @GetMapping("/random")
    public ApiResponse<?> getRandomPosts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         Pageable pageable) {
        return postService.getRandomPosts(pageable);
    }

    @Operation(summary = "게시글 최신순으로 전체 조회")
    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPost(condition, pageable);
    }

    @Operation(summary = "게시글 상세조회")
    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId, HttpServletRequest req) {
        return postService.getSinglePost(postId, req);
    }

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ApiResponse<?> createPost(@Valid @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file") List<MultipartFile> images,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.createPost(postRequestDto, images, userDetailsImpl.getUser());
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ApiResponse<?> deletePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.deletePost(postId, userDetailsImpl.getUser());
    }

    @Operation(summary = "게시글 참여자 승인")
    @Transactional
    @PostMapping("/{postId}/applicants/{userId}")
    public ApiResponse<?> approveParticipant(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable Long postId,
                                             @PathVariable Long userId) {
        return postService.approveParticipant(userDetails.getUser(), postId, userId);
    }

    @Operation(summary = "게시글 참여자 거부")
    @Transactional
    @DeleteMapping ("/{postId}/applicants/{userId}")
    public ApiResponse<?> denyParticipant(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable Long postId,
                                             @PathVariable Long userId) {
        return postService.denyParticipant(userDetails.getUser(), postId, userId);
    }

    @Operation(summary = "게시글 참여신청")
    @Transactional
    @PostMapping("/{postId}/applicants")
    public ApiResponse<?> addApplicant(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                       @PathVariable Long postId) {
        return postService.addApplicant(userDetailsImpl.getUser(), postId);
    }
}
