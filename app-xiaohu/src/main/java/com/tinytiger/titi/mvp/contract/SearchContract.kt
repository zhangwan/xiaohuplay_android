package com.tinytiger.titi.mvp.contract


import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.search.*


/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 2:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc
 */
interface SearchContract {

    interface View : IBaseView {

        fun getKeyword(mBean: SearchKeywordBean)

        fun getAllSearch(mBean: SearchAllBean)

        fun getGameSearch(mBean: SearchGameBean)

        fun getEssaySearch(mBean: SearchNewsBean)

        fun getUserSearch(mBean: SearchUserBean)

        fun getPostSearch(mBean: PostInfoBean)

        fun getCircleSearch(mBean: CircleMeBean)

        //  fun getUserFollow(position:Int ,uid:String,is_mutual:Int)


        fun getHotList(mBean: HotKeyBean)
    }

    interface Presenter : IPresenter<View> {
        /**
         *关键词库搜索
         */
        fun getKeyword(keyWords: String)

        /**
         *所有搜索数据
         */
        fun loadAllInfo(keyWords: String)


        /**
         *视频与文章搜索数据
         */
        fun loadVideoInfo(keyWords: String, page: Int)

        /**
         *用户搜索数据
         */
        fun loadUserInfo(keyWords: String, page: Int)
    }


}