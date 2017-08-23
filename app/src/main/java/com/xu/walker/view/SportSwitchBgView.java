package com.xu.walker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xusn10 on 2017/8/22.
 * 运动选择背景自定义view
 */

public class SportSwitchBgView extends View {

    public SportSwitchBgView(Context context) {
        super(context);
    }

    public SportSwitchBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SportSwitchBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(80, 260, 200, 300);
        canvas.drawRoundRect(rectF, 20, 15, paint);
    }
}
