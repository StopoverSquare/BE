package be.busstop.domain.user.controller;

import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.service.KakaoService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.SuccessCodeEnum;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @Transactional
    @PostMapping("/kakao")
    public ApiResponse<?> kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        log.info("카카오 로그인 콜백 요청 받음. 인증 코드: {}", code);

        // 카카오 로그인에 성공한 후, 사용자 정보 가져오기
        User user = kakaoService.kakaoSignUpOrLinkUser(code);
        log.info("카카오 로그인 성공. 유저 ID: {}", user.getId());
        kakaoService.addToken(user, response);
        return ApiResponse.okWithMessage(SuccessCodeEnum.USER_LOGIN_SUCCESS);
    }
}
