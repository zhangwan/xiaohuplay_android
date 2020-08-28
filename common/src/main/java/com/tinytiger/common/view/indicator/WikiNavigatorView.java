package com.tinytiger.common.view.indicator;
import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;
import com.tinytiger.common.net.data.gametools.wiki.WikiCategoryBean;
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.ArrayList;

/**
 *
 * @author zhw_luke
 * @date 2020/4/9 0009 上午 10:35
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 无角标指示器
 */
public class WikiNavigatorView extends FrameLayout {

    public WikiNavigatorView(Context context) {
        this(context,null);
    }

    public WikiNavigatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WikiNavigatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private FragmentContainerHelper mFragmentContainerHelper =new FragmentContainerHelper();
    private ArrayList<WikiCategoryBean> titles  =new ArrayList<>();
    private CommonNavigatorAdapter mAdapter;
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_navigator, this);
        MagicIndicator magic_indicator= view.findViewById(R.id.magic_indicator);

        CommonNavigator commonNavigator =new CommonNavigator(context);
        commonNavigator.setAdjustMode(false);
         mAdapter=  new CommonNavigatorAdapter(){
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView titleView =new  ScaleTransitionPagerTitleView(context);
                titleView.setText(titles.get(index).cate_name);
                titleView.setTextSize(16f);
                titleView.setNormalColor(ContextCompat.getColor(context, R.color.grayAA));
                titleView.setSelectedColor(ContextCompat.getColor(context, R.color.gray33));

                titleView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFragmentContainerHelper.handlePageSelected(index);
                        if (mListener!=null){
                            mListener.onNavigator(index);
                        }
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        };

        commonNavigator.setAdapter(mAdapter);
        magic_indicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magic_indicator);
    }


    public void setTitles(ArrayList<WikiCategoryBean> title , int index){
        titles.clear();
        titles.addAll(title);
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
            mFragmentContainerHelper.handlePageSelected(index);
            mListener.onNavigator(index);
        }
    }



    public OnNavigatorListener mListener = null;

    public void setOnNavigatorListener(OnNavigatorListener mListener) {
        this.mListener = mListener;
    }

    public interface OnNavigatorListener {

        void onNavigator(int index);
    }
}
