package com.tinytiger.titi.mvp.presenter.gametools


import android.content.Context
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.gametools.GameHomeContract
import com.tinytiger.titi.mvp.model.gametools.GameHomeModel
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/2 0002 下午 2:36
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 评价
 */
class GameHomePresenter : BasePresenter<GameHomeContract.View>(), GameHomeContract.Presenter {


    private val mModel: GameHomeModel by lazy {
        GameHomeModel()
    }

    var mContext: Context? = null


    override fun getUserViewCategoryList() {
        //  mRootView?.showLoading()
        val disposable = mModel.getUserViewCategoryList()
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        loadCategoryList(bean)
                    } else {
                        getErrorCode(bean)
                    }
                   // dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }

     fun getContentList() {
        //  mRootView?.showLoading()
        val disposable = mModel.getContentList()
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        loadContentList(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }


    //点赞评价|标签
    fun likeAssessOrTag(game_id: String, assess_id: String, is_like: Int) {
        //  mRootView?.showLoading()
        val disposable = mModel.likeAssessOrTag(game_id,assess_id,"0")
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        if (is_like != 1) {
                            GameAgentUtils.setGameDetailInfo(game_id, assess_id, 1,0)
                        }
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


    fun follow( user_id: String,is_mutual: Int) {
        mRootView?.showLoading()
        val disposable = mModel.doFollow(is_mutual, user_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        EventBus.getDefault().post(AttentionEvent(user_id, bean.data.is_mutual))
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

    fun amwayWallRecommend(source:String) {
        //  mRootView?.showLoading()
        val disposable = mModel.amwayWallRecommend(source)
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == 200) {
                        loadAmwayWall(bean)
                    } else {
                        getErrorCode(bean)
                    }
                    dismissLoading()
                }
            }, { t ->
                mRootView?.apply {
                    dismissLoading()
                    showErrorMsg(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })
        addSubscription(disposable)
    }
}