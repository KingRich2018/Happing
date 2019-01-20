package top.happing.shiro.filter;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import top.happing.kingdom.mapper.bean.web.ResponseCode;
import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;
import top.happing.shiro.Principal;
import top.happing.shiro.token.LdapUserToken;
import top.happing.shiro.token.TokenType;
import top.happing.shiro.token.UserToken;
import top.happing.web.WebHelper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WeixinAuthcFilter extends FormAuthenticationFilter {

    public static final String LOCAL_LOGIN_REQUEST_URI = "/login-auth";

    public boolean isLocalLoginRequest(ServletRequest request, ServletResponse response) {
        return LOCAL_LOGIN_REQUEST_URI.equals(((HttpServletRequest) request).getRequestURI());
    }

    @Override
    protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
        return isLocalLoginRequest(request, response);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = StringUtils.trimToEmpty(getPassword(request));
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);

        String domain = getDomain(request);
        if (TokenType.PASSWORD.name().equals(domain)) {
            return new UserToken(username, "", rememberMe, host);
        } else if (TokenType.LDAP.name().equals(domain)) {
            return new LdapUserToken(username, password, rememberMe, host, isMobileLogin(request));
        }

        if ("admin".equals(username)) {
            return new UserToken(username, password, rememberMe, host);
        }
        return new LdapUserToken(username, password, rememberMe, host, isMobileLogin(request));
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        if (isMobileLogin(request)) {
            top.happing.utils.WebUtils.renderString((HttpServletResponse) response,
                    ResponseResultBuiler.build(ResponseCode.SUCCESS.getCode(), "login success", subject.getSession(false).getId().toString()));
        } else {
            WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
        }
        Principal principal = (Principal) subject.getPrincipal();
        subject.getSession(false).setAttribute("currUser", principal.getUser());
        return false;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className) ||
                AuthenticationException.class.getName().equals(className)) {
            message = "用户或密码错误, 请重试.";
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        } else {
            message = "系统出现点问题，请稍后再试！";
            e.printStackTrace();
        }
        if (isMobileLogin(request)) {
           top.happing.utils.WebUtils.renderString((HttpServletResponse) response,
                    ResponseResultBuiler.failure(message));
            return false;
        } else {
            request.setAttribute(getFailureKeyAttribute(), className);
            request.setAttribute(getMessageParam(), message);
        }
        return true;
    }

    @Override
    public String getUsernameParam() {
        return "account";
    }

    public String getMessageParam() {
        return "message";
    }

    public boolean isMobileLogin(ServletRequest request) {
        return WebHelper.isMobileLogin(request);
    }

    public String getDomain(ServletRequest request) {
        return WebUtils.getCleanParam(request, "domain");
    }

}
