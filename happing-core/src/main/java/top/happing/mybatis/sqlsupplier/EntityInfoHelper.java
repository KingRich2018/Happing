package top.happing.mybatis.sqlsupplier;

import top.happing.mybatis.sqlsupplier.annotation.ColumnInfo;
import top.happing.mybatis.sqlsupplier.annotation.TableInfo;

import java.lang.reflect.Field;

public class EntityInfoHelper {

    private static final EntityInfoHelper instance = new EntityInfoHelper();

    private EntityInfoHelper() {}

    public static EntityInfoHelper instance() {
        return instance;
    }

    public String determineIdName(Class<?> modelClass) {
        return "id";
    }

    public String determineTableName(Class<?> modelClass) {
        TableInfo tableInfo = modelClass.getAnnotation(TableInfo.class);
        return tableInfo == null ? split("t_", modelClass.getSimpleName()) : tableInfo.name();
    }

    public String determineColumnName(Field field) {
        ColumnInfo columnInfo = field.getAnnotation(ColumnInfo.class);
        return columnInfo == null ? split("", field.getName()) : columnInfo.name();
    }

    private String split(String head, String src) {
        StringBuffer dest = new StringBuffer(head);
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    dest.append("_").append(c);
                } else {
                    dest.append(c);
                }
            } else {
                dest.append(c);
            }
        }
        return dest.toString().toLowerCase();
    }
}
