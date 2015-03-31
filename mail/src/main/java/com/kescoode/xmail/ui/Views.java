package com.kescoode.xmail.ui;

import android.app.Activity;
import android.view.View;
import com.kescoode.xmail.R;

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

}
