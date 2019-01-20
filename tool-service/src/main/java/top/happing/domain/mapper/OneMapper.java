package top.happing.domain.mapper;

import org.apache.ibatis.annotations.Mapper;



import top.happing.domain.model.One;
import top.happing.framework.domain.mapper.BaseMapper;

import java.util.List;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Mapper
public interface OneMapper {

   List<One> randomOne();

    int countbyMenu();
}
