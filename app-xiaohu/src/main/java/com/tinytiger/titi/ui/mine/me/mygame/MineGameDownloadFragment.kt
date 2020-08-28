package com.tinytiger.titi.ui.mine.me.mygame

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.mygame.MineGameDownloadAdapter
import com.tinytiger.titi.sql.SQLiteDB
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.utils.MarketUtil
import com.tinytiger.titi.widget.popup.PopupGameInfo
import com.xwdz.download.core.DownloadEntry
import com.xwdz.download.core.QuietDownloader
import com.xwdz.download.notify.DataUpdatedWatcher

import kotlinx.android.synthetic.main.base_recycler.*


/*
* @author Tamas
* create at 2020/02/27 0013
* Email: ljw_163mail@163.com
* description: 我的游戏 下载 Fragment
*/
class MineGameDownloadFragment : BaseFragment() {

    /**
     *   0  "下载", 1 "已装"
     */
    private var type = 0

    companion object {

        fun getInstance(type: Int): MineGameDownloadFragment {
            val fragment = MineGameDownloadFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.type = type
            return fragment
        }
    }

    private val mAdapter by lazy { MineGameDownloadAdapter() }
    override fun getLayoutId(): Int = R.layout.base_recycler


    override fun initView() {
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(context!!)
        mAdapter.setEmptyView(RefreshView.getEmptyView(context, "", recycler_view))
        refreshLayout.setEnableLoadMore(false)
        refreshLayout.setOnRefreshListener {
            (activity as MineGameDownloadActivity).getDownloadData2()
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            GameDetailActivity.actionStart(context!!, mAdapter.data[position].id, 7)
        }


        mAdapter.mOnGameDownloadListener=object:MineGameDownloadAdapter.OnGameDownloadListener{
            override fun clickMore(view: View, item: MineGameBean, status: DownloadEntry.Status) {

                var info = "取消下载"
                if (type == 1) {
                    info = "卸载游戏"
                }else if (status==DownloadEntry.Status.COMPLETED){
                    info = "删除安装包"
                }
                mGameBean=item
                clickMore(view,info)
            }
        }


        if (type == 0) {
            mAdapter.setNewInstance((activity as MineGameDownloadActivity).mApkList)
        } else {
            mAdapter.setNewInstance((activity as MineGameDownloadActivity).mApkOverList)
        }

    }

    var mGameBean:MineGameBean? =null

    override fun start() {

    }

    override fun onResume() {
        super.onResume()
        QuietDownloader.addObserver(watcher)
    }

    fun setNewData() {
        mAdapter.notifyDataSetChanged()
        if (refreshLayout!=null){
            refreshLayout.finishRefresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mPopupWindow!=null){
            mPopupWindow?.dismiss()
            mPopupWindow=null
        }
        QuietDownloader.removeObserver(watcher)
    }


    private val watcher = object : DataUpdatedWatcher() {
        override fun notifyUpdate(data: DownloadEntry) {
            mAdapter.notifyUpdate(data)
        }
    }


    var mPopupWindow: PopupGameInfo? = null
    /**
     * 取消收藏pop
     */
    fun clickMore(rootView: View,info:String) {
        if (mPopupWindow == null) {
            mPopupWindow = PopupGameInfo(activity)
            mPopupWindow!!.setShowAnimation(createTranslateAnimation(1f, 0f, 0f, 0f))
                .dismissAnimation = createTranslateAnimation(0f, 1f, 0f, 0f)
            mPopupWindow!!.popupGravity = Gravity.END
        }


        mPopupWindow?.setPopInfo(info) {
            mPopupWindow?.dismiss()

            if (mGameBean==null){
                return@setPopInfo
            }

            if (type == 0) {
                //删除本地记录
                SQLiteDB.getInstance(activity).delete(mGameBean!!.id)
                //删除下载缓存
                QuietDownloader.deleteById(mGameBean!!.download_url)
                mAdapter.remove(mGameBean!!)
                (activity as MineGameDownloadActivity).showNumber(0,4)
            } else {
                MarketUtil.showInstalledAppDetails(activity,
                    mGameBean!!.package_name_android)
            }
        }

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null).setBlurBackgroundEnable(false)
            .showPopupWindow(location[0] - Dp2PxUtils.dip2px(context, 80),
                location[1] + Dp2PxUtils.dip2px(context, 7))
    }


    private fun createTranslateAnimation(fromX: Float, toX: Float, fromY: Float,
        toY: Float): Animation? {
        val animation: Animation =
            TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY)
        animation.duration = 500
        animation.interpolator = DecelerateInterpolator()
        return animation
    }
}