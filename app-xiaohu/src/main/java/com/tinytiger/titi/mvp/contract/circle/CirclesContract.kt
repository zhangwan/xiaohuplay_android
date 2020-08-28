package com.tinytiger.titi.mvp.contract.circle

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.*
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface CirclesContract {
    interface View : IBaseView {

        fun showCircleList(bean: CircleMeBean)

        fun showCircleRecommendedList(bean: CircleRecommendedBean)

        fun showCircle(circle_id: String)
    }

    interface Presenter : IPresenter<View> {

        fun getGame(user_id:String,page:Int)

    }
}