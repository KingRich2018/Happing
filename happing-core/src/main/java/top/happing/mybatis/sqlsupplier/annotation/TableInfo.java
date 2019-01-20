package top.happing.mybatis.sqlsupplier.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableInfo {

    String name();
}
