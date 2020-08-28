package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.mine.talent.TalentBean
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.FriendListBean
import io.reactivex.Observable


class TalentModel : BaseModel() {



    fun masterApply(): Observable<TalentBean> {
        return RetrofitManager.serviceOther.masterApply()
            .compose(SchedulerUtils.ioToMain())
    }


    fun getQiniuToken():Observable<QiniuBean>{
        return RetrofitManager.serviceOther.getQiniuToken()
            .compose(SchedulerUtils.ioToMain())
    }

    fun submitMasterApply(key: String,real_name: String,id_number: String,
                          image_one: String,image_two: String,profile: String): Observable<BaseBean> {
        return RetrofitManager.serviceOther.submitMasterApply("1",key,real_name,id_number,image_one,image_two,profile)
            .compose(SchedulerUtils.ioToMain())
    }


    fun submitMasterApply1(key: String,external_name: String,site_name: String,image_one: String,image_two: String,profile: String): Observable<BaseBean> {
        return RetrofitManager.serviceOther.submitMasterApply1(2,key,external_name,site_name,image_one,image_two,profile)
            .compose(SchedulerUtils.ioToMain())
    }
}