package be.busstop.global.security.jwt;

import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.redis.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final UserRepository userRepository;
    private final RedisService redisService;

    public JwtUtil(UserRepository userRepository, RedisService redisService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
    }

    // Header의 KEY 값
    public static final String ACCESS_HEADER = "Access";
    public static final String REFRESH_HEADER = "Refresh";

    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    // 토큰 유효시간
    public final long ACCESS_TOKEN_TIME = 60 * 30 * 1000L;
    public final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 30 * 1000L;

    // log 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); // Base64로 Encode되어있는 secretKey를 Decode하여 사용
        key = Keys.hmacShaKeyFor(bytes); // 새로운 시크릿키 인스턴스 생성
    }

    //JWT(토큰생성)
    public String createToken(String username, UserRoleEnum role, long tokenValid) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + tokenValid)) // 생성 시간에 대한 만료시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); //Actually builds the JWT and serializes it to a compact, URL-safe string according to the JWT Compact
    }

    // AccessToken 생성
    public String createAccessToken(String nickname, UserRoleEnum role) {
        return this.createToken(nickname, role, ACCESS_TOKEN_TIME);
    }

    // Refresh Token 생성
    public String createRefreshToken(String nickname, UserRoleEnum role) {
        return this.createToken(nickname, role, REFRESH_TOKEN_TIME);
    }

    // JWT Cookie에 저장
    public void addJwtToCookie(String token, String tokenValue, HttpServletResponse res){
        try{
            log.info("쿠키주입성공");
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            ResponseCookie cookie = ResponseCookie.from(tokenValue, token)
                    .path("/")
                    .sameSite("")
                    .httpOnly(false)
                    .secure(true)
                    .build();

            res.addHeader("Set-Cookie", cookie.toString());
        }catch (UnsupportedEncodingException e){
            logger.error(e.getMessage());
        }
    }

    // 받아온 Cookie의 Value인 JWT 토큰 substring
    public String substringToken(String tokenValue){
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)){
            return tokenValue.substring("Bearer ".length());
        }
        logger.error("Not Found Token");
        throw new IllegalArgumentException("Not Found Token");
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(String tokenValue, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenValue)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // header 에서 Access JWT 가져오기
    public String getAccessJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken;
        }
        return null;
    }

    // header 에서 Refresh JWT 가져오기
    public String getRefreshJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken;
        }
        return null;
    }

    // Token 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    public boolean validateAllToken(String accessToken, String refreshToken, HttpServletResponse response) {
        accessToken = accessToken.substring("Bearer ".length());
        refreshToken = refreshToken.substring("Bearer ".length());

        if(accessToken != null){ // accessToken 비어있지 않고
            if(validateToken(accessToken) && redisService.getValue(accessToken) == null){ // access 검증, 로그아웃하지 않았다
                return true;
            }else if(!validateToken(accessToken) && refreshToken != null){ //검증은 안되는데 refresh 토큰이 값이 있어
                boolean validateRefreshToken = validateToken(refreshToken); // refresh token 검증
                boolean isRefreshToken = existsRefreshToken(refreshToken); // refresh token DB 존재
                if(validateRefreshToken && isRefreshToken){
                    String email = getUserInfoFromToken(refreshToken).getSubject();
                    UserRoleEnum role = getRoles(email);
                    String newAccessToken = createAccessToken(email, role);
                    response.addHeader(JwtUtil.ACCESS_HEADER, newAccessToken);

                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean existsRefreshToken(String refreshToken) {
        return redisService.getValue(refreshToken) != null;
    }

    public UserRoleEnum getRoles(String nickname){
        return userRepository.findByNickname(nickname).get().getRole();
    }

    // token에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();
        Long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    public String getNicknameFromToken(String token){
        String subToken = substringToken(token);
        Claims info = getUserInfoFromToken(subToken);
        return info.getSubject();
    }
}
