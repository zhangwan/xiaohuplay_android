package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.common.net.data.mine.FeedbackListBean
import com.tinytiger.common.net.data.mine.MyContentListBean
import io.reactivex.Observable


class FeedbackModel : BaseModel() {


    fun getCatList(): Observable<CatListBean> {
        return RetrofitManager.service.getCatList()
            .compose(SchedulerUtils.ioToMain())
    }
    fun getProposalList(opinion_cat_id:Int,page:Int): Observable<FeedbackListBean> {
        return RetrofitManager.service.getProposalList(opinion_cat_id,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun addProposal(opinion_cat_id:Int,content:String): Observable<BaseBean> {
        return RetrofitManager.service.addProposal(opinion_cat_id,content)
            .compose(SchedulerUtils.ioToMain())
    }



}