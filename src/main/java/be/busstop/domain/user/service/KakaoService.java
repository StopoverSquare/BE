package be.busstop.domain.user.service;


import be.busstop.domain.user.dto.KakaoDto;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.jwt.JwtUtil;
import be.busstop.global.security.jwt.RefreshToken;
import be.busstop.global.security.jwt.RefreshTokenRedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final RefreshTokenRedisRepository redisRepository;
    private final JwtUtil jwtUtil;

    @Value("${kakao.login.callback.url}")
    private String kakaoCallbackUrl;

    @Value("${kakao.login.client.id}")
    private String kakaoLoginClientId;

    /**
     * Kakao 로그인 메서드
     *
     * @param code Kakao OAuth 서버로부터 받은 인증 코드
     * @return 사용자 객체
     * @throws JsonProcessingException JSON 파싱 오류가 발생할 경우 예외
     */
    @Transactional
    public User kakaoSignUpOrLinkUser(String code) throws JsonProcessingException {
        log.info("카카오 로그인 시도 중. 인증 코드: {}", code);

        // Kakao OAuth 서버로부터 액세스 토큰 얻기
        String accessToken = getToken(code);

        // Kakao 서버로부터 액세스 토큰 얻기 성공
        log.info("카카오 서버에서 토큰 받기 성공적. 액세스 토큰: {}", accessToken);

        // Kakao OAuth 서버로부터 사용자 정보 가져오기
        KakaoDto kakaoUserDto = getKakaoUserInfo(accessToken);

        // 기존 사용자 검색
        User user = userRepository.findByUserCode(kakaoUserDto.getUserCode()).orElse(null);

        // 기존 사용자가 없는 경우, 새로운 Kakao 사용자 등록
        if (user == null) {
            // 새로운 Kakao 사용자 등록 진행
            log.info("새로운 카카오 사용자 등록을 진행합니다.");
            user = registerKakaoUser(kakaoUserDto);
        } else {
            // 기존 사용자와 연결되는 Kakao 사용자로 등록
            log.info("기존 사용자와 연결되는 카카오 사용자로 등록합니다.");
        }

        return user;
    }

    /**
     * Kakao OAuth 서버로부터 인증 코드를 사용하여 액세스 토큰을 얻는 메서드
     *
     * @param code Kakao OAuth 서버로부터 받은 인증 코드
     * @return 액세스 토큰
     */
    public String getToken(String code) {
        // Kakao OAuth 서버의 토큰 요청 엔드포인트 URL
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/token";

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP 요청 본문 설정 - 액세스 토큰을 요청하는 파라미터들을 설정
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");  // 권한 부여 유형
        map.add("client_id", kakaoLoginClientId);     // 클라이언트 ID
        map.add("redirect_uri", kakaoCallbackUrl);     // 리다이렉션 URI
        map.add("code", code);                        // Kakao OAuth 서버로부터 받은 인증 코드

        // HTTP 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        // Kakao OAuth 서버에 액세스 토큰 요청을 보내고 응답을 받음
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoAuthUrl, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 성공적인 응답인 경우, JSON 형식의 응답 본문을 파싱하여 액세스 토큰을 반환
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } catch (JsonProcessingException e) {
                // JSON 파싱 오류가 발생한 경우 예외 처리
                log.error("Kakao API 응답 파싱 오류.", e);
                throw new RuntimeException("Kakao API 응답 파싱 오류.", e);
            }
        } else {
            log.error("Kakao 인증 코드를 액세스 토큰으로 교환하는 중 오류 발생. 응답: {}", response.getBody());
            throw new RuntimeException("Kakao 인증 코드를 액세스 토큰으로 교환하는 중 오류 발생.");
        }
    }

    /**
     * Kakao OAuth 서버에서 사용자 정보를 가져오는 메서드
     *
     * @param accessToken Kakao OAuth 서버로부터 받은 액세스 토큰
     * @return KakaoDto 객체, Kakao 사용자 정보를 포함
     * @throws JsonProcessingException JSON 파싱 오류가 발생할 경우 예외
     */
    private KakaoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // Kakao OAuth 서버의 사용자 정보를 가져오기 위한 엔드포인트 URI 생성
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP GET 요청 엔티티 생성
        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .headers(headers)
                .build();

        // Kakao OAuth 서버로부터 사용자 정보를 가져옴
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // Kakao OAuth 서버로부터 반환된 JSON 데이터를 파싱하여 사용자 정보 추출
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String usercode = String.valueOf(id);
        String profileImageUrl = jsonNode.get("properties").get("profile_image").asText();

        // 로깅을 통해 Kakao 사용자 정보를 기록
        log.info("카카오 사용자 정보: " + id + ", " + usercode );

        // KakaoDto 객체를 생성하여 사용자 정보를 담고 반환
        return new KakaoDto(id, usercode, profileImageUrl);
    }
    private int calculateAge(String birthday) {
        // 실제로는 생일 정보를 이용하여 연령대를 계산하는 로직을 추가해야 합니다.
        // 이 예시에서는 단순히 현재 년도에서 출생년도를 뺀 값을 반환합니다.
        int birthYear = Integer.parseInt(birthday.substring(0, 4));
        int currentYear = LocalDate.now().getYear();
        return currentYear - birthYear + 1;
    }


    @Transactional
    public User registerKakaoUser(KakaoDto kakaoUserDto) {
        User user = userRepository.findById(kakaoUserDto.getId()).orElse(null);
        if (user != null) {
            user.kakaoIdUpdate(kakaoUserDto);
        } else {
            String randomPwd = passwordEncoder.encode(String.valueOf(kakaoUserDto.getId()));
            String profileImageUrl = kakaoUserDto.getProfileImageUrl(); // 프로필 이미지 URL 가져오기
            user = new User(kakaoUserDto, randomPwd, profileImageUrl);// 프로필 이미지 URL로 User 객체 생성
            userRepository.save(user);
        }
        return user;
    }

    public ApiResponse<?> addToken(User user, HttpServletResponse response) {
        String token = jwtUtil.createToken(String.valueOf(user.getId()),user.getUserCode(),user.getNickname(),user.getAge(), user.getGender(), user.getRole(), user.getProfileImageUrl(),user.getInterest());
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(user.getId()),user.getUserCode(), user.getNickname(),user.getAge(), user.getGender(), user.getRole(), user.getInterest(), user.getProfileImageUrl());

        jwtUtil.addJwtHeaders(token, refreshToken, response);

        // refresh 토큰은 redis에 저장
        RefreshToken refresh = RefreshToken.builder()
                .id(user.getUserCode())
                .refreshToken(refreshToken)
                .build();
        log.info("리프레쉬 토큰 저장 성공. 유저 ID: {}", user.getId());
        redisRepository.save(refresh);
        return ApiResponse.success("토큰 발급 성공 !");
    }
}
