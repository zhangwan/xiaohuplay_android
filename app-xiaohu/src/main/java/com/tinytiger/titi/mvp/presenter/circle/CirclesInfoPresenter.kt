package com.tinytiger.titi.mvp.presenter.circle

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.center.HomeDynamicContract
import com.tinytiger.titi.mvp.contract.center.MineCollectContract
import com.tinytiger.titi.mvp.contract.center.MineGameContract
import com.tinytiger.titi.mvp.contract.circle.CirclesContract
import com.tinytiger.titi.mvp.contract.circle.CirclesInfoContract
import com.tinytiger.titi.mvp.model.center.HomeDynamicModel
import com.tinytiger.titi.mvp.model.center.MineCenterModel
import com.tinytiger.titi.mvp.model.circle.CirclesInfoModel
import com.tinytiger.titi.mvp.model.circle.CirclesModel
import org.greenrobot.eventbus.EventBus

class CirclesInfoPresenter : BasePresenter<CirclesInfoContract.View>(), CirclesInfoContract.Presenter {


    private val mModel: CirclesInfoModel by lazy {
        CirclesInfoModel()
    }


    override fun getGameCircleInfo(game_id: String,circle_id:String) {
         mRootView?.showLoading()
        val disposable = mModel.getGameCircleInfo(game_id,circle_id)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code==200) {
                        showGameCircleInfo(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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

    fun getCircleTagModularPostList(circle_id:String,tag_modular_id: String,fixed_modular_type: String,page: Int) {
        val disposable = mModel.getCircleTagModularPostList(circle_id,tag_modular_id,fixed_modular_type,page)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code==200) {
                        showCircleList(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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

    fun doFollow(is_mutual: Int, user_id: String) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual, user_id)
            .subscribe({ bean ->
                mRootView?.apply {

                    if (bean.code == 200) {
                        EventBus.getDefault().post(AttentionEvent(user_id, bean.data.is_mutual))
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
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

    fun likePost(post_id:String) {
        // mRootView?.showLoading()
        val disposable = mModel.likePost(post_id)
            .subscribe({bean ->
                mRootView?.apply {

                    if (bean.code==200){
                        // showMutual(bean.data.is_mutual)
                    }else{
                        getErrorCode(bean)
                    }
                    dismissLoading()
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
    fun joinCircle(circle_id: String,is_join:Int) {
        if (FastClickUtil.isFastClick()){
            return
        }

        mRootView?.showLoading()
        val disposable = mModel.joinCircle(circle_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code==200) {
                        if (is_join==1){
                            showJoin(0)
                        }else{
                            showJoin(1)
                        }
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