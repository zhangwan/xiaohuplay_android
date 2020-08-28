package com.tinytiger.titi.widget.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;


/**
 * @author zhw_luke
 * @date 2018/10/26 0026 上午 10:12
 * @doc 可设置滑动
 */
public class TouchFrameLayout extends FrameLayout {
    /**
     * 是否可以进行滑动
     * FALSE 可滑动 true 禁止滑动
     */
    private boolean isSlide = true;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    public TouchFrameLayout(Context context) {
        super(context);
    }

    public TouchFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        noTouch(ev);
        return super.dispatchTouchEvent(ev);
    }

    private float mDownY = 0;
    private float mDownX = 0;

  //  private float mMoveY = 0;
  //  private float mMoveX = 0;
    private void noTouch(MotionEvent ev) {
        if (!isSlide) {
            return;
        }

        switch (ev.getAction()) {
           case MotionEvent.ACTION_DOWN:
               mDownY = ev.getY();
               mDownX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                // 过滤左右滑动
               /* if (Math.abs(ev.getX() - mMoveX) < 15 && Math.abs(ev.getY() - mMoveY) > 20) {
                    if (ev.getY() < mMoveY) {
                        //上滑
                        mListener.onTouchMove(true);
                    } else {
                        //下滑
                        mListener.onTouchMove(false);
                    }
                }
                mMoveY = ev.getY();
                mMoveX = ev.getX();*/
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(ev.getX() - mDownX) < 15 && Math.abs(ev.getY() - mDownY) > 20) {
                    /*if (ev.getY() > mDownY) {
                        mListener.onTouchMove(false);
                    }*/
                    if (ev.getY() < mDownY) {
                        //上滑
                        mListener.onTouchMove(true);
                    } else {
                        //下滑
                        mListener.onTouchMove(false);
                    }
                }
                break;
            default:
                break;
        }
    }

    public OnTouchMoveListener mListener = null;


    public void setOnTouchMoveListener(OnTouchMoveListener mListener) {
        this.mListener = mListener;
    }

    public interface OnTouchMoveListener {
        /**
         * 触摸事件
         *
         * @param type true移动 false 移动结束
         */
        void onTouchMove(boolean type);
    }


}
