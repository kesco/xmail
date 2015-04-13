package com.kescoode.xmail.ui.fragment.internal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 应用Fragment基类
 */
public abstract class AppFragment<T extends Activity> extends Fragment {

    protected T getAct() {
        return (T) getActivity();
    }

    protected void onActAttach(T activity) {
        /* Empty */
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onActAttach(getAct());
    }
}
