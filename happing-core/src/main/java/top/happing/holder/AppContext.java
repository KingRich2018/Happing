package top.happing.holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.happing.kingdom.mapper.bean.UserInfo;
import top.happing.shiro.Principal;

@Component
public class AppContext implements ApplicationContextAware {

    private static final Log logger = LogFactory.getLog(AppContext.class);

    private static ApplicationContext context;

    public static Principal getPrincipal() {
        Principal principal = null;
        try {
            Subject subject = SecurityUtils.getSubject();
            principal = (Principal) subject.getPrincipal();
        } catch (Exception e) {
            logger.error("Could not obtain principal from SHIRO...", e);
        }
        return principal;
    }

    public static Session getSession(){
        Session session = null;
        try{
            Subject subject = SecurityUtils.getSubject();
            session = subject.getSession(false);
        }catch (InvalidSessionException e){
            logger.error("Could not obtain session from SHIRO...", e);
        }
        return session;
    }

    public static UserInfo getCurrUser() {
        UserInfo user = null;
        Principal principal = getPrincipal();
        if (principal != null) {
            user = principal.getUser();
        }

        return user;
    }

    public static Long getCurrUserId() {
        UserInfo user = getCurrUser();
        return user == null ? null : user.getId();
    }

    public static  <T> T getBean(Class<T> requiredType) throws BeansException {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return context.getBean(name, requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
