package com.kescoode.adk.device;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 关于键盘的工具类
 *
 * @author Kesco Lin
 */
public class KeyBoard {
    private KeyBoard() {
        throw new UnsupportedOperationException("Cannot initial the class");
    }

    /**
     * 弹出软键盘
     *
     * @param context 上下文
     */
    public static void showSoftKeyBoard(Context context) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 强制弹出软键盘
     *
     * @param context 上下文
     */
    public static void showSoftKeyBoardForce(Context context) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     * @param view    获取焦点的视图
     */
    public static void hideSoftKeyBoard(Context context, View view) {
        view.clearFocus();
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 强制隐藏软键盘
     *
     * @param context 上下文
     * @param view    获取焦点的视图
     */
    public static void hideSoftKeyBoardForce(Context context, View view) {
        view.clearFocus();
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
