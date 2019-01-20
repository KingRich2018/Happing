package top.happing.shiro.filter;

import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;
import top.happing.shiro.Principal;
import top.happing.shiro.token.OAuthToken;
import top.happing.shiro.token.TokenSessionHolder;
import top.happing.utils.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAuthFilter extends FormAuthcFilter {

    private String authorizeUrl;

    private String clientId;

    private String redirectUrl;

    // 授权码参数
    private String authcCodeParam = "code";

    private String failureUrl = "view/error/500";

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String code = httpRequest.getParameter(authcCodeParam);
        return new OAuthToken(code);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        if (savedRequest != null && LOCAL_LOGIN_REQUEST_URI.equals(savedRequest.getRequestURI())) {
            // 允许访问本地登陆
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 认证服务器返回错误，则直接转到错误页面
        String error = request.getParameter("error");
        if(!StringUtils.isEmpty(error)) {
            if (isMobileLogin(request)) {
                top.happing.utils.WebUtils.renderString((HttpServletResponse) response,
                        ResponseResultBuiler.failure(error));
                return false;
            } else {
                WebUtils.issueRedirect(request, response, failureUrl + "?error=" + error);
                return false;
            }
        }


        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated()) {
            if(StringUtils.isEmpty(request.getParameter(authcCodeParam))) {
                // 如果用户没有身份验证，且没有auth code，则重定向到服务端授权
//                WebUtils.issueRedirect(request, response,
//                        ShiroHelper.authorizeUrl(authorizeUrl, clientId, redirectUrl));
                return false;
            }
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        OAuthToken oauthToken = (OAuthToken) token;
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);

        Principal principal = (Principal) subject.getPrincipal();
        Session session = subject.getSession(false);
        session.setAttribute("currUser", principal.getUser());
        TokenSessionHolder.setSessionId(oauthToken.getAccessToken(), session.getId().toString());
        return false;
    }


    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
