package com.netease.nim.uikit.api.wrapper;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.activity.ToolBarOptions;

/**
 *
 * @author zhw_luke
 * @date 2019/10/28 0028 上午 11:29
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc im聊天bar信息
 */
public class NimToolBarOptions extends ToolBarOptions {

    public NimToolBarOptions() {
      //  logoId = R.drawable.nim_actionbar_nest_dark_logo;
      //  navigateId = R.drawable.nim_actionbar_dark_back_icon;

        logoId = 0;
        // navigateId = R.drawable.nim_actionbar_dark_back_icon;
        navigateId = R.mipmap.icon_back;
        isNeedNavigate = true;
    }
}
