package com.tinytiger.titi.mvp.presenter


import com.tinytiger.common.base.BasePresenter
import com.tinytiger.common.net.exception.ExceptionHandle
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.mvp.contract.SearchContract
import com.tinytiger.titi.mvp.model.SearchModel
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 下午 3:00
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索数据处理
 */
class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val videoDetailModel: SearchModel by lazy {
        SearchModel()
    }

    /**
     * 搜索类型index
     * 0 全部 1游戏 2帖子 3圈子 4用户 5资讯
     */
    fun loadSearch(index: Int, keyWords: String, page: Int) {
        when (index) {
            -1 -> {
                getKeyword(keyWords)
            }
            1 -> {
                searchGame(keyWords, page)
            }
            2 -> {
                searchPost(keyWords, page)
            }
            3 -> {
                searchCircle(keyWords, page)
            }
            5 -> {
                loadVideoInfo(keyWords, page)
            }
            4 -> {
                loadUserInfo(keyWords, page)
            }
            else -> {
                loadAllInfo(keyWords)
            }
        }
        if (page == 1) {
            mRootView?.showLoading()
        }
    }

    override fun getKeyword(keyWords: String) {
        val disposable = videoDetailModel.getKeyword(keyWords)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getKeyword(issue)
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

    override fun loadAllInfo(keyWords: String) {
        val disposable = videoDetailModel.searchAll(keyWords)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getAllSearch(issue)
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

    fun searchGame(keyWords: String, page: Int) {
        val disposable = videoDetailModel.searchGame(keyWords, page)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code == 200) {
                        getGameSearch(issue)
                    } else {
                        getErrorCode(issue)
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


    override fun loadVideoInfo(keyWords: String, page: Int) {
        val disposable = videoDetailModel.searchContent(keyWords, page)
            .subscribe({ issue ->
                mRootView?.apply {
                    if (issue.code == 200) {
                        getEssaySearch(issue)
                    } else {
                        getErrorCode(issue)
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

    override fun loadUserInfo(keyWords: String, page: Int) {
        val disposable = videoDetailModel.searchUser(keyWords, page)
            .subscribe({ issue ->
                mRootView?.apply {

                    if (issue.code == 200) {
                        getUserSearch(issue)
                    } else {
                        getErrorCode(issue)
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

    fun follow(is_mutual: Int, user_id: String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.doFollow(is_mutual, user_id)
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


    fun getHotKeyword() {
        mRootView?.showLoading()
        val disposable = videoDetailModel.getHotKeyword()
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getHotList(issue)
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

    fun gameCollection(game_name: String) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.gameCollection(game_name)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        // getHotList(issue)
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

    fun searchPost(keyWords: String, page: Int) {
        val disposable = videoDetailModel.searchPost(keyWords, page)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getPostSearch(issue)
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

    fun searchPost(keyWords: String, page: Int, circle_id: String) {
        val disposable = videoDetailModel.searchPost(keyWords, page, circle_id)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getPostSearch(issue)
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


    fun searchCircle(keyWords: String, page: Int) {
        val disposable = videoDetailModel.searchCircle(keyWords, page)
            .subscribe({ issue ->
                mRootView?.apply {
                    dismissLoading()
                    if (issue.code == 200) {
                        getCircleSearch(issue)
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

    fun likePost(post_id: String) {
        // mRootView?.showLoading()
        val disposable = videoDetailModel.likePost(post_id)
            .subscribe({ bean ->
                mRootView?.apply {
                    dismissLoading()
                    if (bean.code == 200) {
                        // showMutual(bean.data.is_mutual)
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