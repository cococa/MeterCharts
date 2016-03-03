package com.c.mcharts;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.text.DecimalFormat;

/**
 * Created by cocoa on 2016/2/25.14:09
 * email:385811416@qq.com
 */
public class MeterChart extends View {


    public MeterChart(Context context) {
        super(context);
        init(context);
    }

    public MeterChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeterChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private static final float START_ANGLE = 60F;// 45F;//60F;
    private static final float END_ANGLE = 240F;//270F;//255F;
    private static final DecimalFormat format = new DecimalFormat(",###");
    private static final float CIRCLE_STROKE_WIDTH = 15F;// 外圈边框的宽度

    private static final float ANGLE90 = 90F; //90F;//83F;

    private double progress;
    private double temp;
    private Paint circlePaint; //外圆背景
    private Paint overCirclePaint; //外圆
    //    private Paint bgPaint; //view的背景
    private Paint smallGraduationPaint; //小刻度
    private Paint pointPaint;  //    指针
    private Paint topTextPaint;  //  上面的文字
    private Paint dimPaint;  //  下面的面的文字
    private Paint pointCirclePaint;  //  指针的圆圈
    private int pointStartWidth = 2;
    private int pointEndWidth = 8;
    private Path pointPath;

    private int bgColor = Color.parseColor("#191e2d");
    private int circleColor = Color.parseColor("#232b4d");
    private int pointColor = Color.parseColor("#ffd303");
    private int topTextColor = Color.parseColor("#cdcfd6");


    private final int[] circleColors = new int[]{Color.YELLOW, Color.RED};// 渐变色环颜色
    private int topTextInt = 0;


    public int getTopTextInt() {
        return topTextInt;
    }

    public void setTopTextInt(int topTextInt) {
        this.topTextInt = topTextInt;
    }

    public double getProgress() {
        return temp;
    }

    public void setProgress(double temp) {
        this.temp = temp;
    }


    private void init(Context context) {

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);


        overCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        overCirclePaint.setColor(circleColor);
        overCirclePaint.setStyle(Paint.Style.STROKE);
        overCirclePaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        overCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        pointCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointCirclePaint.setStrokeWidth(8f);

        smallGraduationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        smallGraduationPaint.setColor(Color.GRAY);
        smallGraduationPaint.setStyle(Paint.Style.STROKE);
        smallGraduationPaint.setStrokeWidth(5f);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setColor(pointColor);

        topTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topTextPaint.setColor(topTextColor);
        topTextPaint.setStrokeWidth(2f);
        topTextPaint.setTextSize(35f);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bgColor);


        RectF circleRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        canvas.save();

        Path circlePath = new Path();
        circlePath.addArc(circleRectF, START_ANGLE, END_ANGLE);
        canvas.rotate(ANGLE90, centerX, centerY);
        canvas.drawPath(circlePath, circlePaint);

        Shader s = new SweepGradient(centerX, centerY, circleColors, null);
        overCirclePaint.setShader(s);
        circlePath.reset();
        circlePath.addArc(circleRectF, START_ANGLE, (float) (END_ANGLE * progress));
        canvas.drawPath(circlePath, overCirclePaint);

        canvas.restore();


        //刻度
        canvas.save();
        int start = centerY - radius + 20;
        for (int i = 0; i < 180; i++) {
            int end = start + 20;
            if (i % 6 == 0) {
                end = start + 40;
                smallGraduationPaint.setStrokeWidth(6f);
            } else {
                smallGraduationPaint.setStrokeWidth(3f);
            }
            if ((i % 3 == 0 && i % 6 != 0) || (i > 59 && i < 120)) {
                smallGraduationPaint.setColor(Color.TRANSPARENT);
            } else {
                smallGraduationPaint.setColor(Color.GRAY);
            }

            canvas.rotate(2, centerX, centerY);
            canvas.drawLine(centerX, start, centerX, end, smallGraduationPaint);
        }

        //恢复坐标系
        canvas.restore();


        //指针
        canvas.save();
        canvas.rotate((float) (END_ANGLE * progress + START_ANGLE), centerX, centerY);
        canvas.drawPath(pointPath, pointPaint);
        canvas.restore();


        pointCirclePaint.setColor(pointColor);
        canvas.drawCircle(centerX, centerY, 25, pointCirclePaint);
        pointCirclePaint.setColor(bgColor);
        canvas.drawCircle(centerX, centerY, 15, pointCirclePaint);


        String drawText = format.format((int) (topTextInt * animValue)) + "";

        int startY = centerY + 120;

        topTextPaint.setColor(topTextColor);
        topTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(drawText, centerX, startY, topTextPaint);

        topTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("车商指数排名第10名", centerX, startY + 50, topTextPaint);

