package com.tinytiger.titi.mvp.contract.props


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.home.NewsListBean
import com.tinytiger.common.net.data.props.PropsExchangeListBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean


/**
 *
 * @Author luke
 * @Date 2020-02-05 16:17
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具兑换
 *
 */
interface PropsExchangeContract {

    interface View : IBaseView {

        fun getSubmitExchange(bean: PropsExchangeListBean)

        fun getSubmit(bean: BaseBean)
    }

    interface Presenter : IPresenter<View> {

        fun submitExchangePreview(exchange_code_json: String)

    }


}