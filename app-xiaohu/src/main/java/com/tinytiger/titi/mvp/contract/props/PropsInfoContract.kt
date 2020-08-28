package com.tinytiger.titi.mvp.contract.props


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.home.NewsListBean
import com.tinytiger.common.net.data.props.PropsExchangeBean
import com.tinytiger.common.net.data.props.PropsInfoListBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean


/**
 *
 * @author tamas
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface PropsInfoContract {

    interface View : IBaseView {



        fun showPropsInfo(bean: PropsInfoListBean.DataBean)

        fun showResult()

        fun showWearResult(msg:String)

        fun showExchange(bean:PropsExchangeBean.DataBean)


    }

    interface Presenter : IPresenter<View> {

        fun  getPropsInfo(tool_id :String)

        fun buyTool(toolId: String, num: Int)

        fun wearProps(tool_id: String,cate_id: String)

        fun exchangeTools(toolId: String, num: Int)


         fun getExchangeInfo(id: String)

    }


}