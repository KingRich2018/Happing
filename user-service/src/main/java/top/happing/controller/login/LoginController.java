package top.happing.controller.login;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import top.happing.bean.user.User;
import top.happing.kingdom.mapper.bean.UserSecure;
import top.happing.kingdom.mapper.bean.web.ResponseCode;
import top.happing.kingdom.mapper.bean.web.ResponseResult;
import top.happing.kingdom.mapper.bean.web.ResponseResultBuiler;
import top.happing.service.user.UserService;
import top.happing.utils.AesCbcUtil;
import top.happing.utils.JsonUtils;
import top.happing.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.netflix.config.DeploymentContext.ContextKey.appId;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@RestController
public class LoginController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;



    @Value("${hap.world.url}")
    private String worldurl;

    @Value("${hap.world.appid}")
    private String appId;

    @Value("${hap.world.secret}")
    private String secret;
    /*
     * 手机号授权登录
     *
     * @param loginReq
     * @return
     */
    @GetMapping("/login/countUser")
    public int countUser(){
        return userService.countUser();
    }
    @GetMapping("/login/getOne/{openId}")
    public User getOne(@PathVariable("openId") String openId){
        return userService.getOne(openId);
    }
    @PostMapping("/login/weixin")
    public ResponseResult<Map<String, Object>> WXCodeLogin(@RequestBody LoginRequest loginReq, HttpServletRequest request) {
        String url = worldurl+"?appid=" + appId + "&secret=" + secret + "&js_code="
                + loginReq.getCode() + "&grant_type=authorization_code";
        String encryptedData = loginReq.getEncryptedData();
        String iv = loginReq.getIv();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
                String sessionData = responseEntity.getBody();
                Map<String, Object> result = (LinkedHashMap<String, Object>) JsonUtils.fromJsonString(sessionData,
                        Object.class);
                Map<String, Object> map = new HashMap<String, Object>();
                if (result != null && StringUtils.isNotBlank((String) result.get("openid"))) {
                    //String resultObject = AesCbcUtil.decrypt(encryptedData, result.get("session_key").toString(), iv, "UTF-8");
                    User user = new User();
                    user.setOpenId(result.get("openid").toString());
                   /* if (null != resultObject && resultObject.length() > 0) {
                        JSONObject userInfoJSON = JSONObject.fromObject(resultObject);
                        if (StringUtils.isNotBlank((String) userInfoJSON.get("unionid"))) {
                            user.setWeixinId(userInfoJSON.get("unionid").toString());
                        }
                    }*/
                    User existUser = userService.selectOne(user);
                    if (existUser != null) {
                        map.put("token", existUser.getToken());
                        map.put("status", existUser.getStatus());
                        map.put("nickname", existUser.getNickname());
                        map.put("openId", existUser.getOpenId());
                        return ResponseResultBuiler.success(map);
                    }
                    map.put("token", "");
                    map.put("username", "");
                    map.put("password", "");
                    map.put("status", -1);
                    map.put("openId",result.get("openid").toString());
                    return ResponseResultBuiler.build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(),
                            map);
                }

            }
        } catch (Exception e) {

            return ResponseResultBuiler.failure("无用户");
        }
        return ResponseResultBuiler.failure("网络错误");
    }
    @PostMapping("/login/weiLogin")
    public ResponseResult loginAuthority(@RequestBody LoginRequest loginReq, HttpServletRequest request) {
       /* if (loginReq == null || StringUtils.isBlank(loginReq.getIv())
                || StringUtils.isBlank(loginReq.getEncryptedData()) || StringUtils.isBlank(loginReq.getCode())) {
            return ResponseResultBuiler.build(ResponseCode.ILLEARG.getCode(), ResponseCode.ILLEARG.getDesc());
        }
        */
        String url = worldurl + "?appid=" + appId + "&secret=" + secret
                + "&js_code=" + loginReq.getCode() + "&grant_type=authorization_code";
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
                String sessionData = responseEntity.getBody();
                Map<String, Object> result = (LinkedHashMap<String, Object>) JsonUtils.fromJsonString(sessionData,
                        Object.class);
                if (result != null && StringUtils.isNotBlank((String) result.get("openid"))) {
                    loginReq.setOpenId((String) result.get("openid"));
                }
            }
        } catch (Exception e) {
            ResponseResultBuiler.failure("无效的Code");
        }
        Map<String, Object> result = new HashMap<String, Object>();
        // validate user exist
        User newuser = new User();
        newuser.setOpenId(loginReq.getOpenId());
        User existUser = userService.selectOne(newuser);
        if (existUser != null) {
            userService.saveOrUpdate(existUser);
            result.put("token", existUser.getToken());
            result.put("nickname", existUser.getNickname());
            result.put("status", existUser.getStatus());
            return ResponseResultBuiler.build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), result);
        }
        // generate user token
        String token = UserTokenGenerator.generateToken();
        // save user info
        User user = buildUser(loginReq, token);
        userService.insert(user);
        result.put("token", token);
        result.put("status", 0);
        return ResponseResultBuiler.build(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getDesc(), result);
    }
        private  User validateExistUser (LoginRequest loginReq){
            String openId = loginReq.getOpenId();
            //String unionId = loginReq.getWeixinId();
            User user = new User();
            user.setAccount("");
            user.setOpenId("");
           // user.setAccount(unionId);
            return userService.selectOne(user);
        }

    private User buildUser(LoginRequest loginReq, String token) {
        User user = new User();
        user.setImg(loginReq.getImg());
        user.setWeixinId("");
        user.setOpenId(loginReq.getOpenId());
        user.setPhone("");
        user.setAccount("");
        user.setGender(loginReq.getGender());
        user.setPassword("123456");
        user.setNickname(loginReq.getNickname());
        user.setToken(token);
        user.setStatus("0");
        user.setCountry(loginReq.getCountry());
        user.setProvince(loginReq.getProvince());
        user.setCity(loginReq.getCity());
        user.setDistrict(loginReq.getDistrict());
        //user.setLongitude(loginReq.getLongitude());
       // user.setLatitude(loginReq.getLatitude());
        return user;
    }


}