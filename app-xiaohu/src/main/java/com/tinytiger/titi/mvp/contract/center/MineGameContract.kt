package com.tinytiger.titi.mvp.contract.center

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.MineGameListBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface MineGameContract {
    interface View : IBaseView {

         fun showFollowGameList(bean: MineGameListBean.Data)

        fun showGameFollow(game_id: String)

    }

    interface Presenter : IPresenter<View> {


        fun getUserFollowGameList(page:Int)


      //  fun GameFollow(game_id: String, cancel:Int, position: Int)

    }
}