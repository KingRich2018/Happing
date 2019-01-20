package top.happing.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BeanUtils {

    private static final Log LOG = LogFactory.getLog(BeanUtils.class);

    public static Type[] getActualTypeArgs(Class<?> clazz) {

        if (ParameterizedType.class.isAssignableFrom(clazz
                .getGenericSuperclass().getClass())) {
            Type[] actualTypeArgs = ((ParameterizedType) clazz
                    .getGenericSuperclass()).getActualTypeArguments();
            return actualTypeArgs;
        }
        return null;
    }

    public static Object getPropertyValue(Object bean, String propName) {
        if (bean == null || StringUtils.isBlank(propName)) {
            return null;
        }

        Object value = null;
        if (PropertyUtils.isReadable(bean, propName)) {
            try {
                value = PropertyUtils.getPropertyDescriptor(bean, propName)
                        .getReadMethod().invoke(bean);
            } catch (Exception e) {
                LOG.error("", e);
            }
        }

        return value;
    }

    public static void setPropertyValue(Object bean, String propName, Object value) {
        if (bean == null || StringUtils.isBlank(propName)) {
            return;
        }

        if (PropertyUtils.isWriteable(bean, propName)) {
            try {
                PropertyUtils.getPropertyDescriptor(bean, propName)
                        .getWriteMethod().invoke(bean, value);
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
    }

    public static Field[] mergeDestClazzFields(Class<?> clazz, Class<?> destClazz) {
        Field[] fields = null;
        Field[] childFields = clazz.getDeclaredFields();

        Field[] parentFields = destClazz.getDeclaredFields();

        fields = new Field[childFields.length + parentFields.length];
        System.arraycopy(childFields, 0, fields, 0, childFields.length);
        System.arraycopy(parentFields, 0, fields, childFields.length,
                parentFields.length);
        return fields;
    }
}
