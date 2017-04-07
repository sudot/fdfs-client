package net.sudot.fdfs.util;

import java.util.Collection;

/**
 * 验证操作类
 * Created by tangjialin on 2017-04-07 0007.
 */
public class Validate {

    /**
     * 字符空判断
     * @param chars   需要判断的参数
     * @param message 参数为空后需要提示的信息模版
     * @param values  信息模版的参数
     * @param <T> 字符类型泛型
     */
    public static <T extends CharSequence> void notBlank(T chars, String message, Object... values) {
        if (chars == null) {
            throw new NullPointerException(values == null || values.length == 0 ? message : String.format(message, values));
        } else if (isBlank(chars)) {
            throw new IllegalArgumentException(values == null || values.length == 0 ? message : String.format(message, values));
        }
    }

    /**
     * 集合空判断
     * @param collection 需要判断的参数
     * @param message 参数为空后需要提示的信息模版
     * @param values 信息模版的参数
     * @param <T> 集合类型泛型
     */
    public static <T extends Collection<?>> void notEmpty(T collection, String message, Object... values) {
        if (collection == null) {
            throw new NullPointerException(values == null || values.length == 0 ? message : String.format(message, values));
        } else if (collection.isEmpty()) {
            throw new IllegalArgumentException(values == null || values.length == 0 ? message : String.format(message, values));
        }
    }

    /**
     * 对象空判断
     * @param object 需要判断的参数
     * @param message 参数为空后需要提示的信息模版
     * @param values 信息模版的参数
     * @param <T> 集合类型泛型
     */
    public static <T> void notNull(T object, String message, Object... values) {
        if (object == null) {
            throw new NullPointerException(values == null || values.length == 0 ? message : String.format(message, values));
        }
    }

    /**
     * 字符空检测
     * @param cs 检测的字符
     * @return 返回检测结果
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
