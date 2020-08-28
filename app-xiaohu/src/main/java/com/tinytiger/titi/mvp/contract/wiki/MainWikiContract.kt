package com.tinytiger.titi.mvp.contract.wiki

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.wiki.MainWikiListBean
import com.tinytiger.common.net.data.wiki.WikiSearchListBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:首页 百科
*/
interface  MainWikiContract{
    interface View : IBaseView {

        fun showMainWiki(data: MainWikiListBean.DataBean)

        fun showSearchWiki(data: WikiSearchListBean.DataBean)
    }

    interface Presenter : IPresenter<View> {

        fun getMainWiki()
        fun searchWiki(content:String,game_id:String,page:Int)


    }
}