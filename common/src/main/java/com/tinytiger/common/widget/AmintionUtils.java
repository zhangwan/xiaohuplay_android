package com.tinytiger.common.widget;


import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;



/**
 * @author luke
 * @date 2018/8/30 20:19
 * @doc 加载框
 */
public class AmintionUtils {

    public Animation createScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotXValue, float pivotYValue) {
        Animation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        scaleAnimation.setDuration(200);
        return scaleAnimation;
    }

}
