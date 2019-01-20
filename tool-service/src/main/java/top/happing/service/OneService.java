package top.happing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.happing.domain.mapper.OneMapper;
import top.happing.domain.model.One;
import top.happing.framework.service.CrudService;

import java.util.List;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Service
public class OneService  {

    @Autowired
    OneMapper oneMapper;

  public List<One> randomOne()
    {
        System.out.println(1);
        return oneMapper.randomOne();
    }


    public int countbyMenu()
    {
        System.out.println(1);
        return oneMapper.countbyMenu();
    }

}
