package be.busstop.domain.user.controller;

import be.busstop.domain.user.dto.UserReportDto;
import be.busstop.domain.user.service.AdminPageService;
import be.busstop.domain.user.service.UserReportService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Tag(name = "관리자기능 및 신고 API", description = "신고 및 전체 유저, 신고된 유저 조회")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;
    private final AdminPageService adminPageService;
    @Operation(summary = "특정 유저 신고")
    @PostMapping("/report/{userId}")
    public ApiResponse<?> reportUser(@PathVariable Long userId,
                                     @RequestPart(value = "data") UserReportDto userReportDto,
                                     @RequestPart(value = "file") List<MultipartFile> images,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return userReportService.reportUser(userDetailsImpl.getUser(),userId, userReportDto, images);
    }

    @Operation(summary = "관리자권한 부여 = 관리자계정만 권한부여 가능")
    @Transactional
    @PostMapping("/admin/{userId}")
    public ApiResponse<?> makeUserAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails
                                        ,@PathVariable Long userId) {
        return adminPageService.makeUserAdmin(userDetails.getUser(), userId);
    }

    @Operation(summary = "유저 권한 정지")
    @Transactional
    @PostMapping("/black/{userId}")
    public ApiResponse<?> makeUserBlack(@AuthenticationPrincipal UserDetailsImpl userDetails
                                       ,@PathVariable Long userId) {
        return adminPageService.makeUserBlack(userDetails.getUser(), userId);
    }

    @Operation(summary = "BLACK 유저 -> USER 유저로 변경")
    @Transactional
    @PostMapping("/user/{userId}")
    public ApiResponse<?> makeUser(@AuthenticationPrincipal UserDetailsImpl userDetails
                                   ,@PathVariable Long userId) {
        return adminPageService.makeUser(userDetails.getUser(), userId);
    }
    @Operation(summary = "일반유저 조회")
    @GetMapping("/userList")
    public ApiResponse<?> userList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
            return adminPageService.userList(userDetails.getUser());
    }
    @Operation(summary = "BLACK 유저 조회")
    @GetMapping("/blackUser")
    public ApiResponse<?> blackUserList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
            return adminPageService.userBlackList(userDetails.getUser());
    }
    @Operation(summary = "신고된 유저들 조회")
    @GetMapping("/reportList")
    public ApiResponse<?> searchAllUserReports(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) String nickname) {
        return ApiResponse.success(adminPageService.searchAllUserReports(userDetails.getUser(), nickname));
    }

    @Operation(summary = "신고된 유저 상세조회")
    @GetMapping("/reportList/{userId}")
    public ApiResponse<?> getUserReportDetail (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable Long userId){
        return ApiResponse.success(adminPageService.getUserReportDetail(userDetails.getUser(),userId));
    }
}
