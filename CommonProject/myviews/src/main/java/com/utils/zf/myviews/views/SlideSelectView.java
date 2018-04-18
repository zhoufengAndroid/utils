package com.utils.zf.myviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.utils.zf.myviews.R;

/**
 * Created by ZF on 2018/4/10.
 */

public class SlideSelectView extends View {

    private Paint mPaint;
    private float radius = 40;
    private float lineWidth = 40;
    private float downX;
    private int anInt;
    private float pointX;
    private int width;
    private int circleColor;
    private int outCircleColor;
    private int lineColor;
    private int lineBgColor;
    private float slideTextSize;
    private float slideSpacingExtra;
    private int slideTextColor;
    private String[] strings = new String[]{"0元", "5元", "8元", "12元", "16元", "20元"};
    private int selectPoint;
    private boolean isUp;
    private TextPaint textPaint;

    public SlideSelectView(Context context) {
        this(context, null);
    }

    public SlideSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tta = context.obtainStyledAttributes(attrs, R.styleable.SlideSelectView);
        //两种字体颜色和字体大小
        radius = tta.getDimension(R.styleable.SlideSelectView_circleRadius, 5);
        lineWidth = tta.getDimension(R.styleable.SlideSelectView_lineWidth, 5);
        circleColor = tta.getColor(R.styleable.SlideSelectView_circleColor, Color.BLACK);
        outCircleColor = tta.getColor(R.styleable.SlideSelectView_outCircleColor, Color.BLACK);
        lineColor = tta.getColor(R.styleable.SlideSelectView_lineColor, Color.BLACK);
        lineBgColor = tta.getColor(R.styleable.SlideSelectView_lineBgColor, Color.BLACK);
        slideTextColor = tta.getColor(R.styleable.SlideSelectView_slideTextColor, Color.BLACK);
        slideTextSize = tta.getDimension(R.styleable.SlideSelectView_slideTextSize, 50);
        slideSpacingExtra = tta.getDimension(R.styleable.SlideSelectView_slideSpacingExtra, 10);
        initPaint(context);
    }

    private void initPaint(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineWidth);

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(slideTextSize);
        textPaint.setColor(slideTextColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = (int) (MeasureSpec.getSize(widthMeasureSpec) - 2 * radius);
        anInt = width / (strings.length - 1);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            if (strings != null && strings.length != 0) {
                Rect rect = new Rect();
                textPaint.getTextBounds(strings[0], 0, strings[0].length(), rect);
                //从矩形区域中读出文本内容的宽高
                int centerTextHeight = rect.height();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (radius * 2 + centerTextHeight + slideSpacingExtra), heightMode);
            } else {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (radius * 2 + 1), heightMode);

            }

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(10, radius, width + radius * 2 - 11, radius, mPaint);
        mPaint.setColor(lineBgColor);
        canvas.drawLine(10, radius, pointX > (width + radius * 2 - 11) ? (width + radius * 2 - 11) : pointX, radius, mPaint);
        drawCircle(canvas);

        Rect rect = new Rect();
        for (int i = 0; i < strings.length; i++) {
            String text = strings[i];
            textPaint.getTextBounds(text, 0, text.length(), rect);
            //从矩形区域中读出文本内容的宽高
            int centerTextWidth = rect.width();
            int centerTextHeight = rect.height();

            if (i == 0) {
                canvas.drawText(text, 0, radius * 2 + slideSpacingExtra + centerTextHeight - 5, textPaint);//绘制被选中文字，注意点是y坐标,是以文字底部为中心
            } else if (i == strings.length - 1) {
                canvas.drawText(text, getWidth() - centerTextWidth,
                        radius * 2 + slideSpacingExtra + centerTextHeight - 5, textPaint);//绘制被选中文字，注意点是y坐标,是以文字底部为中心
            } else {
                canvas.drawText(text, i * anInt - centerTextWidth / 2 + radius,
                        radius * 2 + slideSpacingExtra + centerTextHeight - 5, textPaint);//绘制被选中文字，注意点是y坐标,是以文字底部为中心
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获得点下去的x坐标
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE://复杂的是移动时的判断
                isUp = false;
                pointX = event.getX();
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                isUp = true;
                float scrollX = event.getX();
                int pointNo = (int) (scrollX / anInt);//左边点为第几个点
                float length = scrollX % anInt;//距离左边点的距离
                if (length < (anInt / 2)) {
                    selectPoint = pointNo;
                    pointX = pointNo * anInt;
                } else {
                    selectPoint = pointNo + 1;
                    pointX = (pointNo + 1) * anInt;
                }
                //抬起手指时，重绘
                invalidate();

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.reset();
        mPaint.setAntiAlias(true);

        mPaint.setColor(outCircleColor);
        if (radius > pointX) {
            canvas.drawCircle(radius, radius, radius, mPaint);
        } else if (width > (pointX + radius)) {
            canvas.drawCircle(pointX + radius, radius, radius, mPaint);
        } else {
            canvas.drawCircle(width + radius, radius, radius, mPaint);
        }

        mPaint.setColor(circleColor);
        if (radius > pointX) {
            canvas.drawCircle(radius, radius, radius/2, mPaint);
        } else if (width > (pointX + radius)) {
            canvas.drawCircle(pointX + radius, radius, radius/2, mPaint);
        } else {
            canvas.drawCircle(width + radius, radius, radius/2, mPaint);
        }



        if (onSelectListener != null && isUp) {
            onSelectListener.onSelect(selectPoint);
        }
    }

    public String getData() {
        if (selectPoint >= strings.length) {
            return strings[strings.length - 1];
        }
        return strings[selectPoint];
    }

    private OnSelectListener onSelectListener;

    public OnSelectListener getOnSelectListener() {
        return onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelect(int position);
    }
}
