package be.busstop.global.security.jwt;

import be.busstop.global.stringCode.ErrorCodeEnum;
import be.busstop.global.utils.ResponseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtExceptionFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다: {}", e.getMessage());
            setErrorResponse(response, ErrorCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException | NullPointerException | UnsupportedEncodingException e) {
            log.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
            setErrorResponse(response, ErrorCodeEnum.TOKEN_INVALID);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCodeEnum.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(ResponseUtils.customError(errorCodeEnum)));
        } catch (IOException e) {
            log.error("에러 응답을 설정하는데 실패하였습니다: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
