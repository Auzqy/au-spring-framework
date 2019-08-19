package top.auzqy.spring.util;

/**
 * description:  类型转换相关的 工具类
 * createTime: 2019-08-19 13:55
 * @author au
 */
public class TypeCastUtils {

    /**
     * description:  将一个 String 类型的值转换为目标类型的值
     * createTime: 2019-08-19 14:00
     * @author au
     * @param srcValue  String 类型的值
     * @param targetClassType   目标类型
     * @return
     */
    public static Object convertParamType(String srcValue, Class<?> targetClassType) {
        if (targetClassType == int.class) {
            return Integer.valueOf(srcValue);
        } else if (targetClassType == float.class) {
            return Float.valueOf(srcValue);
        } else if (targetClassType == double.class) {
            return Double.valueOf(srcValue);
        } else if (targetClassType == short.class) {
            return Short.valueOf(srcValue);
        } else if (targetClassType == long.class) {
            return Long.valueOf(srcValue);
        } else if (targetClassType == byte.class) {
            return Byte.valueOf(srcValue);
        } else {
            return srcValue;
        }
    }
}
