package top.happing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.happing.domain.mapper.HistroyMapper;
import top.happing.domain.model.Histroy;

import java.util.List;

/**
 * @Author wangbo
 * @Description
 * @Date $ $
 **/
@Service
public class HistroyService {
    @Autowired
    HistroyMapper histroyMapper;
    public List<Histroy> getToday(String date){
        System.out.println(date);
        return histroyMapper.getToday(date);
    }

}
