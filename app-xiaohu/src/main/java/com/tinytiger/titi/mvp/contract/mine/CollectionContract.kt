package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.mine.MyContentListBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface CollectionContract {
    interface View : IBaseView {


        fun showCollection(bean: MyContentListBean.DataBean)

        fun showResult()
    }

    interface Presenter : IPresenter<View> {


        /**
         *  获取内容收藏
         */
        fun collectList(page: Int)




        /**
         *  取消内容收藏
         */
        fun cancelCollect(content_id: Int)
    }
}