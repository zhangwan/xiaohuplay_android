package com.tinytiger.titi.mvp.contract


import android.widget.ImageView
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.InitBean
import com.tinytiger.common.net.data.yungaem.InitVersionBean


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface MainContract {

    interface View : IBaseView {

        /**
         *
         */
        fun getConfig(itemInfo: InitVersionBean)

    }

    interface Presenter : IPresenter<View> {

        /**
         *
         */
        fun startApp()

    }


}