package xin.ryven.sqltool.tool;

import java.util.Objects;

/**
 * @author Gray
 */
public class ArrayUtils {

    public static boolean inStringArray(String test, String[] array) {
        for (String a : array) {
            if (Objects.equals(test, a)) {
                return true;
            }
        }
        return false;
    }

}
