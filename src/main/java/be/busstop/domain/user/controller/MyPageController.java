package be.busstop.domain.user.controller;

import be.busstop.domain.user.dto.mypage.MypageRequestDto;
import be.busstop.domain.user.service.MyPageService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static be.busstop.global.responseDto.ApiResponse.success;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/mypage/{userid}")
    public ApiResponse<?> userDetailView(@PathVariable Long userid) {
            return success(myPageService.getCurrentUserDetails(userid));
        }

    @PutMapping("mypage/{userId}")
    public ApiResponse<?> updateUserProfile(@PathVariable Long userId,
                                            @Valid @RequestBody MypageRequestDto mypageRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                            HttpServletResponse response) {
        return myPageService.updateUserProfile(userId, mypageRequestDto, userDetailsImpl.getUser(), response);
        }

    @PutMapping("mypageImage/{userId}")
    public ApiResponse<?> updateUserProfileImage(@PathVariable Long userId,
                                                 @RequestPart(value = "file", required = false) MultipartFile image,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 HttpServletResponse response) {
        return myPageService.updateUserProfileImage(userId, image, userDetailsImpl.getUser(), response);
        }
    }
