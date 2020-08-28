package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.common.net.data.mine.FeedbackListBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.data.mine.talent.TalentBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface TalentContract {
    interface View : IBaseView {
        fun getQiniuToken(qiniuTocken: String)
        fun showResult(bean: TalentBean?)
      //  fun showResult()
    }

    interface Presenter : IPresenter<View> {

        fun masterApply()
    }
}