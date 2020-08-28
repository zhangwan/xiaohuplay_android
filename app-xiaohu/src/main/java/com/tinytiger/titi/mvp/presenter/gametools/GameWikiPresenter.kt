package com.tinytiger.titi.mvp.presenter.gametools

import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.mvp.contract.gametools.GameWikiContract
import com.tinytiger.titi.mvp.model.gametools.GameDetailModel
import com.tinytiger.titi.mvp.model.gametools.GameWikiModel

class GameWikiPresenter : BasePresenter<GameWikiContract.View>(), GameWikiContract.Presenter {


    private val mModel: GameWikiModel by lazy {
        GameWikiModel()
    }

    fun modularInfo(game_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.modularInfo(game_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showWikiModularList(bean)
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
    fun otherModularInfo(submod_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.otherModularInfo(submod_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showWikiModularOtherList(bean)
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
     * 游戏详情-百科详情
     */
     fun getGameWikiDetail(wiki_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.getTemContentInfo(wiki_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showGameWikiDetail(bean)
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
    override fun collectGameWiki(wiki_id:String,is_collect:Int) {
        mRootView?.showLoading()
        val disposable = mModel.collectWiki(wiki_id,is_collect)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        if (is_collect==1){
                            showCollectGameWiki(0)
                        }else{
                            showCollectGameWiki(1)
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



    /**
     * 申请管理员
     */
    fun addApplyAdmin(game_id: String, about_game: String,
                      connect_info: String, has_experience: String,
                      play_game_time: String,week_time: String,
                      the_age: String, office_hours: String,
                      other_game: String, other_game_name: String) {
        mRootView?.showLoading()
        val disposable = mModel.addApplyAdmin(game_id,about_game,connect_info,has_experience,play_game_time,week_time,the_age,office_hours,other_game,other_game_name)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        ApplyAdmin(bean.msg)
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


     fun bannerClick(banner_id:String) {
        val disposable = mModel.bannerClick(banner_id)
            .subscribe()
        addSubscription(disposable)
    }

    fun getWikiStatus(wiki_id:String,page:Int) {

        mRootView?.showLoading()
        val disposable = mModel.getWikiStatus(wiki_id,page)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showWikiStatusList(bean)
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
    fun openWiki(wiki_id:String) {
        mRootView?.showLoading()
        val disposable = mModel.openWiki(wiki_id)
            .subscribe({bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        showCollectGameWiki(1)
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