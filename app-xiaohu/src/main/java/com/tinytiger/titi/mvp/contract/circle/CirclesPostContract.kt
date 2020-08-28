package com.tinytiger.titi.mvp.contract.circle

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.circle.postsend.*


interface CirclesPostContract {
    interface View : IBaseView {
        fun showCircleByCate(bean: SelectCircler2Bean)
        fun showCircleByCate(bean: SelectCircler1Bean)


        fun showCircleByModular(bean: SelectCirclerNameBean)
        fun showCircleByModular(bean: SelectCirclerName2Bean)

        fun showUserList(bean: SelectFriendBean)
        fun showFollowUserList(bean: SelectFriend2Bean)

    }

    interface Presenter : IPresenter<View> {

        fun indexCircleByCate(page: Int)
    }
}