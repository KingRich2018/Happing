package top.happing.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class LdapUserToken extends UsernamePasswordToken {
    private boolean mobile;

    public LdapUserToken(String username, String password, boolean rememberMe, String host, boolean mobile) {
        super(username, password, rememberMe, host);
        this.mobile = mobile;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }
}
