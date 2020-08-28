package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.GameWikiDetailBean
import com.tinytiger.common.net.data.gametools.GameWikiListBean
import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface  GameWikiContract{
    interface View : IBaseView {



        fun showWikiModularList(bean: WikiModularList)

        fun showWikiModularOtherList(bean: WikiModularOtherList)

        fun showGameWikiDetail(bean: WikiDitailList)

        fun showCollectGameWiki(is_collect:Int)

        fun ApplyAdmin(msg:String)

        fun showWikiStatusList(bean: WikiStatusList)
    }

    interface Presenter : IPresenter<View> {

        fun collectGameWiki(wiki_id:String,is_collect:Int)
    }
}