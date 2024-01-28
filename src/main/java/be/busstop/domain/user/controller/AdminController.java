package be.busstop.domain.user.controller;

import be.busstop.domain.user.service.AdminPageService;
import be.busstop.global.responseDto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 API", description = "관리자 관리")
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {
    private final AdminPageService adminPageService;
    @GetMapping("/search")
    public ApiResponse<?> searchNickname(@RequestParam("nickname") String nickname){
        return adminPageService.searchNickname(nickname);
    }
}
