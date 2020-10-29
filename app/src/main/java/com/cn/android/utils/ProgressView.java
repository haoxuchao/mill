package com.cn.android.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.cn.android.R;



/**
 * Created by mac1010 on 2018/5/14.
 */

public class ProgressView extends View {



    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private int length, mCircleXY;
    private float mRadius;
    public  static boolean ismv = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        length = w;
        mCircleXY = length / 2;
        mRadius = (float) (length / 2);
    }


    private Paint mArcPaint, mCirclePaint, mTextPaint;
    private int arcWidth = 10;

    private void initView() {

        mArcPaint = new Paint();
        mArcPaint.setStrokeWidth(arcWidth);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(ActivityCompat.getColor(getContext(), R.color.white));
        mArcPaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.BLACK);
        mCirclePaint.setAlpha(60);
        mCirclePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(ActivityCompat.getColor(getContext(), R.color.white));// 设置灰色
        mTextPaint.setStyle(Paint.Style.FILL);//设置填满


        mPixelsPerMilliSecond = 360f / maxRecordDuration;
    }

    private String mShowText = "长按录制";
    private RectF mRectF;
    private double mPixelsPerMilliSecond;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mCircleXY, mCircleXY, mRadius, mCirclePaint);

        if (mLastUpdateTime != 0) {

            double time = System.currentTimeMillis();
            duanArc += (float) (mPixelsPerMilliSecond * (time - mLastUpdateTime));
            mLastUpdateTime = time;
            if (duanArc > 360) {
                duanArc = 360;
            }
        }

        mRectF = new RectF(arcWidth / 2, arcWidth / 2, length - arcWidth / 2, length - arcWidth / 2);
        canvas.drawArc(mRectF, 270, duanArc, false, mArcPaint);

        // 绘制文字
        float textWidth = length/4;   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        canvas.drawRect((length / 2 - textWidth / 2),(length / 2 - textWidth / 2),(length / 2 + textWidth / 2),(length / 2 +textWidth / 2),mTextPaint);

        if (mLastUpdateTime < mEndTime) {
            invalidate();
        }
    }

    private double mLastUpdateTime = 0;
    private double mStartTime = 0, mEndTime = 0;
    private float duanArc = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ismv) {
                    if (onStatsListener != null) {
                        onStatsListener.stop();
                    }
                } else {
                    if (onStatsListener != null) {
                        onStatsListener.start();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }


    public void start() {
        ismv=true;
        mStartTime = System.currentTimeMillis();
        mLastUpdateTime = mStartTime;
        mEndTime = mStartTime + maxRecordDuration;
        invalidate();
    }

    public void stop() {
        ismv=false;
        mStartTime = 0;
        mLastUpdateTime = mStartTime;
        mEndTime = 0;
        duanArc = 0;
        invalidate();
    }


    private int maxRecordDuration = 10 * 1000;

    public ProgressView setMaxRecordDuration(int maxRecordDuration) {
        this.maxRecordDuration = maxRecordDuration;
        mPixelsPerMilliSecond = 360f / maxRecordDuration;
        return this;
    }

    public int getMaxRecordDuration() {
        return maxRecordDuration;
    }


    public void setOnStatsListener(ProgressView.onStatsListener onStatsListener) {
        this.onStatsListener = onStatsListener;
    }

    private onStatsListener onStatsListener;


    public interface onStatsListener {
        void start();

        void stop();
    }


}
