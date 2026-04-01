package hk.edu.polyu.cpce.error.util;

import java.util.Objects;

public class EspObjectUtils {

    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

}