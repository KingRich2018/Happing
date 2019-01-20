package top.happing.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import top.happing.conf.properties.OAuthProperties;
import top.happing.conf.properties.ShiroProperties;
import top.happing.shiro.JedisSessionDao;
import top.happing.shiro.ShiroSessionManager;
import top.happing.shiro.filter.FormAuthcFilter;
import top.happing.shiro.filter.OAuthFilter;
import top.happing.shiro.filter.UserFilter;
import top.happing.shiro.filter.UserServFilter;

import top.happing.shiro.realm.DefaultAuthorizingRealm;
import top.happing.shiro.realm.LdapAuthorizingRealm;
import top.happing.shiro.realm.OAuthRealm;
import top.happing.shiro.realm.UserServRealm;
import top.happing.utils.StringUtils;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@Configuration
@ConditionalOnProperty(prefix = "happing.shiro", name = "enable", havingValue = "true")
public class ShiroConf {

    @Bean
    public OAuthProperties oauthProperties() {
        return new OAuthProperties();
    }

    @Bean
    public ShiroProperties shiroProperties() {
        return new ShiroProperties();
    }

    @Bean
    @Qualifier("defaultAuthorizingRealm")
    public Realm defaultAuthorizingRealm() {
        return new DefaultAuthorizingRealm();
    }

    @Bean
    @Qualifier("ldapAuthorizingRealm")
    public Realm ldapAuthorizingRealm() {
        return new LdapAuthorizingRealm();
    }

    @Bean
    @Qualifier("oauthRealm")
    public Realm oauthRealm() {
        return new OAuthRealm();
    }

    @Bean
    @Qualifier("userServRealm")
    public Realm userServRealm() {
        return new UserServRealm();
    }

    @Bean("simpleCookie")
    public SimpleCookie simpleCookie() {
        return new SimpleCookie("__sid");
    }

    @Bean("jedisSessionDao")
    @ConditionalOnBean(RedisTemplate.class)
    public SessionDAO jedisSessionDao(RedisTemplate redisTemplate, ShiroProperties shiroProperties) {
        JedisSessionDao jedisSessionDao = new JedisSessionDao();
        jedisSessionDao.setRedisTemplate(redisTemplate);
        jedisSessionDao.setShiroProperties(shiroProperties);
        return jedisSessionDao;
    }

    @Bean("sessionDao")
    @ConditionalOnMissingBean
    public SessionDAO sessionDao() {
        return new MemorySessionDAO();
    }

    @Bean("cacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean("realms")
    public List<Realm> realms(Realm defaultAuthorizingRealm, Realm ldapAuthorizingRealm,
                              Realm oauthRealm) {
        List<Realm> realms = new ArrayList<Realm>();
        realms.add(defaultAuthorizingRealm);
        realms.add(ldapAuthorizingRealm);
        realms.add(oauthRealm);
        return realms;
    }

    @Bean("authenticator")
    public Authenticator authenticator(List<Realm> realms) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(realms);
        return authenticator;
    }

    @Bean("authorizer")
    public Authorizer authorizer(List<Realm> realms) {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setRealms(realms);
        return authorizer;
    }

    @Bean("sessionManager")
    public SessionManager sessionManager(SessionDAO sessionDao, SimpleCookie simpleCookie, ShiroProperties properties) {
        DefaultWebSessionManager sessionManager = new ShiroSessionManager();
        sessionManager.setSessionDAO(sessionDao);
        sessionManager.setGlobalSessionTimeout(properties.getGlobalSessionTimeout());
        sessionManager.setSessionValidationInterval(120000);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionIdCookie(simpleCookie);
        boolean cookieEnable = StringUtils.isNotBlank(properties.getApp()) && "w".equals(properties.getApp());
        sessionManager.setSessionIdCookieEnabled(cookieEnable);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(SessionManager sessionManager, CacheManager cacheManager,
                                           Authenticator authenticator, Authorizer authorizer) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // securityManager.setRealm(realm);
        securityManager.setAuthenticator(authenticator);
        securityManager.setAuthorizer(authorizer);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager, ShiroProperties properties,
                                                     OAuthProperties oauthProperties) {
        ShiroFilterFactoryBean shiroFilterFactory = new ShiroFilterFactoryBean();
        shiroFilterFactory.setSecurityManager(securityManager);
        shiroFilterFactory.setLoginUrl(properties.getLoginUrl());
        shiroFilterFactory.setSuccessUrl(properties.getSuccessUrl());
        shiroFilterFactory.setUnauthorizedUrl(properties.getUnauthorizedUrl());
        shiroFilterFactory.setFilterChainDefinitionMap(properties.getCustomDefinitions());

        customFilters(shiroFilterFactory, oauthProperties);
        return shiroFilterFactory;
    }

    private void customFilters(ShiroFilterFactoryBean shiroFilterFactory,
                               OAuthProperties oauthProperties) {
        Map<String, Filter> filters = shiroFilterFactory.getFilters();
        filters.put("authce", new FormAuthcFilter());
        OAuthFilter oauthFilter = new OAuthFilter();
        oauthFilter.setClientId(oauthProperties.getClientId());
        oauthFilter.setAuthorizeUrl(oauthProperties.getAuthorizeUrl());
        oauthFilter.setRedirectUrl(oauthProperties.getRedirectUrl());
        filters.put("oauth", oauthFilter);
        UserFilter userFilter = new UserFilter();
//        userFilter.setOauthProperties(oauthProperties);
        filters.put("user", userFilter);

        UserServFilter userServFilter = new UserServFilter();
        filters.put("users", userServFilter);
    }

    @Bean
    public FilterRegistrationBean shiroFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filter.addInitParameter("targetFilterLifecycle", "true");
        filter.addUrlPatterns("/*");
        return filter;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator advisorProxy() {
        DefaultAdvisorAutoProxyCreator adviros = new DefaultAdvisorAutoProxyCreator();
        adviros.setProxyTargetClass(true);
        return adviros;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor sourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
