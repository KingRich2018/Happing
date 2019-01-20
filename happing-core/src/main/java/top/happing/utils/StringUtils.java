package top.happing.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

    public static Object trimToNull(Object object) {
        if (object instanceof String) {
            return trimToNull((String) object);
        } else {
            return object;
        }
    }
}
