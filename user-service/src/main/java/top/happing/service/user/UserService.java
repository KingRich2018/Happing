package top.happing.service.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.happing.bean.user.User;

import top.happing.framework.service.CrudService;
import top.happing.holder.AppContext;

import top.happing.id.ObjectId;
import top.happing.kingdom.mapper.bean.UserSecure;
import top.happing.mapper.user.UserMapper;
import top.happing.mapper.user.UserSecureMapper;
import top.happing.utils.StringUtils;

import java.util.Date;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Service
public class UserService extends CrudService<UserMapper, User> {

    private static final String userSecurePrefix = "USERSECURE:";

    private static final long userTokenTime = 31536000000L;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    public int countUser(){
        return userMapper.countUser();
    }

    public User getOne(String openId){
        return userMapper.getOne(openId);
    }


}
