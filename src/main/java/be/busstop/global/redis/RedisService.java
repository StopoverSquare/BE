package be.busstop.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;


    // key-value 설정
    public void setValues(String token, String email, Long expiration){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(token.getClass()));

        values.set(token, email, expiration, TimeUnit.MILLISECONDS);
    }

    // key-value 가져오기
    public String getValue(String token){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    // key-value 삭제
    public boolean delValues(String token){
        return redisTemplate.delete(token.substring(7));
    }

}
