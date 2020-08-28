package com.tinytiger.titi.mvp.contract.mine


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface ReportContract {

    interface View : IBaseView {

        fun getReportData()
        fun getQiniuToken(qiniuTocken: String)
    }

    interface Presenter : IPresenter<View> {

        fun reportArticle(
            content_id: String,
            report_reason: Int,
            supplement: String,
            images_url_1: String,
            images_url_2: String
        )

        fun reportComment(
            comment_id: String,
            report_reason: Int,
            supplement: String,
            images_url_1: String,
            images_url_2: String
        )

        fun loadQiniuToken()
    }


}