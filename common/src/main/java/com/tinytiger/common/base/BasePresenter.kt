package com.tinytiger.common.base


import com.tinytiger.common.R
import com.tinytiger.common.event.ClassEvent

import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus


open class BasePresenter<T : IBaseView> : IPresenter<T> {

    var mRootView: T? = null
        private set

    private var compositeDisposable = CompositeDisposable()


    /**
     * 页面初始化中绑定
     */
    override fun attachView(mRootView: T) {
        this.mRootView = mRootView
    }

    /**
     * 需要在activity或fragment onDestroy中调用销毁
     */
    override fun detachView() {
        mRootView = null
         //保证activity结束时取消所有正在执行的订阅
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.clear()
        }
    }

    private val isViewAttached: Boolean
        get() = mRootView != null

    /**
     * 附件检测,是否绑定view
     */
    fun checkViewAttached() {
        if (!isViewAttached) throw MvpViewNotAttachedException()
    }

    /**
     * 接口调用时调用出触发
     */
    fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * 页面未绑定view,请求数据异常
     */
    private class MvpViewNotAttachedException internal constructor() : RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")


    /**
     * 服务器返回正确
     * 数据异常错误处理
     */
     fun getErrorCode(baseBean: BaseBean){
        when(baseBean.code){
            1000->{//停服通知
                EventBus.getDefault().post(ClassEvent("StopActivity",1000,baseBean.msg))
            }
            3000->{//登录失效
                //ToastUtils.toshort(BaseApp.getContext(), baseBean.msg)
                SpUtils.saveSP(R.string.token, "")
                EventBus.getDefault().post(ClassEvent("LoginActivity",1))
            }
            3008->{//用戶登陸禁止
                ToastUtils.ToastLongCenter(BaseApp.getContext(), baseBean.msg)
                SpUtils.saveSP(R.string.token, "")
                EventBus.getDefault().post(ClassEvent("LoginActivity",1))
            }
            else->{
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(baseBean.msg,baseBean.code)
                }
            }
        }
    }


    open fun bindSocialPlatform(type: Int, unionid: String, nickname: String) {}
}