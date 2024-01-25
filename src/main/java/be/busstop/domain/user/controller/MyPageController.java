package be.busstop.domain.user.controller;

import be.busstop.domain.user.dto.mypage.CheckDuplicateNicknameDto;
import be.busstop.domain.user.dto.mypage.DetailRequestDto;
import be.busstop.domain.user.dto.mypage.NicknameRequestDto;
import be.busstop.domain.user.service.MyPageService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static be.busstop.global.responseDto.ApiResponse.success;

@Tag(name = "마이페이지 API", description = "사용자 정보 조회 및 수정")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    @Operation(summary = "마이페이지 조회")
    @GetMapping("/mypage/{userid}")
    public ApiResponse<?> userDetailView(@PathVariable Long userid) {
            return success(myPageService.getCurrentUserDetails(userid));
    }
    @Operation(summary = "닉네임, 관심사 변경")
    @PutMapping("mypage/{userId}/nickname")
    public ApiResponse<?> updateUserNickname(@PathVariable Long userId,
                                            @Valid @RequestBody NicknameRequestDto nicknameRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                            HttpServletResponse response) {
        return myPageService.updateNickname(userId, nicknameRequestDto, userDetailsImpl.getUser(), response);
    }
    @Operation(summary = "닉네임, 나이, 성별, 관심사 전부 최초등록")
    @PutMapping("mypage/{userId}/detail")
    public ApiResponse<?> updateUserDetail(@PathVariable Long userId,
                                            @Valid @RequestBody DetailRequestDto detailRequestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                            HttpServletResponse response) {
        return myPageService.updateUserDetails(userId, detailRequestDto, userDetailsImpl.getUser(), response);
    }
    @Operation(summary = "프로필 이미지 변경")
    @PutMapping("mypageImage/{userId}")
    public ApiResponse<?> updateUserProfileImage(@PathVariable Long userId,
                                                 @RequestPart(value = "file", required = false) MultipartFile image,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                                 HttpServletResponse response) {
        return myPageService.updateUserProfileImage(userId, image, userDetailsImpl.getUser(), response);
        }
    @Operation(summary = "닉네임 중복 확인")
    @PostMapping("mypage/nickname/check")
    public ApiResponse<?> checkDuplicatedNickname(@RequestBody CheckDuplicateNicknameDto checkDuplicateNicknameDto){
        return myPageService.checkDuplicatedNickname(checkDuplicateNicknameDto);
    }

    @Operation(summary = "별점 측정")
    @PostMapping("mypage/star/{userId}/{score}")
    public ApiResponse<?> evaluateUser(@PathVariable Long userId, @PathVariable Long score){
        return myPageService.evaluateUser(userId, score);
    }
    }


