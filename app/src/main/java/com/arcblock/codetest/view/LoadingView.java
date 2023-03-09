package com.arcblock.codetest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.arcblock.codetest.R;

public class LoadingView extends View {


    private Paint mPaint;// 画笔

    private int mWhiteRadius;// 白色
    private int mDrawWhiteRadius;// 白色

    private int mRedRadius;// 红色
    private int mDrawRedRadius;// 红色

    private ValueAnimator mValueAnimator;// 动画

    private int mDefaultWidth = 75;// 默认宽度
    private int mDefaultHeight = 75;// 默认高度

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // 初始化半径
//        mWhiteRadius = mDrawWhiteRadius = 25;
//        mRedRadius = mDrawRedRadius = 50;

        // 初始化画笔
        mPaint = new Paint();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRedCircle(mPaint, canvas);

        drawWhiteCircle(mPaint, canvas);
    }

    private void drawWhiteCircle(Paint paint, Canvas canvas) {
        paint.setColor(Color.TRANSPARENT);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mDrawWhiteRadius, paint);
    }

    private void drawRedCircle(Paint paint, Canvas canvas) {
        paint.setColor(getContext().getResources().getColor(R.color.app_main_color));

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mDrawRedRadius, paint);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = MeasureSpec.getSize(widthMeasureSpec);
        mRedRadius = mDrawRedRadius = size / 2;
        mWhiteRadius = mDrawWhiteRadius = mRedRadius / 2;
    }

    private void startAnim() {
        mValueAnimator = ValueAnimator.ofFloat(0, 0.25f, 0);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(800);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            float x;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = (float) animation.getAnimatedValue();

                mDrawWhiteRadius = (int) (mWhiteRadius * (1 + x));

                mDrawRedRadius = (int) (mRedRadius * (1 - x));

                postInvalidate();
            }
        });
        mValueAnimator.start();
    }

    public void stopAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator.end();
            mValueAnimator = null;
        }
    }

}
