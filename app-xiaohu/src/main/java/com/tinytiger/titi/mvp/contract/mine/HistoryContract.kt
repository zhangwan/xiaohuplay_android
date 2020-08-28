package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.mine.MyContentListBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface  HistoryContract{
    interface View : IBaseView {


        fun getUserFollow(uid:String, is_mutual:Int)

    fun showViewHistory(bean: MyContentListBean.DataBean)
    fun showAmwayHistory(bean: UserGameAmwayList.Data)

        fun showResult()
    }

    interface Presenter : IPresenter<View> {


        /**
         *  获取浏览历史记录
         */
        fun  getViewHistory(page:Int)

        fun  getUserGameAmwayViewHistory(page:Int)

        /**
         *  清除所有浏览记录
         */
        fun clearViewHistory(type:Int)


        /**
         *   根据浏览记录id删除浏览记录
         */
        fun delViewHistory(id:Int)
    }
}