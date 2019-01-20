package top.happing.shiro;

import top.happing.conf.properties.ShiroProperties;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import top.happing.utils.JsonUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class JedisSessionDao extends AbstractSessionDAO {

    private static final String SESSION_PREFIX = "session:";

    private RedisTemplate redisTemplate;

    private ValueOperations<String, Session> ops;

    private ShiroProperties shiroProperties;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        storeSession(sessionId, session);
        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return ops.get(SESSION_PREFIX + sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        storeSession(session.getId(), session);
    }

    @Override
    public void delete(Session session) {
        if (session == null) {
            throw new NullPointerException("session argument cannot be null.");
        }
        Serializable id = session.getId();
        if (id != null) {
            redisTemplate.delete(SESSION_PREFIX + id);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.opsForValue().multiGet(redisTemplate.keys(SESSION_PREFIX + "*"));
    }

    private void storeSession(Serializable sessionId, Session session) {
        if (sessionId == null) {
            throw new NullPointerException("when store the session, id argument cannot be null.");
        }
        String key = SESSION_PREFIX + sessionId;
        redisTemplate.opsForValue().set(key, session);
        redisTemplate.expire(key, shiroProperties.getGlobalSessionTimeout(), TimeUnit.MILLISECONDS);
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ops = redisTemplate.opsForValue();
    }

    public void setShiroProperties(ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
    }
}
