
package top.happing.framework.domain.mapper;

import top.happing.framework.domain.model.BaseEntity;
import top.happing.mybatis.query.QueryCriteria;
import org.apache.ibatis.annotations.Options;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T extends BaseEntity> {

    /**
     *
     * @param entity
     * @return
     */
    @Options(useGeneratedKeys = true)
    Long insert(T entity);

    /**
     *
     * @param id
     * @return
     */
    void deleteById(Serializable id);

    /**
     *
     * @param columnMap
     * @return
     */
    void deleteByMap(Map<String, Object> columnMap);

    /**
     *
     * @param entity
     */
    void delete(T entity);

    /**
     *
     * @param idList
     */
    void deleteByIdList(List<? extends Serializable> idList);

    /**
     *
     * @param entity
     * @return
     */
    void updateById(T entity);

    /**
     *
     * @param entity
     * @return
     */
    Integer update(T entity);

    /**
     *
     * @param id
     * @return
     */
    T selectById(Serializable id);

    /**
     *
     * @param idList
     * @return
     */
    List<T> selectByIdList(List<? extends Serializable> idList);

    /**
     *
     * @param columnMap
     * @return
     */
    List<T> selectByMap(Map<String, Object> columnMap);

    /**
     *
     * @param entity
     * @return
     */
    T selectOne(T entity);

    /**
     *
     * @param entity
     * @return
     */
    Integer selectCount(T entity);

    /**
     *
     * @param entity
     * @return
     */
    List<T> selectList(T entity);

    /**
     *
     * @param criteria
     * @return
     */
    T selectOneByCriteria(QueryCriteria criteria);

    /**
     *
     * @param criteria
     * @return
     */
    Integer selectCountByCriteria(QueryCriteria criteria);

    /**
     *
     * @param criteria
     * @return
     */
    List<T> selectListByCriteria(QueryCriteria criteria);
}
