package com.tinytiger.titi.mvp.contract.circle

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.postsend.DraftDetailBean
import com.tinytiger.common.net.data.circle.postsend.DraftListBean


interface CirclesPost1Contract {
    interface View : IBaseView {

        // fun getQiniuToken(qiniuTocken: String)
        fun sendPost(type: Int, is_draft: Int, detail: PostBean?)

        fun getDrafts(bean: DraftListBean)

        fun getDraftDetail(bean: DraftDetailBean)

    }

    interface Presenter : IPresenter<View> {

        fun indexDrafts(page: Int)

    }
}