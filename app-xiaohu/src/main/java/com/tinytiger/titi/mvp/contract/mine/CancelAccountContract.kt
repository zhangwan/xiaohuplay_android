package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:  设置
*/
interface  CancelAccountContract{
    interface View : IBaseView {


        /**
         */
        fun showResult( bean: BaseBean)



    }

    interface Presenter : IPresenter<View> {


        fun cancellationAccount()


    }
}