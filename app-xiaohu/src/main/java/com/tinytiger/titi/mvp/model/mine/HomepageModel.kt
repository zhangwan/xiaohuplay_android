package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.data.user.FollowUserBean
import io.reactivex.Observable


class HomepageModel : BaseModel() {

//    fun cancelBlack(user_id:String): Observable<BaseBean> {
//        return RetrofitManager.service.cancelBlack(user_id)
//            .compose(SchedulerUtils.ioToMain())
//    }
    fun getHomepageInfo(user_id:String): Observable<UserCenterBean> {
        return RetrofitManager.service.getHomepageInfo(user_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun doFollow(is_mutual:Int,user_id:String): Observable<FollowUserBean> {
        return if(is_mutual<0){
            RetrofitManager.service.doFollow(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            RetrofitManager.service.cancelFans(user_id)
                .compose(SchedulerUtils.ioToMain())
        }
    }

    fun changeBackgroundImg(background_img:String): Observable<BaseBean> {
        return RetrofitManager.service.changeBackgroundImg(background_img)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getHomepageWorks(user_id:String,page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.getHomepageWorks(user_id,page,20)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getHomepageLike(user_id:String,page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.getHomepageLike(user_id,page,20)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getQiniuToken(): Observable<QiniuBean> {
        return RetrofitManager.service.getQiniuToken()
            .compose(SchedulerUtils.ioToMain())
    }

    fun addBlack(is_black:Int,user_id:String): Observable<BaseBean> {
        if(is_black == 0){
            return RetrofitManager.service.addBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
        }else{
            return RetrofitManager.service.cancelBlack(user_id)
                .compose(SchedulerUtils.ioToMain())
        }

    }


    fun indexWorks(page:Int): Observable<MyContentListBean> {
        return RetrofitManager.service.indexWorks(page)
            .compose(SchedulerUtils.ioToMain())
    }


}