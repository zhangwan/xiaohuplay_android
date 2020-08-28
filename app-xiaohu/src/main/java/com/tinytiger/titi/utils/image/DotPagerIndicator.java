package com.tinytiger.titi.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.tinytiger.titi.R;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData;

import java.util.List;

/**
 * 非手指跟随的小圆点指示器
 * Created by hackware on 2016/7/13.
 */
public class DotPagerIndicator extends View implements IPagerIndicator {
    private List<PositionData> mDataList;

   private Bitmap game_line;
    private float mCircleCenterX;

    private int dp6=0;
    public DotPagerIndicator(Context context) {
        super(context);
        dp6=UIUtil.dip2px(context, 6);
        Drawable game_lines=ContextCompat.getDrawable(context, R.mipmap.icon_game_line);
        game_line=drawableToBitmap(game_lines);
    }

    @Override
    public void onPageSelected(int position) {
        if (mDataList == null || mDataList.isEmpty()) {
            return;
        }
        PositionData data = mDataList.get(position);
        mCircleCenterX = data.mLeft + data.width() / 2;
        invalidate();
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mDataList = dataList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(game_line,leftX,getHeight()-dp6,null);
    }
    float leftX;
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        // 计算锚点位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mDataList, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mDataList, position + 1);

         leftX = current.mLeft + (current.mRight - current.mLeft) / 2;
     //   float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

     //   mAnchorX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static  Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
