package com.kescoode.xmail.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

    public static <T extends View> T findById(Activity target, int id) {
        return (T) target.findViewById(id);
    }

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
}
