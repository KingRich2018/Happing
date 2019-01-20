package top.happing.shiro;

import top.happing.shiro.token.TokenSessionHolder;
import top.happing.utils.StringUtils;
import top.happing.web.WebHelper;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

public class ShiroSessionManager extends DefaultWebSessionManager {


    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        Serializable sid = null;
        if (WebHelper.isMobileLogin(request)) {
            String token = WebHelper.containsAccessToken(request);
            if (StringUtils.isNotBlank(token)) {
                sid = TokenSessionHolder.instance().getSID(token);
            }
        } else {
            sid = super.getSessionId(request, response);
        }

        return sid;
    }

    @Override
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        // when session is invalidation, remove it from holder
        TokenSessionHolder.instance().rmSID(key.getSessionId());
    }

    @Override
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        // when session is expiration, remove it from holder
        TokenSessionHolder.instance().rmSID(key.getSessionId());
    }

//    @Override
//    protected void onChange(Session session) {
//        super.onChange(session);
//        TokenSessionHolder.instance().rmSID(session.getId());
//    }
}
