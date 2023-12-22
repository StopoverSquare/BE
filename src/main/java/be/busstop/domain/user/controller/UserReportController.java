package be.busstop.domain.user.controller;

import be.busstop.domain.user.dto.UserReportDto;
import be.busstop.domain.user.service.UserReportService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserReportController {

    private final UserReportService userReportService;

    @PostMapping("/report/{userId}")
    public ApiResponse<?> reportUser(@PathVariable Long userId,
                                     @RequestPart(value = "data") UserReportDto userReportDto,
                                     @RequestPart(value = "file") List<MultipartFile> images,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return userReportService.reportUser(userDetailsImpl.getUser(),userId, userReportDto, images);
    }
}
