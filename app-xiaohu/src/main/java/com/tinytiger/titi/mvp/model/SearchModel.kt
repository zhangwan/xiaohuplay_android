package com.tinytiger.titi.mvp.model



import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.search.*
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class SearchModel : BaseModel() {

    fun getKeyword(title:String):Observable<SearchKeywordBean>{
        return RetrofitManager.service.getKeyword(title)
                .compose(SchedulerUtils.ioToMain())
    }

    fun searchAll(title:String):Observable<SearchAllBean>{
        return RetrofitManager.service.searchAll(title)
            .compose(SchedulerUtils.ioToMain())
    }

    fun searchGame(title:String,page:Int):Observable<SearchGameBean>{
        return RetrofitManager.service.searchGame(title,page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun searchUser(title:String,page:Int):Observable<SearchUserBean>{
        return RetrofitManager.service.searchUser(title,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun searchContent(title:String,page:Int):Observable<SearchNewsBean>{
        return RetrofitManager.service.searchContent(title,page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun doFollow(uid:String):Observable<FollowUserBean>{
        return RetrofitManager.service.doFollow(uid)
            .compose(SchedulerUtils.ioToMain())
    }

    fun cancelFans(uid:String):Observable<FollowUserBean>{
        return RetrofitManager.service.cancelFans(uid)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getHotKeyword():Observable<HotKeyBean>{
        return RetrofitManager.service.getHotKeyword()
            .compose(SchedulerUtils.ioToMain())
    }

    fun gameCollection(game_name: String):Observable<BaseBean>{
        return RetrofitManager.service.gameCollection(game_name)
            .compose(SchedulerUtils.ioToMain())
    }

    fun searchPost(title: String,page:Int):Observable<PostInfoBean>{
        return RetrofitManager.serviceMoment.searchPost(title,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun searchPost(title: String,page:Int,circle_id : String):Observable<PostInfoBean>{
        return RetrofitManager.serviceMoment.searchPost(title,page,circle_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun searchCircle(title: String,page:Int):Observable<CircleMeBean>{
        return RetrofitManager.serviceMoment.searchCircle(title,page)
            .compose(SchedulerUtils.ioToMain())
    }
    fun likePost(post_id:String):Observable<BaseBean>{
        return RetrofitManager.serviceMoment.likePost(post_id)
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

}