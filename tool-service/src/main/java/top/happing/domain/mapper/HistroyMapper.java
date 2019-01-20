package top.happing.domain.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.happing.domain.model.Histroy;

import java.util.List;
@Mapper
public interface HistroyMapper {
    List<Histroy> getToday(String date);
}
