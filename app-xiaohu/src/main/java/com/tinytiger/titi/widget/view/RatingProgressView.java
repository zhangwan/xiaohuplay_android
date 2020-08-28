package com.tinytiger.titi.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.tinytiger.titi.R;


public class RatingProgressView extends LinearLayout {


    private ProgressBar mProgressBar1,mProgressBar2,mProgressBar3,mProgressBar4,mProgressBar5;

    public RatingProgressView(@NonNull Context context) {
        this(context, null);
    }

    public RatingProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init(context);
    }



    private void initAttr(Context context, AttributeSet attrs) {
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_rating_progress, this);


        mProgressBar1 = view.findViewById(R.id.progress_1);
        mProgressBar2 = view.findViewById(R.id.progress_2);
        mProgressBar3 = view.findViewById(R.id.progress_3);
        mProgressBar4 = view.findViewById(R.id.progress_4);
        mProgressBar5 = view.findViewById(R.id.progress_5);




    }

    public void setProgress(int progress1,int progress2,int progress3,int progress4,int progress5,int max){
        if(max==0)
            max = 1;

        mProgressBar1.setProgress(progress1*100/max);
        mProgressBar2.setProgress(progress2*100/max);

        mProgressBar3.setProgress(progress3*100/max);
        mProgressBar4.setProgress(progress4*100/max);
        mProgressBar5.setProgress(progress5*100/max);
    }

}
