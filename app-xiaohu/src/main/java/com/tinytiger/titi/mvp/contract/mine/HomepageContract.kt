package com.tinytiger.titi.mvp.contract.mine

import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.mine.MyContentListBean


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description: 
*/
interface  HomepageContract{
    interface View : IBaseView {


        /**
         * 显示个人主页的信息
         */
        fun showHomepageInfo( bean:UserCenterBean.DataBean)


        fun showContentList( bean: MyContentListBean.DataBean)


        fun showResult()

//        fun showCancelBlack()

        fun getQiniuToken(qiniu: String)


        fun showMutual(isMutual: Int)

        fun showBlackStatus(is_black: Int)
    }

    interface Presenter : IPresenter<View> {


        /**
         *  获取个人主页的信息
         */
        fun getHomepageInfo(user_id:String)
        /**
         *  修改用户背景图
         */
        fun changeBackgroundImg(background_img:String)


        fun getHomepageWorks(user_id:String,page:Int)
        fun getHomepageLike(user_id: String, page: Int)

        fun loadQiniuToken()

        fun doFollow(is_mutual:Int,user_id:String)

        fun addBlack(is_black:Int,user_id: String)

//         fun loadCancelBlack(user_id: String)
    }
}