package com.tinytiger.titi.mvp.contract.news


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.home.NewsListBean


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface NewsContract {

    interface View : IBaseView {

        fun getNewsInfo(mBean: NewsListBean)

        fun getNewsLike(type: Int)
        fun getNewsCollect(type: Int)

        /**
         * 评论发送成功
         */
        fun getNewsText()

        fun getUserFollow(is_mutual:Int)

    }

    interface Presenter : IPresenter<View> {

        fun loadContentInfo(content_id:String)

    }


}