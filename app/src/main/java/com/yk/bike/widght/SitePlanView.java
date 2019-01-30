package com.yk.bike.widght;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yk.bike.R;
import com.yk.bike.utils.BitmapCache;

import java.text.DecimalFormat;

public class SitePlanView extends View {

    private static final String TAG = "SitePlanView";

    private Bitmap check;
    private Bitmap cancel;

    private Paint mPaint = new Paint();
    private int x = 0;
    private int y = 0;
    private int cx = 0;
    private int cy = 0;
    private int radius = 200;
    private float scalePerPixel = 0f;
    private Bitmap mBitmap;

    private boolean isMove = false;
    private boolean canMove = false;
    private boolean isMultiTouch = false;

    private OnSitePlanClickListener onSitePlanClickListener;

    private int buttonSize;
    private DecimalFormat decimalFormat;

    public SitePlanView(Context context) {
        super(context);
    }

    public SitePlanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SitePlanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SitePlanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mBitmap == null || changed) {
            cx = getWidth() / 2;
            cy = getHeight() / 2;

            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mBitmap.eraseColor(ContextCompat.getColor(getContext(), R.color.colorBlack_50));
            mPaint = new Paint();
            mPaint.setAntiAlias(true);//设置画笔为无锯齿
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            check = BitmapCache.getBitmapByDrawable(R.drawable.ic_check);
            cancel = BitmapCache.getBitmapByDrawable(R.drawable.ic_cancel);

            buttonSize = check.getWidth();
            decimalFormat = new DecimalFormat("0.0");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawCircle(cx, cy, radius, mPaint);
        int scale = (int) (radius * scalePerPixel);
        String scaleText = scale < 1000 ? +scale + "m" : decimalFormat.format((double) scale / 1000d) + "km";
        drawText(cx - radius, cy - radius, cx + radius, cy + radius, scaleText, canvas);
        if (!isMove) {
            float y = cy + radius + buttonSize >= getHeight() ?
                    cy - radius - buttonSize :
                    cy + radius;
            canvas.drawBitmap(check, cx + radius - buttonSize, y, null);
            canvas.drawBitmap(cancel, cx - radius, y, null);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                x = (int) event.getX();
                y = (int) event.getY();

                canMove = Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy)) <= radius;
                performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                if (canMove) {
                    if (event.getPointerCount() >= 2) {
                        radius = (int) Math.sqrt((event.getX(0) - event.getX(1)) * (event.getX(0) - event.getX(1)) + (event.getY(0) - event.getY(1)) * (event.getY(0) - event.getY(1))) / 2;
                        if (radius >= getWidth() / 2 - buttonSize)
                            radius = getWidth() / 2 - buttonSize;
                        isMultiTouch = true;
                    }

                    if (isMultiTouch)
                        break;

                    isMove = true;

                    float moveX = event.getX() - x;
                    float moveY = event.getY() - y;

                    cx += moveX;
                    cy += moveY;

                    if (cx + radius >= getWidth())
                        cx = getWidth() - radius;
                    if (cx - radius <= 0)
                        cx = radius;
                    if (cy + radius >= getHeight())
                        cy = getHeight() - radius;
                    if (cy - radius <= 0)
                        cy = radius;

                    x = (int) event.getX();
                    y = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                isMultiTouch = false;
                /*为按钮添加点击事件
                 * 先左右分确定还是取消
                 * 再上下分
                 * */
                if (onSitePlanClickListener != null && x >= cx + radius - buttonSize && x <= cx + radius) {
                    //确定
                    boolean b = cy + radius + buttonSize >= getHeight();
                    if (b && y <= cy - radius && y >= cy - radius - buttonSize) {
                        //按钮在上
                        onSitePlanClickListener.onCheckClick();
                    } else if (!b && y >= cy + radius && y <= cy + radius + buttonSize) {
                        //按钮在下
                        onSitePlanClickListener.onCheckClick();
                    }

                } else if (onSitePlanClickListener != null && x >= cx - radius && x <= cx - radius + buttonSize) {
                    //取消
                    boolean b = cy + radius + buttonSize >= getHeight();
                    if (b && y <= cy - radius && y >= cy - radius - buttonSize) {
                        //按钮在上
                        onSitePlanClickListener.onCancelClick();
                    } else if (!b && y >= cy + radius && y <= cy + radius + buttonSize) {
                        //按钮在下
                        onSitePlanClickListener.onCancelClick();
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

    public void reset() {
        cx = getWidth() / 2;
        cx = getWidth() / 2;
        radius = 200;
        scalePerPixel = 0f;
        invalidate();
    }

    /**
     * 画文字
     */
    private void drawText(int startX, int startY, int endX, int endY, String content, Canvas canvas) {
        Rect rect = new Rect(startX, startY, endX, endY);
        Paint textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        textPaint.setTextSize((float) Math.sqrt(((startX - endX) * (startX - endX) + (startY - endY) * (startY - endY)) / 25f));
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        canvas.drawText(content, rect.centerX(), baseLineY, textPaint);
    }

    public int getCx() {
        return cx;
    }

    public SitePlanView setCx(int cx) {
        this.cx = cx;
        return this;
    }

    public int getCy() {
        return cy;
    }

    public SitePlanView setCy(int cy) {
        this.cy = cy;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public SitePlanView setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public float getScalePerPixel() {
        return scalePerPixel;
    }

    public SitePlanView setScalePerPixel(float scalePerPixel) {
        this.scalePerPixel = scalePerPixel;
        return this;
    }

    public OnSitePlanClickListener getOnSitePlanClickListener() {
        return onSitePlanClickListener;
    }

    public SitePlanView setOnSitePlanClickListener(OnSitePlanClickListener onSitePlanClickListener) {
        this.onSitePlanClickListener = onSitePlanClickListener;
        return this;
    }

    public interface OnSitePlanClickListener {
        void onCheckClick();

        void onCancelClick();
    }
}
