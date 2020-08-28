package com.tinytiger.titi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/*
 * @author Tamas
 * create at 2020/3/3 0003
 * Email: ljw_163mail@163.com
 * description:
 */
public class WordGroupView extends View   {

    private static final String TAG = WordGroupView.class.getSimpleName();

    //文字绘制画笔
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //辅助线绘制画笔
    private Paint mSublinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<DrawPoint> drawPointList = new ArrayList<>();
    private List<String> keyWords = new ArrayList<>();
    private int[] colors = {
            Color.parseColor("#5DDD6F"),
            Color.parseColor("#FF556E"),
            Color.parseColor("#D7AB00"),

    };

    //文字展示最小可用宽高
    private int mMinTextWidth;
    private int mMinTextHeight;

    //最小可展示文字大小
    private int mMinFontSize;

    private int[][] mPoints;
    private int mMaxXPointCount;
    private int mMaxYPointCount;
    //设置词条最大长度和最小长度
    private int mMaxLength = 0;
    private int mMinLength = 0;

    public WordGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mMinFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                context.getResources().getDisplayMetrics());

        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mMinFontSize);

        Rect textBound = new Rect();
        mTextPaint.getTextBounds("正", 0, 1, textBound);

        mMinTextWidth = mMinTextHeight = textBound.width();

    //    Log.e(TAG, mMinTextWidth + "-" + mMinTextHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mMaxXPointCount = w / mMinTextWidth;
        mMaxYPointCount = h / mMinTextHeight;

        mPoints = new int[mMaxXPointCount][mMaxYPointCount];
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                mPoints[i][j] = 0;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(keyWords==null || keyWords.size()==0){
            return;
        }

        canvas.drawColor(Color.parseColor("#F2F2F2"));

//        /**
//         * 绘制辅助线
//         */
//        for (int i = 0; i <= mMaxXPointCount; i++) {
//            canvas.drawLine(i * mMinTextWidth, 0, i * mMinTextWidth, mMinTextHeight * mMaxYPointCount, mSublinePaint);
//        }
//
//        for (int i = 0; i <= mMaxXPointCount; i++) {
//            canvas.drawLine(0, i * mMinTextHeight, mMinTextWidth * mMaxXPointCount, i * mMinTextHeight, mSublinePaint);
//        }

        //widget=4表示单个文字占用空间为4x4,其他类似
        for (int i = 4; i >= 1; i--) {
            buildPoints(mPoints, getFullContent(i), i, 0);
        }

        /**
         * 绘制真实的效果
         */
        for (DrawPoint point : drawPointList) {
            point.draw(canvas);
        }
    }

    private String[] getFullContent(int widget) {

        int maxCount = mMaxXPointCount * mMaxYPointCount * widget * widget;
        int currMaxItemCount = (int) (Math.random() * maxCount);
        String[] maxItems = new String[currMaxItemCount];
        for (int i = 0; i < maxItems.length; i++) {
            maxItems[i] = keyWords.get((int) (Math.random() * keyWords.size()));
        }
        if (maxItems.length == 0) {
            maxItems = new String[]{
                    keyWords.get(0)
            };
        }
        return maxItems;
    }

    /**
     * 不断检索并构建指定widget大小的数据，直到不能容纳或者绘制完成
     */
    private void buildPoints(int[][] points, String[] maxItems, int widget, int index) {

        if ((index > maxItems.length - 1) || capacityOut(points, widget, maxItems[index])) {
            return;
        }

        final int randomPointX = (int) (Math.random() * mMaxXPointCount);
        final int randomPointY = (int) (Math.random() * mMaxYPointCount);

        boolean isBuildSuc = false;
        if (isCanFull(points, randomPointX, randomPointY)
                && isCanDraw(points, randomPointX, randomPointY, maxItems[index], widget)) {
            for (int i = randomPointX; i < randomPointX + maxItems[index].length() * widget; i++) {
                for (int j = randomPointY; j < randomPointY + widget; j++) {
                    points[i][j] = 1;
                }
            }
            drawPointList.add(new DrawPoint(randomPointX, randomPointY, maxItems[index], widget));
            isBuildSuc = true;
        } else {
            buildPoints(points, maxItems, widget, index);
        }

        if (isBuildSuc) {
            buildPoints(points, maxItems, widget, index + 1);
        }
    }

    /**
     * 超出可容纳范围
     */
    private boolean capacityOut(int[][] points, int widget, String maxItem) {
        boolean isCapacityOut = true;
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (isCanDraw(points, i, j, maxItem, widget)) {
                    isCapacityOut = false;
                    break;
                }
            }
        }
        return isCapacityOut;
    }

    /**
     * 判断是否可以绘制
     */
    private boolean isCanDraw(int[][] points, int randomPointX, int randomPointY, String maxItem, int widget) {
        boolean isCanFull = false;
        if (isOutHorizontal(randomPointX + maxItem.length() * widget)
                && isOutVertical(randomPointY + widget)) {
            isCanFull = true;
            for (int i = randomPointX; i < randomPointX + maxItem.length() * widget; i++) {
                for (int j = randomPointY; j < randomPointY + widget; j++) {
                    if (!isCanFull(points, i, j)) {
                        isCanFull = false;
                        break;
                    }
                }
            }
        }
        return isCanFull;
    }

    /**
     * 判断水平方向是否越界
     */
    private boolean isOutHorizontal(int pointX) {
        return pointX <= mMaxXPointCount;
    }

    /**
     * 判断竖直方向是否越界
     */
    private boolean isOutVertical(int pointY) {
        return pointY <= mMaxYPointCount;
    }

    /**
     * 是否可以填充
     */
    private boolean isCanFull(int[][] point, int pointX, int pointY) {
        if (pointX < mMaxXPointCount && pointY < mMaxYPointCount) {
            return point[pointX][pointY] == 0;
        }
        return false;
    }

    public void random() {
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                mPoints[i][j] = 0;
            }
        }
        drawPointList.clear();
        invalidate();
    }

    public class DrawPoint {
        private int pointX;
        private int pointY;
        public String label;
        public int widget;

        DrawPoint(int pointX, int pointY, String label, int widget) {
            this.pointX = pointX;
            this.pointY = pointY;
            this.label = label;
            this.widget = widget;
        }

        public void draw(Canvas canvas) {
            mTextPaint.setTextSize(mMinFontSize * widget - 8 * widget);
            mTextPaint.setColor(colors[(int) (Math.random() * colors.length)]);
       //     Log.e(TAG, widget + "-" + label + "-" + pointX + "-" + pointY);
            canvas.drawText(label, pointX * mMinTextWidth + 4 * widget, (pointY + widget) * mMinTextHeight - 4 * widget, mTextPaint);
            mTextPaint.setStyle(Paint.Style.FILL);
        }
    }

    public void setWords(List<String> words) {
        this.keyWords = words;
        for (int i = 0; i < keyWords.size(); i++) {
            if (i == 0) {
                mMaxLength = keyWords.get(i).length();
                mMinLength = keyWords.get(i).length();
                continue;
            }
            if (keyWords.get(i).length() > mMaxLength) {
                mMaxLength = keyWords.get(i).length();
            }
            if (keyWords.get(i).length() < mMinLength) {
                mMinLength = keyWords.get(i).length();
            }
        }
        drawPointList.clear();
        invalidate();
    }

}
