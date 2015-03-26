package com.kescoode.xmail.tool;

/**
 * 关于邮件的工具类
 *
 * @author Kesco Lin
 */
public class EmailUtil {
    private EmailUtil() {
        throw new UnsupportedOperationException("Can not initial the class");
    }

    public static String[] subDomain(String email) {
        return email.split("@");
    }
}
