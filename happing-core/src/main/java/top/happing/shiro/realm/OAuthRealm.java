package top.happing.shiro.realm;

import top.happing.shiro.token.OAuthToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class OAuthRealm extends AuthorizingRealm {

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private OAuthProperties oauthProperties;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        OAuthToken authToken = (OAuthToken) token;
//        String authCode = authToken.getAuthCode();
//        // 获取access token
//        String accessTokenJson = WebUtils.executeHttpGet(
//                ShiroHelper.iamTokenUrl(oauthProperties.getAccessTokenUrl(), oauthProperties.getClientId(),
//                        oauthProperties.getRedirectUrl(), oauthProperties.getClientSecret(), authCode));
//        // 获取 user info
//        if (StringUtils.isBlank(accessTokenJson)) {
//            throw new AuthenticationException("Could not obtain access token");
//        } else {
//            AccessToken accessToken = (AccessToken) JsonUtils.fromJsonString(accessTokenJson, AccessToken.class);
//            authToken.setAccessToken(accessToken.getAccess_token());
//            String userInfoJson = WebUtils.executeHttpGet(
//                    ShiroHelper.iamUserUrl(oauthProperties.getUserInfoUrl(), accessToken.getAccess_token()));
//            if (StringUtils.isBlank(userInfoJson)) {
//                throw new AuthenticationException("Could not obtain user info");
//            }
//
//            UserInfo iamUserInfo = (UserInfo) JsonUtils.fromJsonString(userInfoJson, UserInfo.class);
//            String account = iamUserInfo.getSaicId();
//            User user = userService.selectByAccount(account);
//            if (user == null) {
//                user = iamUserInfo.toUser();
//                userService.insert(user);
//            }
//
//            Principal principal = new Principal(user.getId(), account, user.getName());
//            principal.setUser(user);
//            return new SimpleAuthenticationInfo(principal, authCode, getName());
//        }

        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuthToken;
    }

}
