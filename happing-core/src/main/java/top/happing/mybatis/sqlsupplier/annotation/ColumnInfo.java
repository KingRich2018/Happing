package top.happing.mybatis.sqlsupplier.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ColumnInfo {

   String name() ;

}
