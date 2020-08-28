package com.tinytiger.titi.mvp.contract.mine


import com.tinytiger.common.net.data.mine.UserPrivacyBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter




interface  SettingPrivacyContract{
    interface View : IBaseView {

        fun showPrivacyData()
        fun getUserPrivateConfig(bean: UserPrivacyBean)
    }

    interface Presenter : IPresenter<View> {

        fun modifyPrivateConfig(private_type: Int, modify_val: Int)
        fun loadUserPrivateConfig()
    }
}