package com.tinytiger.titi.mvp.contract.news

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home.RecommendBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean


/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 下午 2:51
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc
 */
interface NewsInfoContract {
    interface View : IBaseView {

        fun showContentInfo(bean: ContentInfoBean.DataBean)

        fun showComment(bean: CommentListBean.DataBean)

        fun showIntroContentList(bean: ArrayList<RecommendBean>)

        fun showResult()

        fun showLike(is_like:Int)
        fun showCollect(is_collect:Int)
    }

    interface Presenter : IPresenter<View> {


        fun getContentInfo(content_id: String)
        fun addLike(content_id: String)
        fun cancelLike(content_id: String)

        fun addCollect(content_id: String)
        fun cancelCollect(content_id: String)

    }
}