package com.tinytiger.common.view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tinytiger.common.R;

/**
 * @author zhw_luke
 * @date 2018/10/10 0010 下午 1:54
 * @doc 全局RecyclerView 背景添加
 */
public class RefreshView {


    public static View getNewFooterView(Activity AC, String str, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.load_end_layout_new, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView bottomTxt = view.findViewById(R.id.bottom_txt);
            bottomTxt.setText(str);
        }
        return view;
    }

    public static View getNewFooterView(Activity AC, String str, int height, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.load_end_layout_new, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView bottomTxt = view.findViewById(R.id.bottom_txt);
            bottomTxt.setText(str);
        }
        if (height != 0) {

            LinearLayout ll_content = view.findViewById(R.id.ll_content);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            params.topMargin = height;
            ll_content.setLayoutParams(params);
        }
        return view;
    }


    public static View getFooterView(Activity AC, String str, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.load_end_layout, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView bottomTxt = view.findViewById(R.id.bottom_txt);
            bottomTxt.setText(str);
        }
        return view;
    }

    /**
     * 加载失败背景
     *
     * @param AC
     * @param str
     * @param mRecyclerView
     * @return
     */
    public static View getErrorView(Activity AC, String str, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.view_error, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView rv_error_tv = view.findViewById(R.id.error_view_tv);
            rv_error_tv.setText(str);
        }
        return view;
    }

    /**
     * 空白页数据
     *
     * @param AC
     * @param str
     * @param mRecyclerView
     * @return
     */
    public static View getEmptyView(Activity AC, String str, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView rv_error_tv = view.findViewById(R.id.empty_view_tv);
            rv_error_tv.setText(str);
        }
        return view;
    }

    /**
     * 空白页数据
     *
     * @param AC
     * @param str
     * @param mRecyclerView
     * @return
     */
    public static View getEmptyView(Activity AC, String str, int image, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView rv_error_tv = view.findViewById(R.id.empty_view_tv);
            rv_error_tv.setText(str);
        }

        ImageView ivEmpty = view.findViewById(R.id.ivEmpty);
        ivEmpty.setImageDrawable(ContextCompat.getDrawable(AC, image));


        return view;
    }


    /**
     * 空白页数据
     *
     * @param AC
     * @param str
     * @param mRecyclerView
     * @return
     */
    public static View getMyEmptyView(Activity AC, String str, View mRecyclerView) {
        View view = AC.getLayoutInflater().inflate(R.layout.view_empty_mine, (ViewGroup) mRecyclerView.getParent(), false);
        if (!TextUtils.isEmpty(str)) {
            TextView rv_error_tv = view.findViewById(R.id.empty_view_tv);
            rv_error_tv.setText(str);
        }
        return view;
    }


    /**
     * 无网络数据
     *
     * @param AC
     * @param mRecyclerView
     * @param lis
     * @return
     */
    public static View getNetworkView(Activity AC, View mRecyclerView, View.OnClickListener lis) {
        View view = AC.getLayoutInflater().inflate(R.layout.view_no_network, (ViewGroup) mRecyclerView.getParent(), false);
        view.findViewById(R.id.no_network_view_tv).setOnClickListener(lis);
        return view;
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * RecyclerView滑动到指定位置
     */
    public static void smoothMoveToPosition(RecyclerView mRecyclerView, int position) {
        if (mRecyclerView == null) {
            return;
        }
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
/*            mToPosition = position;
            mShouldScroll = true;*/
        }
    }


    /**
     * 设置view Margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
