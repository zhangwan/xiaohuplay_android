package com.tinytiger.common.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;


/**
 *
 * @author zhw_luke
 * @date 2020/5/11 0011 下午 2:37
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 画廊效果
 */
public class TempRecyclerView extends RecyclerView {
    public TempRecyclerView(@NonNull Context context) {
        this(context,null);
    }

    public TempRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
       // return super.dispatchTouchEvent(ev);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
      //  return super.onTouchEvent(e);
    }*/


}