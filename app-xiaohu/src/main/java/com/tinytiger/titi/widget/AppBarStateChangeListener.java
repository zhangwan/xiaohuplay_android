package com.tinytiger.titi.widget;

import com.google.android.material.appbar.AppBarLayout;

/*
 * @author Tamas
 * create at 2019/12/3 0003
 * Email: ljw_163mail@163.com
 * description:
 */
public  abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener  {

    private static final String TAG = "AppBarStateChangeListen";

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        onOffsetChanged(appBarLayout);
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED);
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE);
            }
            mCurrentState = State.IDLE;
        }
    }

    //状态发生了改变
    public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

    //发生了偏移
    public abstract void onOffsetChanged(AppBarLayout appBarLayout);

}
