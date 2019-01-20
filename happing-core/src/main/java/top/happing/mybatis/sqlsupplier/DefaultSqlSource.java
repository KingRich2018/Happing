package top.happing.mybatis.sqlsupplier;

import top.happing.framework.domain.mapper.BaseMapper;
import top.happing.framework.domain.model.BaseEntity;
import top.happing.mybatis.sqlsupplier.annotation.NotColumn;
import top.happing.utils.BeanUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSource {

    private List<String> ignoreFieldList = new ArrayList<String>();

    public DefaultSqlSource() {
        ignoreFieldList.add("serialVersionUID");
    }

    public SqlSource getSqlSource(Class<?> mapperClass, Method method,
                                  Configuration configuration, LanguageDriver languageDriver) {
        SqlMethod sqlMethod = null;
        try {
            sqlMethod = SqlMethod.valueOf(method.getName());
        } catch (Exception e) {
            return null;
        }

        Class<?> modelClass = extractModelClass(mapperClass);
        Class<?> parameterType = extractParameterType(method);

        switch (sqlMethod) {
            case insert:
                return languageDriver.createSqlSource(configuration, buildSqlInsert(modelClass), parameterType);
            case deleteById:
                return languageDriver.createSqlSource(configuration, buildSqlDeleteById(modelClass), parameterType);
            case deleteByMap:
                return languageDriver.createSqlSource(configuration, buildSqlDeleteByMap(modelClass), parameterType);
            case delete:
                return languageDriver.createSqlSource(configuration, buildSqlDelete(modelClass), parameterType);
            case deleteByIdList:
                return languageDriver.createSqlSource(configuration, buildSqlDeleteByIdList(modelClass), parameterType);
            case updateById:
                return languageDriver.createSqlSource(configuration, buildSqlUpdateById(modelClass), parameterType);
            case update:
                return languageDriver.createSqlSource(configuration, buildSqlUpdate(modelClass), parameterType);
            case selectById:
                return new RawSqlSource(configuration, buildSqlSelectById(false, modelClass), parameterType);
            case selectByIdList:
                return languageDriver.createSqlSource(configuration, buildSqlSelectById(true, modelClass), parameterType);
            case selectByMap:
                return languageDriver.createSqlSource(configuration, buildSqlSelectByMap(modelClass), parameterType);
            case selectOne:
                return languageDriver.createSqlSource(configuration, buildSqlSelectOne(modelClass), parameterType);
            case selectCount:
                return languageDriver.createSqlSource(configuration, buildSqlSelectCount(modelClass), parameterType);
            case selectList:
                return languageDriver.createSqlSource(configuration, buildSqlSelectList(modelClass), parameterType);
            case selectOneByCriteria:
                return languageDriver.createSqlSource(configuration, buildSqlSelectOneByCriteria(modelClass), parameterType);
            case selectCountByCriteria:
                return languageDriver.createSqlSource(configuration, buildSqlSelectCountByCriteria(modelClass), parameterType);
            case selectListByCriteria:
                return languageDriver.createSqlSource(configuration, buildSqlSelectListByCriteria(modelClass), parameterType);
            default:
                throw new BuilderException("Could not find value method on default sql source");
        }
    }

    private String buildSqlInsert(Class<?> modelClass) {
        StringBuilder fieldBuilder = new StringBuilder("\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");
        StringBuilder placeholderBuilder = new StringBuilder("\n<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n");

        Field[] fields = BeanUtils.mergeDestClazzFields(modelClass, BaseEntity.class);

        for (Field field : fields) {
            NotColumn notColumn = field.getAnnotation(NotColumn.class);
            if (notColumn == null && !ignoreFieldList.contains(field.getName())) {
                String columnName = determineColumnName(field);
                String fieldName = field.getName();

                fieldBuilder.append("\n<if test=\"").append(fieldName).append(" != null\">");
                fieldBuilder.append(columnName).append(", </if> ");
                placeholderBuilder.append("\n<if test=\"").append(fieldName).append(" != null\">");
                placeholderBuilder.append("#{").append(fieldName).append("}, </if>");
            }
        }
        fieldBuilder.append("\n</trim>");
        placeholderBuilder.append("\n</trim>");

        return String.format(SqlMethod.insert.getScript(), determineTableName(modelClass), fieldBuilder.toString(),
                placeholderBuilder.toString());
    }

    private String buildSqlDeleteById(Class<?> modelClass) {
        return String.format(SqlMethod.deleteById.getScript(), determineTableName(modelClass), determineIdName(modelClass),
                determineIdName(modelClass));
    }

    private String buildSqlDeleteByMap(Class<?> modelClass) {
        return String.format(SqlMethod.deleteByMap.getScript(), determineTableName(modelClass), sqlWhere(modelClass, true));
    }

    private String buildSqlDelete(Class<?> modelClass) {
        return String.format(SqlMethod.delete.getScript(), determineTableName(modelClass), sqlWhere(modelClass, true));
    }

    private String buildSqlDeleteByIdList(Class<?> modelClass) {
        return String.format(SqlMethod.deleteByIdList.getScript(), determineTableName(modelClass),
                determineIdName(modelClass), sqlWhereByIdList());
    }

    private String buildSqlUpdateById(Class<?> modelClass) {
        return String.format(SqlMethod.updateById.getScript(), determineTableName(modelClass), sqlSetValue(modelClass),
                determineIdName(modelClass), determineIdName(modelClass));
    }

    private String buildSqlUpdate(Class<?> modelClass) {
        return String.format(SqlMethod.update.getScript(), determineTableName(modelClass), sqlSetValue(modelClass),
                sqlWhere(modelClass, false));
    }

    private String buildSqlSelectById(boolean batch, Class<?> modelClass) {
        SqlMethod sqlMethod = SqlMethod.selectById;
        if (batch) {
            sqlMethod = SqlMethod.selectByIdList;
            return String.format(sqlMethod.getScript(), sqlSelectColumns(modelClass),
                    determineTableName(modelClass), determineIdName(modelClass), sqlWhereByIdList());
        }
        return String.format(sqlMethod.getScript(), sqlSelectColumns(modelClass),
                determineTableName(modelClass), determineIdName(modelClass), determineIdName(modelClass));
    }

    private String buildSqlSelectByMap(Class<?> modelClass) {
        return String.format(SqlMethod.selectByMap.getScript(), sqlSelectColumns(modelClass),
                determineTableName(modelClass), sqlWhere(modelClass, true));
    }

    private String buildSqlSelectOne(Class<?> modelClass) {
        return String.format(SqlMethod.selectOne.getScript(), sqlSelectColumns(modelClass),
                determineTableName(modelClass), sqlWhere(modelClass, true));
    }

    private String buildSqlSelectCount(Class<?> modelClass) {
        return String.format(SqlMethod.selectCount.getScript(),
                determineTableName(modelClass), sqlWhere(modelClass, true));
    }

    private String buildSqlSelectList(Class<?> modelClass) {
        return String.format(SqlMethod.selectList.getScript(), sqlSelectColumns(modelClass),
                determineTableName(modelClass), sqlWhere(modelClass, true));
    }

    private String buildSqlSelectOneByCriteria(Class<?> modelClass) {
        return String.format(SqlMethod.selectOneByCriteria.getScript(), sqlSelectColumns(modelClass),
                determineTableName(modelClass), sqlWhereByCriteria(modelClass));
    }

    private String buildSqlSelectCountByCriteria(Class<?> modelClass) {
        return String.format(SqlMethod.selectCountByCriteria.getScript(),
                determineTableName(modelClass), sqlWhereByCriteria(modelClass));
    }

    private String buildSqlSelectListByCriteria(Class<?> modelClass) {
        return String.format(SqlMethod.selectListByCriteria.getScript(), sqlSelectColumns(modelClass),
                determineTableName(modelClass), sqlWhereByCriteria(modelClass));
    }

    private String sqlSelectColumns(Class<?> modelClass) {
        StringBuilder columns = new StringBuilder();
        Field[] fields = BeanUtils.mergeDestClazzFields(modelClass, BaseEntity.class);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            NotColumn notColumn = field.getAnnotation(NotColumn.class);
            if (notColumn == null && !ignoreFieldList.contains(field.getName())) {
                columns.append(determineColumnName(field)).append(" AS ")
                        .append(field.getName()).append(",");
            }
        }
        columns.deleteCharAt(columns.length() - 1);

        return columns.toString();
    }

    private String determineIdName(Class<?> modelClass) {
        return EntityInfoHelper.instance().determineIdName(modelClass);
    }

    private String determineTableName(Class<?> modelClass) {
        return EntityInfoHelper.instance().determineTableName(modelClass);
    }

    private String determineColumnName(Field field) {
        return EntityInfoHelper.instance().determineColumnName(field);
    }

    private Class<?> extractParameterType(Method method) {
        Class<?> parameterType = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> currentParameterType : parameterTypes) {
            if (!RowBounds.class.isAssignableFrom(currentParameterType) && !ResultHandler.class.isAssignableFrom(currentParameterType)) {
                if (parameterType == null) {
                    parameterType = currentParameterType;
                } else {
                    // issue #135
                    parameterType = MapperMethod.ParamMap.class;
                }
            }
        }
        return parameterType;
    }

    private Class<?> extractModelClass(Class<?> mapperClass) {
        if (mapperClass == BaseMapper.class) {
            return null;
        } else {
            Type[] types = mapperClass.getGenericInterfaces();
            ParameterizedType target = null;
            for (Type type : types) {
                if (type instanceof ParameterizedType && BaseMapper.class.isAssignableFrom(mapperClass)) {
                    target = (ParameterizedType) type;
                    break;
                }
            }
            return (Class<?>) target.getActualTypeArguments()[0];
        }
    }

    private String sqlSetValue(Class<?> modelClass) {
        StringBuilder set = new StringBuilder();
        set.append("<trim prefix=\"SET\" suffixOverrides=\",\">");

        Field[] fields = BeanUtils.mergeDestClazzFields(modelClass, BaseEntity.class);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            String columnName = determineColumnName(field);
            NotColumn notColumn = field.getAnnotation(NotColumn.class);
            if (notColumn == null && !ignoreFieldList.contains(field.getName()) && !fieldName.equals(determineIdName(modelClass))) {
                set.append("\n<if test=\"").append(fieldName).append(" != null\">");
                set.append(columnName).append("=#{").append(fieldName).append("}, </if>");
            }
        }
        set.append("\n</trim>");
        return set.toString();
    }

    private String sqlWhereByIdList() {
        StringBuilder where = new StringBuilder();
        where.append("\n<foreach item=\"item\" index=\"index\" collection=\"list\" separator=\",\">");
        where.append("#{item}");
        where.append("\n</foreach>");
        return where.toString();
    }

    private String sqlWhereByMap() {
        StringBuilder where = new StringBuilder();
        where.append("\n<if test=\"columnMap!=null and !columnMap.isEmpty\">");
        where.append("\n<where>");
        where.append("\n<foreach collection=\"columnMap.keys\" item=\"k\" separator=\"AND\">");
        where.append("\n<if test=\"columnMap[k] != null\">");
        where.append("\n${k} = #{columnMap[${k}]}");
        where.append("\n</if>");
        where.append("\n</foreach>");
        where.append("\n</where>");
        where.append("\n</if>");
        return where.toString();
    }

    private String sqlWhere(Class<?> modelClass, boolean merge) {
        StringBuilder where = new StringBuilder();
        where.append("\n where 1=1 ");

        Field[] fields = null;
        if (merge) {
            fields = BeanUtils.mergeDestClazzFields(modelClass, BaseEntity.class);
        } else {
            fields = modelClass.getDeclaredFields();
        }

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            NotColumn notColumn = field.getAnnotation(NotColumn.class);
            if (notColumn == null && !ignoreFieldList.contains(field.getName())) {
                where.append("\n<if test=\"").append(field.getName()).append("!=null ");
                if (field.getType().isAssignableFrom(String.class)) {
                    where.append(" and ").append(field.getName()).append("!=''");
                }
                where.append("\"> \n and ");
                where.append(determineColumnName(field)).append("=#{").append(field.getName()).append("}");
                where.append("\n</if>");
            }
        }

        return where.toString();
    }

    private String sqlWhereByCriteria(Class<?> modelClass) {
        StringBuilder where = new StringBuilder();
        where.append("\n where 1=1 ");
        where.append("\n <if test=\"condition!=null and condition!=''\"> ");
        where.append("\n ${condition}");
        where.append("\n </if>");

        return where.toString();
    }

}
