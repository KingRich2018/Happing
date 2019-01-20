package top.happing.shiro.realm;

import top.happing.shiro.token.LdapUserToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.ldap.AbstractLdapRealm;
import org.apache.shiro.realm.ldap.DefaultLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

@ConfigurationProperties(prefix = "happing.ldap")
public class LdapAuthorizingRealm extends AbstractLdapRealm {

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleService roleService;

    protected void onInit() {
        super.onInit();
        ensureContextFactory();
    }

    private void ensureContextFactory() {
        DefaultLdapContextFactory defaultFactory = new DefaultLdapContextFactory();
        defaultFactory.setUrl(this.url);
        defaultFactory.setSystemUsername(this.systemUsername);
        defaultFactory.setSystemPassword(this.systemPassword);
        defaultFactory.setSearchBase(this.searchBase);

         setLdapContextFactory(defaultFactory);
    }

    @Override
    protected AuthenticationInfo queryForAuthenticationInfo(
            AuthenticationToken token, LdapContextFactory ldapContextFactory) throws NamingException {
        LdapUserToken userToken = (LdapUserToken) token;

        Object principal = token.getPrincipal();
        Object credentials = token.getCredentials();

        LdapContext ctx = null;
        try {
            ctx = ldapContextFactory.getLdapContext(principal, credentials);
        } finally {
            LdapUtils.closeContext(ctx);
        }

        String account = (String) token.getPrincipal();
//        User user = userService.selectByAccount(account);
//        if (user == null) {
//            // LDAP认证通过后，本地没有用户先插入一条
//            User newUser = new User();
//            newUser.setAccount(account);
//            userService.insert(newUser);
//            Principal userPrincipal = new Principal(newUser.getId(), account, newUser.getName());
//            userPrincipal.setUser(newUser);
//            return new SimpleAuthenticationInfo(userPrincipal, credentials, getName());
//        } else {
//            Principal userPrincipal = new Principal(user.getId(), account, user.getName());
//            userPrincipal.setUser(user);
//            return new SimpleAuthenticationInfo(userPrincipal, credentials, getName());
//        }

        return null;
    }

    @Override
    protected AuthorizationInfo queryForAuthorizationInfo(
            PrincipalCollection principalCollection, LdapContextFactory ldapContextFactory) throws NamingException {
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
        return token instanceof LdapUserToken;
    }
}
