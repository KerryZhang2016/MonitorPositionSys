package com.bupt.model.utils.assist;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 辅助判断
 * 检测类， 检测各种对象是否为null或empty。
 * 
 * @author mty
 * @date 2013-6-10下午5:50:57
 *
 */
public class Check {

	public static boolean isEmpty(CharSequence str) {
		return isNull(str) || str.length() == 0;
	}

	public static boolean isEmpty(Object[] os) {
		return isNull(os) || os.length == 0;
	}

	public static boolean isEmpty(Collection<?> l) {
		return isNull(l) || l.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> m) {
		return isNull(m) || m.isEmpty();
	}

	public static boolean isNull(Object o) {
		return o == null;
	}

    /**
     * 验证邮箱格式是否正确
     * */
    public static boolean isValidEmail(String mail) {
        Pattern pattern = Pattern
                .compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

//    /**
//     * 验证密码格式是否正确
//     * */
//    public static boolean isValidPwd(String pwd) {
//
//    }
}
