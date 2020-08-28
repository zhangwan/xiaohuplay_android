package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.mine.ShareAppBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface  MineContract{
    interface View : IBaseView {


        /**
         * 登录信息
         * @param login_status Boolean false 登录失效 true 登录成功
         * @param bean UserCenterBean 用户中心的数据
         */
        fun showUserCenter(login_status:Boolean, bean: UserCenterBean.DataBean?)


        fun getShareAppBean(bean: ShareAppBean)
    }

    interface Presenter : IPresenter<View> {


        /**
         *  获取个人中心基本数据
         */
        fun getUserCenter()



    }
}