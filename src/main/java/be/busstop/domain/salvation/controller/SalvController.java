package be.busstop.domain.salvation.controller;

import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.salvation.dto.SalvRequestDto;
import be.busstop.domain.salvation.dto.SalvSearchCondition;
import be.busstop.domain.salvation.service.SalvService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "구제 관련 API", description = "구제글 작성 및 조회, 삭제")
@RestController
@RequestMapping("/api/salvation")
@RequiredArgsConstructor
public class SalvController {
    private final SalvService salvService;

    @Operation(summary = " 구제글 최신순으로 전체 조회 -> 관리자만 가능 ")
    @GetMapping
    public ApiResponse<?> searchSalvation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     SalvSearchCondition condition,
                                     Pageable pageable) {
        return salvService.searchSalvation(userDetails.getUser(), condition, pageable);
    }
    @Operation(summary = " 구제글 상세조회 -> 관리자만 가능 ")
    @GetMapping("/{salvationId}")
    public ApiResponse<?> readOneSalvation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long salvationId) {
        return salvService.getSingleSalvation(userDetails.getUser(),salvationId);
    }
    @Operation(summary = " 구제글 작성 -> 권한 필요 없음 ")
    @PostMapping
    public ApiResponse<?> createSalvation(@Valid @RequestPart(value = "data") SalvRequestDto salvRequestDto,
                                          @RequestPart(value = "file") List<MultipartFile> images) {
        return salvService.createSalvation(salvRequestDto, images);
    }
    @Operation(summary = " 유저 구제 및 글 삭제 -> 관리자만 가능 ")
    @DeleteMapping("/{salvationId}")
    public ApiResponse<?> salvationOk(@PathVariable Long salvationId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return salvService.salvationOk(salvationId, userDetailsImpl.getUser());
    }

}
