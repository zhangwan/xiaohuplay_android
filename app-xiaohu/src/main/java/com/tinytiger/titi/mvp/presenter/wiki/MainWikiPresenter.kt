package com.tinytiger.titi.mvp.presenter.wiki

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.wiki.MainWikiContract
import com.tinytiger.titi.mvp.model.wiki.MainWikiModel

class MainWikiPresenter : BasePresenter<MainWikiContract.View>(), MainWikiContract.Presenter {


    private val mModel: MainWikiModel by lazy {
        MainWikiModel()
    }

    /**
     * 游戏百科
     */
    override fun getMainWiki() {
        mRootView?.showLoading()
        val disposable = mModel.getGameWiki()
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showMainWiki(bean.data)
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


    /**
     * 收藏游戏百科
     */
    override fun searchWiki(content:String,game_id:String,page:Int) {
        mRootView?.showLoading()
        val disposable = mModel.searchWiki(content,game_id,page)
            .subscribe({bean ->

                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showSearchWiki(bean.data)
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


//
//
//    /**
//     * 申请管理员
//     */
//    fun addApplyAdmin(game_id: String, about_game: String,
//                      connect_info: String, has_experience: String,
//                      play_game_time: String,week_time: String,
//                      the_age: String, office_hours: String,
//                      other_game: String, other_game_name: String) {
//        mRootView?.showLoading()
//        val disposable = mModel.addApplyAdmin(game_id,about_game,connect_info,has_experience,play_game_time,week_time,the_age,office_hours,other_game,other_game_name)
//            .subscribe({bean ->
//
//                mRootView?.apply {
//                    dismissLoading()
//                    if (bean.code == 200) {
//                        ApplyAdmin(bean.msg)
//                    } else {
//                        getErrorCode(bean)
//                    }
//
//                }
//
//            }, { t ->
//                mRootView?.dismissLoading()
//                mRootView?.showErrorMsg(
//                    ExceptionHandle.handleException(t),
//                    ExceptionHandle.errorCode
//
//                )
//            })
//        addSubscription(disposable)
//    }
//
//
//     fun bannerClick(banner_id:String) {
//        val disposable = mModel.bannerClick(banner_id)
//            .subscribe()
//        addSubscription(disposable)
//    }
}