//        int yyyy = (int) (radius + radius * Math.sin(START_ANGLE * Math.PI / 180));


    }


    public void playAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "animProgress", 0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        if (temp > 0.89) {
            animatorSet.setInterpolator(new DecelerateInterpolator());
        } else {
            animatorSet.setInterpolator(new OvershootInterpolator());
        }
        animatorSet.setTarget(this);
        animatorSet.play(animator);
        animatorSet.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateSize();
    }


    int width;
    int height;
    int centerX;
    int centerY;
    int radius;
    int pointLegth;

    private void calculateSize() {
        width = getWidth();
        height = getHeight();
            getPaddingTop();

        Log.e("--------------","  getPaddingTop=="+getPaddingTop()+"   getPaddingLeft=="+getPaddingLeft());


        int requestRadius = Math.min(width, height);

        int size = (int) (requestRadius + requestRadius * Math.sin(START_ANGLE * Math.PI / 180));
        while (size > height - 30 || requestRadius * 2 > width - 30) {
            requestRadius -= 10;
            size = (int) (requestRadius + requestRadius * Math.sin(START_ANGLE * Math.PI / 180));
        }

        centerX = width / 2;
        centerY = (int) (height - requestRadius * Math.sin(START_ANGLE * Math.PI / 180)) - 15;
        radius = requestRadius;

        pointLegth = radius * 2 / 5;

        pointPath = new Path();
        pointPath.moveTo(centerX - pointEndWidth, centerY);//
        pointPath.lineTo(centerX + pointEndWidth, centerY);//
        pointPath.lineTo(centerX + pointStartWidth, centerY + pointLegth);     //
        pointPath.lineTo(centerX - pointStartWidth, centerY + pointLegth);     //
        pointPath.close();
    }

    private float animValue;
    void setAnimProgress(float value) {
        this.animValue = value;
        progress = temp * value;
        invalidate();
    }


}


//        Path dimPath = new Path();
//        dimPath.moveTo(width / 2, height / 2);
//        dimPath.lineTo(width, height - 400);
//        dimPath.lineTo(width, height - 200);
//        dimPath.lineTo(width / 2, height / 2);
//        dimPath.close();
//        canvas.drawPath(dimPath, dimPaint);
//
//        Path dimPath1 = new Path();
//        dimPath1.moveTo(width / 2, height / 2);
//        dimPath1.lineTo(width, height - 300);
//        dimPath1.lineTo(width, height - 200);
//        dimPath1.lineTo(width / 2, height / 2);
//        dimPath1.close();
//        dimPaint.setColor(Color.parseColor("#301b1f2f"));
//        canvas.drawPath(dimPath, dimPaint);


//刻度的渐变，第2份设计图修改，暂存
//            int max = 255;
//            if (i > 50 && i < 130) {
//                if (i > 40 && i < 80) {
////                    smallGraduationPaint.setAlpha((70 - i) * 20);
//                    smallGraduationPaint.setAlpha((80 - i) * 6);
//                } else if (i > 100 && i < 140) {
////                    smallGraduationPaint.setAlpha((i - 90) * 20);
//                    smallGraduationPaint.setAlpha((i - 100) * 6);
//                } else {
//                    smallGraduationPaint.setAlpha(0);
//                }
//            } else {
//                smallGraduationPaint.setAlpha(254);
//            }

//        canvas.drawRect(startX - 50, startY - h - 50, startX + w + 50, startY + 50, circlePaint);
//        Rect r = new Rect();
//        r.top = startY - h - 50;
//        r.left = startX - 50;
//        r.right = startX + w + 50;
//        r.bottom = startY + 50;
//        canvas.drawBitmap(bmp, r, r, circlePaint);
//        canvas.drawRoundRect(startX - 50, startY - h - 50, startX + w + 50, startY + 50, 10,10,circlePaint);
//        canvas.drawRoundRect(new RectF(startX - 50, startY - h - 25, startX + w + 50, startY + 25), 10, 10, buttonPaint);


//        int startX = halfWidth + 100 / 2 + 50;
//        int startY = halfHeight + 50;
//        topTextPaint.setColor(Color.TRANSPARENT);
//        canvas.drawText(drawText, startX, startY, topTextPaint);


//        Rect rect = new Rect();
//        topTextPaint.getTextBounds(drawText, 0, drawText.length(), rect);
//        int w = rect.width();
//        int h = rect.height();


//缺口
//        Path path = new Path();
//        path.moveTo(width / 2, height / 2);
//        path.lineTo(0, height - 200);
//        path.lineTo(width, height - 200);
//        path.lineTo(width / 2, height / 2);
//        path.close();
//        canvas.drawPath(path, bgPaint);


//        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
//        p.setColor(Color.WHITE);
//        canvas.drawCircle(halfWidth, halfHeight, 10, p);


//        int xxxx = (int) (halfWidth - radius / Math.sqrt(2));
//        int yyyy = (int) (halfHeight + radius / Math.sqrt(2));


//        int xxxx = (int) (halfWidth - radius * Math.cos(45 * Math.PI / 180));
//          int yyyy = (int) (radius + radius * Math.sin(45 * Math.PI / 180));


//    drawColor(bgColor);
