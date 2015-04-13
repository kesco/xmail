package com.kescoode.xmail.exception;

/**
 * 扩展{@link RuntimeException},主要是字符串的处理
 *
 * @author Kesco Lin
 */
public class XDynamicException extends RuntimeException {

    public XDynamicException(String detailMsg, Object... args) {
        super(String.format(detailMsg, translateArgs(args)));
    }

    private static String[] translateArgs(Object... args) {
        String[] sArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            sArgs[i] = String.valueOf(args[i]);
        }
        return sArgs;
    }
}
