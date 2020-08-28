package com.tinytiger.titi.mvp.contract.game

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.circle.postsend.*


interface GameWikiErrorContract {
    interface View : IBaseView {
        fun getQiniuToken(qiniuTocken: String)

        fun commitSuccess()
    }

    interface Presenter : IPresenter<View> {
        fun loadQiniuToken()
    }
}