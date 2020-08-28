package com.tinytiger.titi.mvp.presenter.mine


import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.data.BaseBean

import com.tinytiger.titi.mvp.contract.mine.ReportContract

import com.tinytiger.titi.mvp.model.mine.ReportModel
import io.reactivex.Observable


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 举报处理
 */
class ReportPresenter : BasePresenter<ReportContract.View>(), ReportContract.Presenter {

    private val videoDetailModel: ReportModel by lazy {
        ReportModel()
    }


    fun report(reportType:Int,reportId:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
       when(reportType){
           1->{
               reportComment1(reportId, report_reason, supplement, images_url_1, images_url_2)
           }
           2->{
               reportComment(reportId, report_reason, supplement, images_url_1, images_url_2)
           }
           3->{
               reportUser(reportId, report_reason, supplement, images_url_1, images_url_2)
           }
           5->{
               reportComment5(reportId, report_reason, supplement, images_url_1, images_url_2)
           }
           6->{
               reportPost(reportId, report_reason, supplement, images_url_1, images_url_2)
           }
           else->{
               reportArticle(reportId, report_reason, supplement, images_url_1, images_url_2)
           }
       }
    }


    override fun reportArticle(content_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.report(content_id,report_reason,supplement,images_url_1,images_url_2)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getReportData()
                    }else{
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

    fun reportComment1(content_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
        val disposable = videoDetailModel.report(content_id,report_reason,supplement,images_url_1,images_url_2)
        commonReportComment(disposable)
    }
    override fun reportComment(comment_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
        val disposable = videoDetailModel.report2(comment_id,report_reason,supplement,images_url_1,images_url_2)
        commonReportComment(disposable)
    }
    fun reportUser(user_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
        val disposable = videoDetailModel.reportUser(user_id,report_reason,supplement,images_url_1,images_url_2)
        commonReportComment(disposable)
    }
    fun reportComment5(assess_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
        val disposable = videoDetailModel.report5(assess_id,report_reason,supplement,images_url_1,images_url_2)
        commonReportComment(disposable)
    }
    fun reportPost(post_id:String,report_reason:Int,supplement:String,images_url_1:String,images_url_2:String) {
        val disposable = videoDetailModel.reportPost(post_id,report_reason,supplement,images_url_1,images_url_2)
        commonReportComment(disposable)
    }

    private fun commonReportComment(disposable: Observable<BaseBean>){
        mRootView?.showLoading()
        var subscribe=disposable.subscribe({ issue ->
            mRootView?.apply {
                dismissLoading()
                if (issue.code==200){
                    getReportData()
                }else{
                    getErrorCode(issue)
                }
            }
        }, { t ->
            mRootView?.apply {
                dismissLoading()
                showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
            }
        })
        addSubscription(subscribe)
    }
    override fun loadQiniuToken() {
        mRootView?.showLoading()
        val disposable = videoDetailModel.getQiniuToken()
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code==200){
                        getQiniuToken(issue.data.qiniu_token)
                    }else{
                        dismissLoading()
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }


     fun feedback(feedback_type:Int,problem_desc:String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.feedback(feedback_type,problem_desc)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code==200){
                        getReportData()
                    }else{
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }
}