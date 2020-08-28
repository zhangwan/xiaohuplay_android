package com.tinytiger.titi.mvp.contract.center


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.center.UserCollectWikiList

import com.tinytiger.common.net.data.center.UserGameAmwayList
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.net.data.mine.MyContentListBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface MineCollectContract {
    interface View : IBaseView {

        fun showAmwayCollectList(bean: UserGameAmwayList.Data)
        fun showNewsCollectList(bean: MyContentListBean.DataBean)
        fun showUserWikiCollectList(bean: UserCollectWikiList.Data)
        fun showPostCollectList(bean: PostInfoBean)

        fun getUserFollow(uid:String, is_mutual:Int)

        fun batchCancelCollect()
    }

    interface Presenter : IPresenter<View> {


        fun getUserGameAmwayCollectList(page: Int)

        fun collectList(page: Int)

        fun getUserWikiCollectList(page: Int)


    }
}