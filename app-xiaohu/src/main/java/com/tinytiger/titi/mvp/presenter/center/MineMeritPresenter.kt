package com.tinytiger.titi.mvp.presenter.center

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.center.MineMeritContract
import com.tinytiger.titi.mvp.model.center.MineCenterModel

class MineMeritPresenter : BasePresenter<MineMeritContract.View>(), MineMeritContract.Presenter {



    private val mModel: MineCenterModel by lazy {
        MineCenterModel()
    }




    override fun wearMedal(medal_id: String,position:Int) {
          mRootView?.showLoading()
        val disposable = mModel.wearMedal(medal_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                         showWearMedal(position)
                        showErrorMsg(issue.msg, issue.code)
                    } else {
                        getErrorCode(issue)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }


    override fun getMedalList(page:Int) {
//        mRootView?.showLoading()
        val disposable = mModel.getMedalList(page)
            .subscribe({bean ->


                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        mRootView?.showMedalList(bean.data)
                    } else {
                        getErrorCode(bean)
                    }
                }


            }, { t ->
                mRootView?.dismissLoading()
                mRootView?.showErrorMsg(
                    ExceptionHandle.handleException(t),
                    ExceptionHandle.errorCode

                )
            })
        addSubscription(disposable)
    }


}