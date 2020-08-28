package com.tinytiger.titi.mvp.contract.center

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface MineWorkContract {
    interface View : IBaseView {

        fun showGameAmwayList(bean: UserGameAmwayList.Data)

        fun delUserGameAmway()

        fun showDynamicNodeList(bean: PostInfoBean)

        fun showAnswersNodeList(bean: PostInfoBean)

    }

    interface Presenter : IPresenter<View> {


        fun getUserGameAmwayList(page: Int)

        fun delUserGameAmwayList(ids: String)
    }
}