package hk.edu.polyu.cpce.error.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.text.MessageFormat;

public class EspStringUtils {

    public static boolean isNull(String str) {
        return EspObjectUtils.isNull(str);
    }

    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return isEmpty(str) || str.isBlank();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String formatString(@NonNull String template, @Nullable Object... args) {
        if (EspObjectUtils.isNull(template) || isBlank(template)) {
            return template;
        }
        if (EspObjectUtils.isNull(args) || args.length == 0) {
            return template;
        }
        return MessageFormat.format(template, args);
    }
}
