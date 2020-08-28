package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.GameLibListBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface GameLibContract {
    interface View : IBaseView {
        fun showGameList(data: GameLibListBean)
    }

    interface Presenter : IPresenter<View> {
        fun getGameList(type: Int)


    }
}