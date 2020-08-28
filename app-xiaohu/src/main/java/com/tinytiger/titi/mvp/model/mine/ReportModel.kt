package com.tinytiger.titi.mvp.model.mine



import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.base.BaseModel


import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable


class ReportModel : BaseModel() {


/*    content_id	int
    内容id content_id和comment_id必须传一个

    comment_id	int
    评论id content_id和comment_id必须传一个

    report_type	int
    举报类型： 1内容 2评论

    report_reason	int
    举报理由：1,低俗色情、2.涉赌内容、3.涉政内容、4.涉军内容、5.垃圾广告、6.内容与标题严重不符

    supplement	string
    补充内容

    images_url_1	string
    上传图片 url地址 第一张

    images_url_2	string
    上传图片 url地址 第二张*/

    fun report(content_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String):Observable<BaseBean>{
        return RetrofitManager.service.report(content_id,report_reason,supplement,images_url_1,images_url_2)
                .compose(SchedulerUtils.ioToMain())
    }

    fun report2(comment_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String):Observable<BaseBean>{
        return RetrofitManager.service.report2(comment_id,report_reason,supplement,images_url_1,images_url_2)
            .compose(SchedulerUtils.ioToMain())
    }

    fun reportUser(userId:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String):Observable<BaseBean>{
        return RetrofitManager.service.reportUser(userId,report_reason,supplement,images_url_1,images_url_2)
            .compose(SchedulerUtils.ioToMain())
    }
    fun report5(assess_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String):Observable<BaseBean>{
        return RetrofitManager.service.report5(assess_id,report_reason,supplement,images_url_1,images_url_2)
            .compose(SchedulerUtils.ioToMain())
    }

    fun reportPost(post_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String):Observable<BaseBean>{
        return RetrofitManager.service.reportPost(post_id,report_reason,supplement,images_url_1,images_url_2)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getQiniuToken():Observable<QiniuBean>{
        return RetrofitManager.service.getQiniuToken()
            .compose(SchedulerUtils.ioToMain())
    }

    fun feedback(feedback_type:Int,problem_desc:String):Observable<BaseBean>{
        return RetrofitManager.service.feedback(feedback_type,problem_desc)
            .compose(SchedulerUtils.ioToMain())
    }
}