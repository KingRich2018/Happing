package top.happing.mybatis.localism;

import top.happing.exception.AppException;
import top.happing.mybatis.localisms.MySqlLocalism;
import top.happing.mybatis.plugin.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.session.RowBounds;

public class LocalismFactory {

    public static String buildPaginationSql(Pagination page, String buildSql, String dialectType, String dialectClazz)
            throws Exception {
        return getiDialect(dialectType, dialectClazz).buildPaginationSql(buildSql, page.getOffset(), page.getSize());
    }

    public static String buildPaginationSql(RowBounds rowBounds, String buildSql, String dialectType, String dialectClazz)
            throws Exception {
        return getiDialect(dialectType, dialectClazz).buildPaginationSql(buildSql, rowBounds.getOffset(), rowBounds.getLimit());
    }

    private static Localism getiDialect(String dialectType, String dialectClazz) throws Exception {
        Localism dialect = null;
        if (StringUtils.isNotEmpty(dialectType)) {
            dialect = getDialectByDbType(dialectType);
        } else {
            if (StringUtils.isNotEmpty(dialectClazz)) {
                try {
                    Class<?> clazz = Class.forName(dialectClazz);
                    if (Localism.class.isAssignableFrom(clazz)) {
                        dialect = (Localism) clazz.newInstance();
                    }
                } catch (ClassNotFoundException e) {
                    throw new ExecutorException("Class :" + dialectClazz + " is not found");
                }
            }
        }

        if (dialect == null) {
            throw new ExecutorException("The value of the dialect property in mybatis configuration is not defined.");
        }
        return dialect;
    }

    private static Localism getDialectByDbType(String dbtype) throws Exception {
        if ("mysql".equals(dbtype)) {
            return MySqlLocalism.instance;
        }  else {
            throw new AppException("Not support db type " + dbtype);
        }
    }
}
