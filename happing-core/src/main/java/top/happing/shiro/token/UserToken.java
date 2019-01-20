package top.happing.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UserToken extends UsernamePasswordToken {

    public UserToken(String username, String password, boolean rememberMe, String host) {
        super(username, password, rememberMe, host);
    }
}
