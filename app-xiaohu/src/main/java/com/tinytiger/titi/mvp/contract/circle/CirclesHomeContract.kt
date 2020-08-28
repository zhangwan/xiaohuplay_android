package com.tinytiger.titi.mvp.contract.circle

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.*
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionUserBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface CirclesHomeContract {
    interface View : IBaseView {

         fun showFocusList(bean: CircleAttentionBean)

        fun showUserList(bean: CircleAttentionUserBean)

        fun showCircleList(bean: CircleMeBean)
        fun showCircleRecommendedList(bean: CircleRecommendedBean)

    }

    interface Presenter : IPresenter<View> {
        fun focusList(page:Int)

    }
}