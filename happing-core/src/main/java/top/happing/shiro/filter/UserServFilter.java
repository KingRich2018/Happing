package top.happing.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import top.happing.holder.AppContext;
import top.happing.kingdom.mapper.bean.UserInfo;
import top.happing.kingdom.mapper.bean.web.ResponseCode;
import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;
import top.happing.shiro.token.RestUserService;
import top.happing.shiro.token.TokenSessionHolder;
import top.happing.shiro.token.UserServiceToken;
import top.happing.utils.StringUtils;
import top.happing.utils.WebUtils;
import top.happing.web.WebHelper;

public class UserServFilter extends AccessControlFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
//            subject.getSession().setTimeout(1800000);
            if (subject.getPrincipal() == null) {
                // try resolve session from token
                checkToken(request, response, subject);
            }

            // If principal is not null, then the user is known and should be allowed access.
            return subject.getPrincipal() != null;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (WebHelper.isMobileLogin(request)) {
            WebUtils.renderString((HttpServletResponse)response,
            ResponseResultBuiler.build(ResponseCode.UNAUTH.getCode(), ResponseCode.UNAUTH.getDesc()));
        } else {
            saveRequestAndRedirectToLogin(request, response);
        }
        return false;
    }

    private boolean checkToken(ServletRequest request, ServletResponse response, Subject subject) {
        String token = WebHelper.containsAccessToken(request);
        if (StringUtils.isNotBlank(token)) {
            RestUserService restUserService = AppContext.getBean(RestUserService.class);
            UserInfo user = restUserService.fetchUserInfo(token, (HttpServletRequest) request);
            if (user == null || user.getId() == null) {
                return false;
            }

            UserServiceToken userServToken = new UserServiceToken(
                    user.getAccount(), StringUtils.trimToEmpty(user.getPassword()), false, request.getRemoteHost());
            userServToken.setUserInfo(user);
            subject.login(userServToken);
            TokenSessionHolder.instance().setSID(token, subject.getSession(false).getId().toString());
        }

        return false;
    }

}
