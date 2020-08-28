package com.tinytiger.titi.mvp.contract.center

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.UserMedalBean
import com.tinytiger.common.net.data.center.UserMedalList


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface MineMeritContract {
    interface View : IBaseView {
        fun showMedalList(bean: UserMedalList.DataBean)

        fun showWearMedal(position:Int)

    }

    interface Presenter : IPresenter<View> {


        /**
         *用户意见收发室分类
         */
        fun getMedalList(page:Int)



        /**
         *   佩戴/取消佩戴 勋章
         */
        fun wearMedal(medal_id:String,position:Int)


    }
}