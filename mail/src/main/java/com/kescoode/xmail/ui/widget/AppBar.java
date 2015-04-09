package com.kescoode.xmail.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.kescoode.xmail.R;
import com.kescoode.adk.view.Views;
import com.kescoode.xmail.ui.widget.internal.InsetColorDrawable;

/**
 * 包含{@link XToolbar}的Container，主要为了解决自定义Toolbar没阴影的问题
 *
 * @author Kesco Lin
 */
public class AppBar extends LinearLayout {
    private XToolbar toolbar;
    private int shadowHeightPx;
    private InsetColorDrawable background;
    private boolean hasMarginSet = false;

    public AppBar(Context context) {
        this(context, null);
    }

    public AppBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.appBarStyle);
    }

    public AppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        readAttr(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(VERTICAL);
        readAttr(context, attrs, defStyleAttr);
    }

    private void readAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBarAttrs,
                defStyleAttr, R.style.Widget_MailTheme_AppBar);
        if (a != null) {
            try {
                int appBarColor = a.getColor(R.styleable.AppBarAttrs_toolbar_background,
                        getResources().getColor(R.color.blue_sky_1));
                int shadowHeight = a.getDimensionPixelSize(R.styleable.AppBarAttrs_toolbar_shadow_height,
                        getResources().getDimensionPixelSize(R.dimen.toolbar_shadow_height));
                shadowHeightPx = shadowHeight;
                initBackground(appBarColor, shadowHeight);
                int appBarLayoutId = a.getResourceId(R.styleable.AppBarAttrs_toolbar_layout, R.layout.toolbar);
                inflateToolbar(appBarLayoutId);
            } finally {
                a.recycle();
            }
        }
    }

    private void inflateToolbar(int barXml) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(barXml, this, true);
    }

    private void initBackground(int appBarColor, int shadowHeight) {
        Rect insets = new Rect(0, 0, 0, shadowHeight);
        this.background = new InsetColorDrawable(appBarColor, insets);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(background);
        } else {
            super.setBackgroundDrawable(background);
        }
    }

    public void setToolbar(XToolbar bar) {
        toolbar = bar;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addShadowView();
    }

    private void addShadowView() {
        if (shadowHeightPx == 0) {
            return;
        }
        View shadowView = new View(getContext());
        Drawable drawable = Views.getDrawable(this, R.drawable.shape_appbar_shadow);
        Views.setBackground(shadowView, drawable);
        addView(shadowView, LayoutParams.MATCH_PARENT, shadowHeightPx);
    }

    @Override
    public void setBackgroundColor(int color) {
        background.setColor(color);
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation != VERTICAL) {
            throw new UnsupportedOperationException("AppBar cannot set orientation to other.");
        }
        super.setOrientation(orientation);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMarginsWithoutShadow();
    }

    /**
     * 修改因为加入阴影导致的空白位移</p>
     * 有Bug，暂缓使用
     */
    private void setMarginsWithoutShadow() {
        if (!hasMarginSet) {
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int bottomMargin = layoutParams.bottomMargin - shadowHeightPx;
                layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin,
                        bottomMargin);
                requestLayout();
                hasMarginSet = true;
            }
        }
    }
}
