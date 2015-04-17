package com.kescoode.xmail.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import com.kescoode.xmail.R;

/**
 * 分割线View
 *
 * @author Kesco Lin
 */
public class LineDivider extends View {

    private Paint dividerPaint;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineDivider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        construct(context);
    }

    public LineDivider(Context context) {
        this(context, null);
    }

    public LineDivider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct(context);
    }

    private void construct(Context context) {
        dividerPaint = new Paint();
        dividerPaint.setStrokeCap(Paint.Cap.SQUARE);
        // TODO: 增加自定义Color和宽度
        dividerPaint.setColor(getResources().getColor(R.color.gray));
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float height = canvas.getHeight();
        dividerPaint.setStrokeWidth(height);
        canvas.drawLine(getLeft(), height / 2, getRight(), height / 2, dividerPaint);
    }
}
