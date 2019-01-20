package top.happing.mybatis.plugin;

import top.happing.mybatis.localism.LocalismFactory;
import top.happing.utils.BeanUtils;
import top.happing.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor implements Interceptor {

    private String dialectType;

    private String dialectClazz;

    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Object parameterObject = boundSql.getParameterObject();

        //获取分页参数对象
        Page<Object> page = null;
        if (parameterObject != null) {
            page = convertParameter(parameterObject, page);
        }

        //如果设置了分页对象，则进行分页
        if (page != null && page.getSize() != -1) {
            if (StringUtils.isBlank(boundSql.getSql())){
                return null;
            }
            String originalSql = boundSql.getSql().trim();

            page.setTotal(SqlUtils.getCount(originalSql, null,
                    mappedStatement, parameterObject, boundSql));

            //分页查询 本地化对象 修改数据库注意修改实现
            String pageSql = LocalismFactory.buildPaginationSql(page, originalSql, dialectType, dialectClazz);
            invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            //解决MyBatis 分页foreach 参数失效 start
            if (BeanUtils.getPropertyValue(boundSql, "metaParameters") != null) {
                MetaObject mo = (MetaObject) BeanUtils.getPropertyValue(boundSql, "metaParameters");
                BeanUtils.setPropertyValue(newBoundSql, "metaParameters", mo);
            }
            //解决MyBatis 分页foreach 参数失效 end
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

            invocation.getArgs()[0] = newMs;
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {
        try{
            if (parameterObject instanceof Page) {
                return (Page<Object>) parameterObject;
            } else {
                return (Page<Object>)BeanUtils.getPropertyValue(parameterObject, "page");
            }
        }catch (Exception e) {
            return null;
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(),
                ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            for (String keyProperty : ms.getKeyProperties()) {
                builder.keyProperty(keyProperty);
            }
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    public String getDialectType() {
        return dialectType;
    }

    public void setDialectType(String dialectType) {
        this.dialectType = dialectType;
    }

    public String getDialectClazz() {
        return dialectClazz;
    }

    public void setDialectClazz(String dialectClazz) {
        this.dialectClazz = dialectClazz;
    }
}
