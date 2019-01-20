package top.happing.mapper.user;

import org.apache.ibatis.annotations.Mapper;
import top.happing.bean.user.User;
import top.happing.framework.domain.mapper.BaseMapper;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    public int countUser();

    public User getOne(String openId);
}
