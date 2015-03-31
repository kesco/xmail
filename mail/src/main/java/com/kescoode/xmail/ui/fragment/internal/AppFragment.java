package com.kescoode.xmail.ui.fragment.internal;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * 应用Fragment基类
 */
public abstract class AppFragment<T extends Activity> extends Fragment {

    protected T getAct() {
        return (T) getActivity();
    }

}
