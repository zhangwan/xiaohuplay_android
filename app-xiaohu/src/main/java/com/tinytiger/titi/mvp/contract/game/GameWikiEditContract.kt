package com.tinytiger.titi.mvp.contract.game

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.GameLibListBean
import com.tinytiger.common.net.data.gametools.GameWikiEditBean
import com.tinytiger.common.net.data.gametools.GameWikiListBean


/**
 * @author lmq001
 * @date 2020/06/01 11:10
 * @copyright 小虎互联科技
 * @doc 查看词条编辑者
 */
interface GameWikiEditContract {
    interface View : IBaseView {
        fun showGameWikiList(data: GameWikiEditBean)
    }

    interface Presenter : IPresenter<View> {
        fun getGameWikiList(content_id: String, page: Int)
    }
}