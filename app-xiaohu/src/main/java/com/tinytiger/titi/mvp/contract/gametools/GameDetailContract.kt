package com.tinytiger.titi.mvp.contract.gametools

import com.tinytiger.common.base.IBaseView
import com.tinytiger.common.base.IPresenter
import com.tinytiger.common.net.data.gametools.GameCommentList
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description:
*/
interface GameDetailContract {
    interface View : IBaseView {



        fun showGameInfoCommentList(bean:GameCommentList.Data)


        fun showGameInfoData(bean: GameInfoDetailBean.Data)

        fun showGameFollow(is_follow: Int,currentItem: Int)

        fun showlikeAssess(position: Int,message: String,is_like:Int)

        fun showFollowStatus(is_follow: Int, user_id: String)

        fun addAppointSuccess()
    }

    interface Presenter : IPresenter<View> {

        fun getGameInfo(game_id: String,user_id:String)



        fun getGameInfoCommentList(game_id: String,user_id: String,star_class: String,comment_list_status: Int,page:Int)

          fun GameFollow(game_id: String, is_follow: Int)

        fun likeAssessOrTag(game_id: String, assess_id: String,is_like:Int)

        fun doFollow(user_id:String,is_follow:Int)

        fun addAppointment(game_id:String)
    }
}