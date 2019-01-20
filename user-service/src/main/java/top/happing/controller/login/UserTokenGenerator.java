package top.happing.controller.login;

import java.util.UUID;

/**
 * Created by jlj on 2017/9/5.
 */
public class UserTokenGenerator {


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
