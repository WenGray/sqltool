package xin.ryven.sqltool.tool;


/**
 * lower case, upper case, underline transfer
 *
 * @author Gray
 */
public class CaseUtils {

    /**
     * 非开头的大写字母转换成下划线，开头的大写字母改小写
     *
     * @param source 源数据
     * @return 转换数据
     */
    public static String upper2Underline(String source) {
        if (source == null || source.length() == 0) {
            return "";
        }
        char[] sChars = source.toCharArray();
        StringBuilder builder = new StringBuilder(sChars.length);
        for (int i = 0; i < sChars.length; i++) {
            char ch = sChars[i];
            if (isUpper(ch)) {
                //如果不是首字母，需要添加下划线
                if (i > 0) {
                    builder.append("_");
                }
                builder.append(toLower(ch));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    private static boolean isUpper(char c) {
        return c >= Const.UPPER_A && c <= Const.UPPER_Z;
    }


    /**
     * 转小写
     *
     * @param c 需要转换的char
     * @return 小写
     */
    private static char toLower(char c) {
        return isUpper(c) ? (char) (c + Const.UPPER_BETWEEN_LOWER) : c;
    }

    /**
     * 提取get属性
     *
     * @param source getUsername
     * @return username
     */
    public static String extractGet(String source) {
        if (!source.startsWith(Const.GET)) {
            throw new RuntimeException("不是 get 方法");
        }
        int getLength = Const.GET.length();
        if (source.length() <= getLength) {
            return "";
        }
        char[] s = source.toCharArray();
        char[] dist = new char[s.length - getLength];
        System.arraycopy(s, getLength + 1, dist, 1, s.length - getLength - 1);
        dist[0] = toLower(s[getLength]);
        return new String(dist);
    }
}
