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
interface MineContentContract {
    interface View : IBaseView {


        fun showFocusUserWorks(bean: MyContentListBean.DataBean)

        fun showResult()

        fun getUserCollect(position:Int ,is_collect:Int)
    }

    interface Presenter : IPresenter<View> {


        /**
         *  获取关注用户作品列表
         */
        fun getFocusUserWorks(page: Int)




        /**
         *   取消内容点赞
         */
        fun cancelLike(content_id: Int)

        /**
         *   内容点赞
         */
        fun addLike(content_id: Int)
    }
}