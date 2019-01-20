package top.happing.shiro.token;

import top.happing.conf.properties.ServiceProperties;
import top.happing.holder.AppContext;
import top.happing.kingdom.mapper.bean.UserInfo;
import top.happing.exception.AppException;
import top.happing.framework.service.CrudService;
import top.happing.framework.service.RestBaseService;
import top.happing.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestUserService extends RestBaseService {

    @Autowired
    private ServiceProperties serviceProperties;
    
    public UserInfo fetchByAccount(String account) {
    	CrudService usrSer = usrSer();
        if (usrSer != null) {
        	Map<String, Object> params = new HashMap<>();
            params.put("account", account);
            
            List list = usrSer.selectByMap(params);
            if (list.size() == 1) {
                Object usr = list.get(0);
                UserInfo userInfo = new UserInfo();
                BeanUtils.setPropertyValue(userInfo, "id", BeanUtils.getPropertyValue(usr, "id"));
                BeanUtils.setPropertyValue(userInfo, "account", BeanUtils.getPropertyValue(usr, "account"));
                BeanUtils.setPropertyValue(userInfo, "name", BeanUtils.getPropertyValue(usr, "name"));
                BeanUtils.setPropertyValue(userInfo, "password", BeanUtils.getPropertyValue(usr, "password"));
                BeanUtils.setPropertyValue(userInfo, "nickname", BeanUtils.getPropertyValue(usr, "nickname"));
                BeanUtils.setPropertyValue(userInfo, "img", BeanUtils.getPropertyValue(usr, "img"));
                return userInfo;
            } else {
                throw new AppException("Invalid token");
            }
        } else {
        	return get(serviceProperties.getUser(), "/user/findByAccount/" + account, UserInfo.class, null);
        }
    }

    public UserInfo fetchUserInfo(String token, HttpServletRequest request) {
        CrudService usrSer = usrSer();
        if (usrSer != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", token);
            
            List list = usrSer.selectByMap(params);
            if (list.size() == 1) {
                Object usr = list.get(0);
                UserInfo userInfo = new UserInfo();
                BeanUtils.setPropertyValue(userInfo, "id", BeanUtils.getPropertyValue(usr, "id"));
                BeanUtils.setPropertyValue(userInfo, "account", BeanUtils.getPropertyValue(usr, "account"));
                BeanUtils.setPropertyValue(userInfo, "name", BeanUtils.getPropertyValue(usr, "name"));
                BeanUtils.setPropertyValue(userInfo, "password", BeanUtils.getPropertyValue(usr, "password"));
                BeanUtils.setPropertyValue(userInfo, "nickname", BeanUtils.getPropertyValue(usr, "nickname"));
                BeanUtils.setPropertyValue(userInfo, "img", BeanUtils.getPropertyValue(usr, "img"));
                return userInfo;
            } else {
                throw new AppException("Invalid token");
            }
        }
        return get(serviceProperties.getUser(), "/user/token/" + token, UserInfo.class, request);
    }

    public List<String> fetchRolesByUser(UserInfo userInfo) {

        return Collections.emptyList();
    }


    public CrudService usrSer() {
        try {
            return  (CrudService) AppContext.getBean("userService", CrudService.class);
        } catch (Exception e) {
            // ignore e
        }
        return null;
    }

}
