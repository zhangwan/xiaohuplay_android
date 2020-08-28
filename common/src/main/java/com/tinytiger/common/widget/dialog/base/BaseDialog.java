package com.tinytiger.common.widget.dialog.base;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tinytiger.common.R;


/**
 * Created by Tamas Lee on 2018/5/8.
 * company Vineyards
 * ljw_163mail@163.com
 * desc:
 */

public abstract class BaseDialog extends DialogFragment {

    private static final String TAG = "base_dialog";

    private static final float DEFAULT_DIM = 0.6f;

    protected  boolean mIsCancelOutside = true;
    protected  boolean mIsCancelable = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
    }

    @LayoutRes
    public abstract int getLayoutRes();


    public abstract void bindView(View v);

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();



        if (getHeight() > 0) {
            params.height = getHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

    public int getWidtch() {
        return 1;
    }

    public int getHeight() {
        return -1;
    }

    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public boolean getCancelOutside() {
        return mIsCancelOutside;
    }


    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        try{
            show(fragmentManager, getFragmentTag());
        }catch (IllegalStateException ignore){
        }
    }

}
