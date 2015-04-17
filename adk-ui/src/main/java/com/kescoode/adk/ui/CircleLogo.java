package com.kescoode.adk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import com.kescoode.adk.view.Views;

/**
 * 圆形的Logo，用于如个人头像，标题栏等等
 *
 * @author Kesco Lin
 */
public class CircleLogo extends View {
    private String logoText;
    private int logoColor;
    private int logoTextColor;
    private int logoSize;
    private float logoTextSize;

    public CircleLogo(Context context) {
        this(context, null);
    }

    public CircleLogo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLogo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleLogo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        construct(context, attrs, defStyleAttr);
    }

    private void construct(Context context, AttributeSet attrs, int defStyleAttr) {
        readAttr(context, attrs, defStyleAttr);
        Views.setBackground(this, createDrawable(logoColor));
    }

    private void readAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleLogoAttrs, defStyleAttr, 0);
        if (a != null) {
            try {
                logoColor = a.getColor(R.styleable.CircleLogoAttrs_logo_color,
                        getResources().getColor(R.color.adk_grey_blue));
                logoTextColor = a.getColor(R.styleable.CircleLogoAttrs_logo_text_color,
                        getResources().getColor(R.color.adk_white));
                logoSize = a.getDimensionPixelSize(R.styleable.CircleLogoAttrs_logo_size,
                        getResources().getDimensionPixelSize(R.dimen.default_circle_logo_size));
                logoTextSize = a.getDimension(R.styleable.CircleLogoAttrs_logo_text_size,
                        getResources().getDimension(R.dimen.default_circle_logo_text_size));
                String defaultText = a.getString(R.styleable.CircleLogoAttrs_logo_text);
                logoText = defaultText == null ?
                        getResources().getString(R.string.default_circle_logo_text) : defaultText;
            } finally {
                a.recycle();
            }
        }
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas, logoText);
    }

    private void drawText(Canvas canvas, String text) {
        Paint paint = new Paint();
        paint.setColor(logoTextColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(logoTextSize);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextAlign(Paint.Align.CENTER);

        float xPos = canvas.getWidth() / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = canvas.getHeight();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // TODO: 垂直置中的算法仍还要优化
        float yPos = (height - fontHeight) / 2 - fontMetrics.top;
        canvas.drawText(text, xPos, yPos, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(logoSize, logoSize);
    }

    public void setLogoText(@NonNull String text) {
        if (text.length() != 1) {
            throw new IllegalArgumentException("The input text must have 1 character length.");
        }
        logoText = text;
        invalidate();
    }

    public void setLogoColor(int color) {
        logoColor = color;
        Views.setBackground(this, createDrawable(color));
    }

}
