package top.happing.mybatis.query;

import top.happing.framework.domain.model.BaseEntity;
import top.happing.mybatis.plugin.Page;
import top.happing.mybatis.sqlsupplier.EntityInfoHelper;
import top.happing.utils.BeanUtils;
import top.happing.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class EntityQueryCriteria implements QueryCriteria {

    private BaseEntity entity;
    private Class<?> entityClass;

    private List<String> criteria = new LinkedList<String>();

    private Page page = new Page(1, Integer.MAX_VALUE);

    EntityInfoHelper entityInfoHelper = EntityInfoHelper.instance();


    public EntityQueryCriteria(BaseEntity entity) {
        this.entity = entity;
        this.entityClass = entity.getClass();
    }

    public EntityQueryCriteria(BaseEntity entity, Page page) {
        this.entity = entity;
        this.entityClass = entity.getClass();
        this.page = page;
    }

    public EntityQueryCriteria andIs(String field) {
        try {
            Object val = BeanUtils.getPropertyValue(entity, field);
            if (StringUtils.trimToNull(val) != null) {
                if (val instanceof String) {
                    criteria.add(" and " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                            + "='" + val + "'");
                } else {
                    criteria.add(" and " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                            + "=" + val);
                }
            }
        } catch (Exception e) {

        }
        return this;
    }

    public EntityQueryCriteria orIs(String field) {
        try {
            Object val = BeanUtils.getPropertyValue(entity, field);
            if (StringUtils.trimToNull(val) != null) {
                if (val instanceof String) {
                    criteria.add(" or " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                            + "='"  + val + "'");
                } else {
                    criteria.add(" or " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                            + "="  + val);
                }
            }
        } catch (Exception e) {

        }
        return this;
    }

    public EntityQueryCriteria andLike(String field) {
        try {
            Object val = BeanUtils.getPropertyValue(entity, field);
            if (StringUtils.trimToNull(val) != null) {
                if (val instanceof String) {
                    criteria.add(" and " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                            + " like '" + val + "%'");
                }
            }
        } catch (Exception e) {

        }
        return this;
    }

    public EntityQueryCriteria orLike(String field) {
        try {
            Object val = BeanUtils.getPropertyValue(entity, field);
            if (StringUtils.trimToNull(val) != null) {
                if (val instanceof String) {
                    criteria.add(" or " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                            + " like '" + val + "%'");
                }
            }
        } catch (Exception e) {

        }
        return this;
    }

    public EntityQueryCriteria sortAsc(String field) {
        try {
            criteria.add(" ORDER BY " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                    + " ASC ");
        } catch (Exception e) {

        }
        return this;
    }

    public EntityQueryCriteria sortDesc(String field) {
        try {
            criteria.add(" ORDER BY " + entityInfoHelper.determineColumnName(entityClass.getDeclaredField(field))
                    + " DESC ");
        } catch (Exception e) {

        }
        return this;
    }

    @Override
    public String getCondition() {
        if (entity == null) {
            return null;
        }

        StringBuilder condition = new StringBuilder();
        for (String eq : criteria) {
            condition.append(eq);
        }

        return condition.toString();
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
