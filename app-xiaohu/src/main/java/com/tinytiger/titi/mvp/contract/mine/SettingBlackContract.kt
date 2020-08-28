package com.tinytiger.titi.mvp.contract.mine


import com.tinytiger.common.net.data.mine.UserBlackBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter




interface  SettingBlackContract{
    interface View : IBaseView {

        fun showCancelBlack()
        fun getUserBlackList(bean: UserBlackBean)
    }

    interface Presenter : IPresenter<View> {

        fun loadUserBlackList(page: Int)
        fun loadCancelBlack(user_id:String)
    }
}