package com.tinytiger.titi.mvp.model.gametools


import com.tinytiger.common.base.BaseModel

import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.gametools.GameAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAddBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.ContentListBean
import com.tinytiger.common.net.data.home2.GameCategoryBean
import com.tinytiger.common.net.data.home2.GameListBean
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.LikeBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class GameHomeModel : BaseModel() {

    fun getUserViewCategoryList():Observable<GameCategoryBean>{
        return RetrofitManager.service.getUserViewCategoryList()
            .compose(SchedulerUtils.ioToMain())
    }

    fun getContentList():Observable<ContentListBean>{
        return RetrofitManager.service.getContentList()
            .compose(SchedulerUtils.ioToMain())
    }


    fun myGame():Observable<GameListBean>{
        return RetrofitManager.service.myGame()
            .compose(SchedulerUtils.ioToMain())
    }

    fun newGame():Observable<GameListBean>{
        return RetrofitManager.service.newGame()
            .compose(SchedulerUtils.ioToMain())
    }

    fun amwayWall():Observable<AmwayWallListBean>{
        return RetrofitManager.service.amwayWall()
            .compose(SchedulerUtils.ioToMain())
    }

    fun likeAssessOrTag(game_id:String,assess_id:String,tag_id: String):Observable<BaseBean>{
        return RetrofitManager.service.likeAssessOrTag(game_id,assess_id,tag_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun doFollow(is_mutual:Int,user_id:String): Observable<FollowUserBean> {
        //1:互相关注 0:登录者关注了对方 -1:未关注 -2:自己
        return if(is_mutual<0){
            RetrofitManager.serviceUser.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.serviceUser.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }

    fun amwayWallRecommend(source:String):Observable<AmwayWallListBean>{
        return RetrofitManager.serviceGame.amwayWallRecommend(source)
            .compose(SchedulerUtils.ioToMain())
    }




}