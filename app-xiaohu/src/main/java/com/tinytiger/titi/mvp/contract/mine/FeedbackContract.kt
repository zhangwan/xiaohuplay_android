package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.common.net.data.mine.FeedbackListBean
import com.tinytiger.common.net.data.mine.MyContentListBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface FeedbackContract {
    interface View : IBaseView {


        fun showProposalList(bean:FeedbackListBean.DataBean?)

        fun showResult()

        fun showCateList(bean:List<CatListBean.CateBean>)
    }

    interface Presenter : IPresenter<View> {

        /**
         * 用户意见收发室分类
         */
        fun getCatList()

        /**
         *  意见收发室列表
         */
        fun getProposalList(opinion_cat_id:Int,page: Int)




        /**
         *   用户发表意见
         */
        fun addProposal(opinion_cat_id:Int,content: String)
    }
}