package com.kescoode.adk.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

/**
 * View工具类
 *
 * @author Kesco Lin
 */
public class Views {

    private Views() {
        throw new UnsupportedOperationException("Can not intial the class");
    }

    /**
     * 获取相应的View
     *
     * @param target 获取View的目标
     * @param id View的ID
     * @param <T> View类型
     * @return 获取的View
     */
    public static <T extends View> T findById(Activity target, int id) {
        return (T) target.findViewById(id);
    }

    /**
     * 获取相应的View
     *
     * @param target 获取View的目标
     * @param id View的ID
     * @param <T> View类型
     * @return 获取的View
     */
    public static <T extends View> T findById(View target, int id) {
        return (T) target.findViewById(id);
    }

    public static void setBackground(View target, Drawable drawable) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            target.setBackground(drawable);
        } else {
            target.setBackgroundDrawable(drawable);
        }
    }

    public static Drawable getDrawable(View target, int resourseId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return target.getResources().getDrawable(resourseId, null);
        } else {
            return target.getResources().getDrawable(resourseId);
        }
    }

    /**
     * View延时执行的静态方法
     *
     * @param run 需要执行的{@link Runnable}
     * @param delayed 延时
     */
    public static void delayExecuteOnUi(Runnable run, long delayed) {
        new Handler(Looper.getMainLooper()).postDelayed(run, delayed);
    }
}
