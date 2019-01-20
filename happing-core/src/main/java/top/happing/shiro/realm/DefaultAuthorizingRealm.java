package top.happing.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import top.happing.kingdom.mapper.bean.UserInfo;
import top.happing.shiro.Principal;
import top.happing.shiro.token.RestUserService;
import top.happing.shiro.token.UserToken;

public class DefaultAuthorizingRealm extends AuthorizingRealm {
	
    @Autowired
    private RestUserService restUserService;

//    @Autowired
//    private UserService userService;

//    @Autowired
//    private RoleService roleService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UserToken token = (UserToken) authenticationToken;
        String account = (String) token.getPrincipal();
        UserInfo user = restUserService.fetchByAccount(account);
        if (user != null) {
          Principal principal = new Principal(user.getId(), account, user.getName());
          principal.setUser(user);
          return new SimpleAuthenticationInfo(principal, "", getName());
      }
//        User user = userService.selectByAccount(account);
//        if (user != null) {
//            Principal principal = new Principal(user.getId(), account, user.getName());
//            principal.setUser(user);
//            return new SimpleAuthenticationInfo(principal, StringUtils.trimToEmpty(user.getPassword()), getName());
//        }
        return null;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
//        User user = principal.getUser();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        List<Role> roles = roleService.selectRoleListByUser(user.getId());
//        for (Role role : roles) {
//            info.addRole(role.getRoleCode());
//        }
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UserToken;
    }
}
