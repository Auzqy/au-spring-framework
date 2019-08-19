package top.auzqy.spring.util;

import com.sun.istack.internal.Nullable;

/**
 * description:  关于字符串的操作的工具类
 * createTime: 2019-08-19 13:27
 * @author au
 */
public class StringUtils {

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}

}
