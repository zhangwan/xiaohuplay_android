package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.GameCommentList
import com.tinytiger.common.net.data.gametools.GameContentListBean
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface GameContentContract {
    interface View : IBaseView {

       // fun showGameFollow(is_follow: Int)

        fun getGameDetailContentData(bean:GameContentListBean.Data)

    }

    interface Presenter : IPresenter<View> {

        fun getGameDetailContentData(game_id: String,page:Int)
    }
}