package com.example.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * @ProjectName: WidgetDemo
 * @Package: com.example.widgetdemo.widget
 * @ClassName: MyRegulator
 * @Description:
 * @Author: TaxiDriverSantos
 * @CreateDate: 2019/11/19   13:49
 * @UpdateUser: TaxiDriverSantos
 * @UpdateDate: 2019/11/19   13:49
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MyRegulator2 extends View {

    private Paint paint;
    private int width;
    private int height;
    private float horizontalLineWidth;
    private float sX;
    private float eY;

    private float strokeWidthV;
    private float strokeWidthH;
    private float eX;
    private float sY;

    private String content = "0";
    private boolean isNeedShowText = true;
    private float lastY;
    private float lastX;

    private float currentX;
    private float currentY;
    private float percentage;
    private int initialValue = 0;
    private int rangeFactor = 100;
    private String unitCharacter = "%";
    private boolean isFirstTime = true;

    public MyRegulator2(Context context) {
        this(context, null);
    }

    public MyRegulator2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRegulator2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        Loger.e("onLayout   width=" + width + "   height= " + height);

        strokeWidthV = width / 30f;
        strokeWidthH = width / 60f;

        Loger.e("  strokeWidthV=" + strokeWidthV + "  strokeWidthH =" + strokeWidthH);

        sX = (width / 2f);
        horizontalLineWidth = width / 15f;
        eX = sX - horizontalLineWidth;
        sY = height * 1.5f / 18;
        eY = height * 16.5f / 18;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawRuler(canvas);
        drawLightRuler(canvas);
        drawCircle(canvas);
        drawString(canvas);

    }


    private void drawRuler(Canvas canvas) {
        canvas.save();
        paint.setColor(Color.parseColor("#797979"));
        paint.setStrokeWidth(strokeWidthV);
        canvas.drawLine(sX, sY, sX, eY, paint);
        canvas.restore();
        Loger.e(" drawRuler  ");
    }

    private void drawLightRuler(Canvas canvas) {
        canvas.save();
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStrokeWidth(strokeWidthV);

        Loger.d("drawLightRuler  percentage=" + percentage);
        canvas.drawLine(sX, (eY - Math.round(percentage * (Math.abs(eY - sY)))), sX, eY, paint);
        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
//        Loger.e(" drawCircle  radius=" + horizontalLineWidth + "   paint= " + paint);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(sX, (eY - Math.round(percentage * (Math.abs(eY - sY)))), horizontalLineWidth / 1.8f, paint);
        canvas.restore();

        Loger.e(" drawCircle ");
    }

    private void ensureCurrentYValid() {
        if (currentY > eY) {
            currentY = eY;
        }
        if (currentY < sY) {
            currentY = sY;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isNeedShowText = true;
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                currentX = event.getX();
                currentY = event.getY();

                ensureCurrentYValid();
                percentage = (eY - currentY) / (eY - sY);
                Loger.d("  currentY= " + currentY + "    currentX=" + currentX + "     percentage=" + percentage);
                ensurePercentageValid();
                invalidate();


                Loger.w("  ACTION_MOVE= " + "    listener=" + listener);
                if (listener != null) {
                    ensurePercentageValid();
                    listener.onRegulatorPercentageChanged(percentage,this);
                }


                lastX = currentX;
                lastY = currentY;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                isNeedShowText = false;
//                invalidate();
//                Loger.w("  ACTION_UP= " + "    listener=" + listener);
//                if (listener != null) {
//                    ensurePercentageValid();
//                    listener.onRegulatorPercentageChanged(percentage,this);
//                }
                break;
        }
        return true;
    }

    private void ensurePercentageValid() {
        if (percentage < 0) {
            percentage = 0;
        }
        if (percentage > 1) {
            percentage = 1;
        }
    }

    private String getContentText(float percentageFactor) {

        if (percentageFactor > 1) {
            percentageFactor = 1;
        }

        if (percentageFactor < 0) {
            percentageFactor = 0;
        }

        return (initialValue + Math.round(percentageFactor * rangeFactor)) + unitCharacter;
    }

    private void drawString(Canvas canvas) {

        if (!isNeedShowText) {
            return;
        }

        canvas.save();
//        Loger.e(" drawCircle  radius=" + horizontalLineWidth + "   paint= " + paint);
        paint.setStrokeWidth(strokeWidthH / 100f);
        paint.setAntiAlias(true);
        paint.setColor(Color.CYAN);
        paint.setTextSize(strokeWidthV * 3);
        content = getContentText(percentage);
        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();

        float x = width * 3f / 4f - textWidth / 2f;

        float y = (eY - Math.round(percentage * (Math.abs(eY - sY)))) - height / 100f;
        canvas.drawText(content, x, y, paint);
        canvas.restore();

//        Loger.e(" drawString    content=" + content);
    }

    public void setInitialValue(int initialVal, int rangeFactor, String unitCharactor) {
        this.initialValue = initialVal;
        this.rangeFactor = rangeFactor;
        this.unitCharacter = unitCharactor;
        invalidate();//此处最好用定义属性的进行初始化
    }

    public void setPercentage(float p) {
        if (p > 1) {
            p = 1f;
        }
        this.percentage = p;
        Loger.d("setPercentage  p=" + p);

        if (listener != null && isFirstTime) {
            listener.onRegulatorPercentageChanged(percentage,this);
            isFirstTime = false;
        }

        invalidate();
    }


    private OnRegulatorChangedListener listener;

    public void setListener(OnRegulatorChangedListener listener) {
        this.listener = listener;
    }

    public interface OnRegulatorChangedListener {
        void onRegulatorPercentageChanged(float percentage,MyRegulator2 regulator);
    }
}
