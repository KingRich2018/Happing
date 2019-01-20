package top.happing.auth;

import top.happing.filter.FilterContants;
import top.happing.kingdom.mapper.bean.UserSecure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by jlj on 2017/6/30.
 */
public class SignRepo {

    public static final String userSecurePrefix = "USERSECURE:";

    public static final String userSecureNoncePrefix = "USERSECURE_NONCE:";
    @Autowired
    private RedisTemplate redisTemplate;


    public UserSecure userSecure(String appId) {
        return  (UserSecure) redisTemplate.opsForValue().get(userSecurePrefix + appId);
    }

    public String getNonce(String appId) {
        return (String) redisTemplate.opsForValue().get(userSecureNoncePrefix + appId);
    }

    public void setNonce(String appId, String nonce) {
        String key = userSecureNoncePrefix + appId;
        redisTemplate.opsForValue().set(key, nonce);
        redisTemplate.expire(key, FilterContants.ALLOW_TIME_DELAY, TimeUnit.MILLISECONDS);
    }

}
