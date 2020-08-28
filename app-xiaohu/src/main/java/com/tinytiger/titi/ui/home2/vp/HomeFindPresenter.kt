package com.tinytiger.titi.ui.home2.vp

import com.tinytiger.common.basis.BasisPresenter
import com.tinytiger.common.http.HttpBaseCallBack
import com.tinytiger.common.http.HttpConstants
import com.tinytiger.common.http.HttpLoadingCallback
import com.tinytiger.common.http.HttpRequestUtil
import com.tinytiger.common.net.data.ad.AdFindBean
import com.tinytiger.common.net.data.ad.AdListBean
import com.tinytiger.common.net.data.ad.DiscoverClassify
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.home2.HomeFindFragment

/**
 * @author zwy
 * create at 2020/6/29 0029
 * description:
 */
class HomeFindPresenter : BasisPresenter<HomeFindFragment>() {

    /**
     * 是否加载过
     */
    private var typeAd = false

    fun loadAdList() {
        if (typeAd) {
            return
        }
        val hashMap = mapOf<String, String>()
        HttpRequestUtil.onGet(HttpConstants.Home.GET_DISCOVER_INDEX, hashMap,
            object : HttpLoadingCallback<AdFindBean>() {
                override fun onResponse(any: AdFindBean) {
                    super.onResponse(any)
                    typeAd = true
                    mvpView?.getAdList(any)
                    mvpView?.finishLoad()
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    typeAd = false
                    mvpView?.finishLoad()
                }
            })
    }

    fun clickAdRecord(ad_id: String) {
        val hashMap = mapOf(
            "ad_id" to ad_id,
            "user_id" to SpUtils.getString(R.string.user_id, ""),
            "position_id" to "8"    //8发现banner
        )
        HttpRequestUtil.onGet(HttpConstants.Home.CLICK_AD_RECORD, hashMap,
            object : HttpBaseCallBack<AdListBean>() {
                override fun onResponse(any: AdListBean) {
                    super.onResponse(any)
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }

    fun amwayWallRecommend(source: String) {
        val hashMap = mapOf("source" to source)
        HttpRequestUtil.onGet(HttpConstants.Home.AMWAY_WALL_RECOMMEND, hashMap,
            object : HttpBaseCallBack<AmwayWallListBean>() {
                override fun onResponse(any: AmwayWallListBean) {
                    super.onResponse(any)
                    mvpView?.loadAmwayWall(any)
                    mvpView?.finishLoad()
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    mvpView?.finishLoad()
                    super.onFailure(errorCode, errorMsg)
                }
            })
    }

    fun getDiscoverClassify(page: Int) {
        val hashMap = mapOf("page" to page.toString(), "per_page" to "15")
        HttpRequestUtil.onGet(HttpConstants.Home.GET_DISCOVER_CLASSIFY, hashMap,
            object : HttpBaseCallBack<DiscoverClassify>() {
                override fun onResponse(any: DiscoverClassify) {
                    super.onResponse(any)
                    mvpView?.loadDiscoverClassify(any)
                }

                override fun onFailure(errorCode: String, errorMsg: String) {
                    super.onFailure(errorCode, errorMsg)
                    mvpView?.finishLoad()
                    mvpView?.showErrorMsg(errorMsg, errorCode)
                }
            })
    }

}