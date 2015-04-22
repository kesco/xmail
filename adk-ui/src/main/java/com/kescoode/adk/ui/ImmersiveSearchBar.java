package com.kescoode.adk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.kescoode.adk.device.KeyBoard;
import com.kescoode.adk.view.Views;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Material Design式的SearchView
 *
 * @author Kesco Lin
 */
public class ImmersiveSearchBar extends Toolbar {
    public static interface ActionBackListener {
        void back();
    }

    private ImageView ivArrow;
    private EditText etSearch;
    private ImageView ivClose;

    private ActionBackListener backListener;

    private String hintString;

    public ImmersiveSearchBar(Context context) {
        this(context, null);
    }

    public ImmersiveSearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImmersiveSearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct(context, attrs, defStyleAttr);
    }

    private void construct(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_immersive_search, this, true);
        ivArrow = Views.findById(this, R.id.iv_arrow_back);
        etSearch = Views.findById(this, R.id.et_search);
        ivClose = Views.findById(this, R.id.iv_close);
        readAttr(context, attrs);
        buildView();
        attachListener();
    }

    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImmersiveSearchBarAttrs);
        if (a != null) {
            try {
                hintString = a.getString(R.styleable.ImmersiveSearchBarAttrs_hint);
            } finally {
                a.recycle();
            }
        }
    }

    private void buildView() {
        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.material_actionbar_size_minimal));

        if (!TextUtils.isEmpty(hintString)) {
            etSearch.setHint(hintString);
        }
    }

    private void attachListener() {
        ivArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backListener != null) {
                    backListener.back();
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    ivClose.setVisibility(VISIBLE);
                } else {
                    ivClose.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (etSearch.getVisibility() == VISIBLE) {
        }
    }

    /**
     * 显示搜索栏，有动画
     *
     * @param activity 搜索栏依附的
     */
    public void appear(Activity activity) {
        setVisibility(VISIBLE);

        FrameLayout layout = (FrameLayout) activity.getWindow().getDecorView()
                .findViewById(android.R.id.content);
        RelativeLayout root = (RelativeLayout) findViewById(R.id.rl_root);
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                r.getDisplayMetrics());
        int cx = layout.getLeft() + layout.getRight();
        int cy = layout.getTop();

        int finalRadius = (int) Math.max(layout.getWidth(), px);

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
                root, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationEnd() {
                showKeyboard(true);
            }

            @Override
            public void onAnimationRepeat() {
            }

            @Override
            public void onAnimationStart() {
            }

        });
        animator.start();
    }

    public void setBackListener(ActionBackListener backListener) {
        this.backListener = backListener;
    }

    public void setHint(String hint) {
        etSearch.setHint(hint);
    }

    public void setHint(@StringRes int hint) {
        setHint(getContext().getResources().getString(hint));
    }

    public String getResult() {
        return etSearch.getText().toString();
    }

    public void showKeyboard(boolean show) {
        if (show) {
            etSearch.requestFocus();
            KeyBoard.showSoftKeyBoard(getContext());
        } else {
            KeyBoard.hideSoftKeyBoard(getContext(), etSearch);
        }
    }

}
