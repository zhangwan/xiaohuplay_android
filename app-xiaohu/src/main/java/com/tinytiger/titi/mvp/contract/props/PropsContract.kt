package com.tinytiger.titi.mvp.contract.props


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.home.NewsListBean
import com.tinytiger.common.net.data.props.PropsInfoListBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface PropsContract {

    interface View : IBaseView {

        fun indexGoodsCate(bean: PropsTabListBean)

        fun indexGoods(bean: PropsListBean)


        fun wearProps(bean: BaseBean)
    }

    interface Presenter : IPresenter<View> {

        fun loadindexGoodsCate(need_group: Int)

    }


}