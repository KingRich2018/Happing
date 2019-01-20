package top.happing.shiro.token;

import top.happing.conf.properties.ShiroProperties;
import top.happing.holder.AppContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class TokenSessionHolder {

    private static final String T_S_PREFIX = "token_session:";

    private static final TokenSessionHolder instance = new TokenSessionHolder();

    private final static Map<String, String> holder = new ConcurrentHashMap<String, String>();

    private RedisTemplate redisTemplate = AppContext.getBean("redisTemplate", RedisTemplate.class);

    private ShiroProperties shiroProperties = AppContext.getBean("shiroProperties", ShiroProperties.class);

    private TokenSessionHolder() {

    }

    public static TokenSessionHolder instance() {
        return instance;
    }

    public String getSID(String token) {
        return (String) redisTemplate.opsForValue().get(T_S_PREFIX + token);
    }

    public void setSID(String token, String sid) {
        String tskey = T_S_PREFIX + token;
        redisTemplate.opsForValue().set(tskey, sid);
        redisTemplate.expire(tskey, shiroProperties.getGlobalSessionTimeout(), TimeUnit.MILLISECONDS);
    }

    public void rmSID(Serializable sid) {
    }


    public static String getSessionId(String token) {
        return holder.get(token);
    }

    public static void setSessionId(String token, String sessionId) {
        holder.put(token, sessionId);
    }

    public static void removeBySessionId(Serializable sessionId) {
        if (sessionId == null) {
            return;
        }
        String sid = sessionId.toString();
        String token = "";
        Iterator<Map.Entry<String, String>> it = holder.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (sid.equals(entry.getValue())) {
                token = entry.getKey();
            }
        }
        holder.remove(token);
    }
}
