package be.busstop.global.security.jwt;

import be.busstop.domain.post.entity.Category;
import be.busstop.domain.user.dto.LoginRequestDto;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.redis.RedisService;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.responseDto.ErrorResponse;
import be.busstop.global.security.UserDetailsImpl;
import be.busstop.global.stringCode.ErrorCodeEnum;
import be.busstop.global.stringCode.SuccessCodeEnum;
import be.busstop.global.utils.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static be.busstop.global.utils.ResponseUtils.customError;
import static be.busstop.global.utils.ResponseUtils.okWithMessage;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRedisRepository redisRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRedisRepository redisRepository, UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.redisRepository = redisRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            LoginRequestDto loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            UserDetailsImpl userDetails = loadUserByUsername(loginRequestDto.getNickname());

            if (userDetails == null) {
                sendErrorResponse(response, "닉네임이 올바르지 않습니다.", HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }

            if (userDetails.getUser().getRole() == UserRoleEnum.BLACK) {
                sendErrorResponse(response, "관리자에 의해 정지된 계정입니다.", HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }


            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getNickname(),
                            loginRequestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("로그인 시도 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .error(new ErrorResponse(errorMessage, statusCode))
                .build();

        String jsonResponse;
        try {
            jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
            response.getWriter().write(jsonResponse);
        } catch (IOException ex) {
            log.error("에러 메시지 JSON 변환 중 예외 발생: {}", ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }


    private boolean isPasswordValid(String password, String encodedPassword) {

        return passwordEncoder.matches(password, encodedPassword);
    }

    private UserDetailsImpl loadUserByUsername(String nickname) {

        return userRepository.findByNickname(nickname)
                .map(UserDetailsImpl::new)
                .orElse(null);
    }


    /**
     * 로그인 성공 시 JWT를 생성하여 응답에 추가합니다.
     *
     * @param request    HttpServletRequest 객체
     * @param response   HttpServletResponse 객체
     * @param chain      FilterChain 객체
     * @param authResult 인증 결과 Authentication 객체
     * @throws IOException      입출력 예외가 발생한 경우
     * @throws ServletException Servlet 예외가 발생한 경우
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        ObjectMapper objectMapper = new ObjectMapper();

        // 사용자 정보 가져오기
        String userCode = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        String nickname = ((UserDetailsImpl) authResult.getPrincipal()).getNickname();
        Long userId = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getId();
        String age = (((UserDetailsImpl) authResult.getPrincipal()).getUser().getAge());
        String gender = (((UserDetailsImpl) authResult.getPrincipal()).getUser().getGender());
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();
        String profileImageUrl = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getProfileImageUrl();
        String interest = String.valueOf(((UserDetailsImpl) authResult.getPrincipal()).getUser().getInterest());

        // 카카오 로그인의 경우 username에 카카오 이메일 정보가 담겨있을 것이므로 해당 값을 그대로 사용

        String token = jwtUtil.createToken(String.valueOf(userId),userCode, nickname, age, gender, role, profileImageUrl, Category.valueOf(interest));
        String refreshToken = jwtUtil.createRefreshToken(String.valueOf(userId),userCode, role, profileImageUrl);
        jwtUtil.addJwtHeaders(token,refreshToken, response);


        // refresh 토큰은 redis에 저장
        RefreshToken refresh = RefreshToken.builder()
                .id(userCode)
                .refreshToken(refreshToken)
                .build();
        redisRepository.save(refresh);

        ApiResponse<?> apiResponse = okWithMessage(SuccessCodeEnum.USER_LOGIN_SUCCESS);

        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    /**
     * 로그인 실패 시 실패 응답을 반환합니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param failed   AuthenticationException 객체
     * @throws IOException      입출력 예외가 발생한 경우
     * @throws ServletException Servlet 예외가 발생한 경우
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        ObjectMapper objectMapper = new ObjectMapper();

        // 로그인 실패 응답 반환
        ApiResponse<?> apiResponse = customError(ErrorCodeEnum.LOGIN_FAIL);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
