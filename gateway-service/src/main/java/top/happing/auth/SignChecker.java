package top.happing.auth;

import top.happing.filter.FilterContants;
import top.happing.kingdom.mapper.bean.UserSecure;
import top.happing.kingdom.mapper.bean.web.ResponseCode;
import top.happing.kingdom.mapper.bean.web.ResponseResult;
import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;
import top.happing.utils.RsaUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jlj on 2017/6/30.
 */
public class SignChecker {

    @Autowired
    private SignRepo signRepo;

    public ResponseResult<String> check(String appId, String auth) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(auth)) {
            return ResponseResultBuiler.build(ResponseCode.ILLEARG.getCode(), ResponseCode.ILLEARG.getDesc());
        }

        UserSecure userSecure =signRepo.userSecure(appId);
        if (userSecure == null) {
            return ResponseResultBuiler.build(AuthResponseCode.AUTH_NO_SECURE.getCode(), AuthResponseCode.AUTH_NO_SECURE.getDesc());
        }
        try {
            byte[] privateKey = Base64.decodeBase64(userSecure.getPrivateKey());
            String authDecrypt = new String(RsaUtils.decryptByPrivateKey(Base64.decodeBase64(auth), privateKey));
            String[] authDecryptArr = StringUtils.split(authDecrypt, FilterContants.AUTH_SEPARATOR_CHARS);

            String token = authDecryptArr[0];
            String timestamp = authDecryptArr[1];
            String nonce = authDecryptArr[2];
            if (StringUtils.isBlank(token) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
               return ResponseResultBuiler.build(AuthResponseCode.AUTH_INVALID.getCode(), AuthResponseCode.AUTH_INVALID.getDesc());
            }

            long now = System.currentTimeMillis();
            if ((Long.valueOf(timestamp) + FilterContants.ALLOW_TIME_DELAY) < now) {
                return ResponseResultBuiler.build(AuthResponseCode.REQ_TIMEOUT.getCode(), AuthResponseCode.REQ_TIMEOUT.getDesc());
            }

            if (nonce.equals(signRepo.getNonce(appId))) {
                return ResponseResultBuiler.build(AuthResponseCode.REQ_REPLAY.getCode(), AuthResponseCode.REQ_REPLAY.getDesc());
            } else {
                signRepo.setNonce(appId, nonce);
            }

            if (!token.equals(userSecure.getToken())) {
                return ResponseResultBuiler.build(AuthResponseCode.TOKEN_INVALID.getCode(), AuthResponseCode.TOKEN_INVALID.getDesc());
            }

            if (now > userSecure.getTokenEndTime().getTime()) {
                return ResponseResultBuiler.build(AuthResponseCode.TOKEN_EXPIRE.getCode(), AuthResponseCode.TOKEN_EXPIRE.getDesc());
            }

            return ResponseResultBuiler.build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseResultBuiler.failure();
    }


    public ResponseResult<String> checkAdmin(String sid, String auth, String secure) {
        if (StringUtils.isBlank(sid) || StringUtils.isBlank(auth)) {
            return ResponseResultBuiler.build(ResponseCode.ILLEARG.getCode(), ResponseCode.ILLEARG.getDesc());
        }

        String digestAuth = DigestUtils.md5Hex(sid + secure);
        if (!digestAuth.equals(auth)) {
            return ResponseResultBuiler.build(AuthResponseCode.AUTH_NO_SECURE.getCode(), AuthResponseCode.AUTH_NO_SECURE.getDesc());
        }

        return ResponseResultBuiler.success();
    }

}
