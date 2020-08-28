package com.tinytiger.titi.mvp.contract.circle.post


import android.app.Activity
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.net.data.circle.post.PostDetailsBean
import com.tinytiger.common.net.data.circle.post.PostListBean
import com.tinytiger.common.net.data.search.*


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface PostContract {

    interface View : IBaseView {

        fun getPostDetailsBean(mBean: PostDetailsBean)
        fun getPostListBean(mBean: PostListBean)
        fun showMutual(is_mutual: Int)

        fun addCommentTxt(txt: String, comment_id: Int)
        fun delComment(comment_id: String)

        fun replyAdoption(item: PostData,participateNum:String)

        fun showCollect(is_collect: Int)
    }

    interface Presenter : IPresenter<View> {

        /**
         *所有搜索数据
         */
        fun getPostInfo(activity: Activity, keyWords:String)

    }


}