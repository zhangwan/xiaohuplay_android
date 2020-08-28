package com.tinytiger.titi.ui.mine.other

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.basis.BasisActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.gametools.GameInfoBean
import com.tinytiger.common.net.data.gametools.GamePlayListBean
import com.tinytiger.common.net.data.gametools.GameShareInfoBean
import com.tinytiger.common.net.data.gametools.ShareInfoBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.ShareGameDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.GameCloudAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.ui.mine.other.vp.GameCloudPresenter
import kotlinx.android.synthetic.main.activity_game_cloud.*

import kotlinx.android.synthetic.main.mine_fragment_collection.recycler_view
import kotlinx.android.synthetic.main.mine_fragment_collection.refreshLayout
import org.greenrobot.eventbus.EventBus

/**
 * @author zwy
 * create at 2020/7/3 0003
 * description:云游戏详情页
 */
class GameCloudActivity : BasisActivity<GameCloudPresenter>() {

    companion object {
        fun actionStart(context: Context) {
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, GameCloudActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mAdapter by lazy { GameCloudAdapter() }
    var page = 1
    var mTvDuration:TextView?=null
    var shareBean: ShareInfoBean?=null
    var surplusTime:Int?=null
    var headerView:View?=null

    override fun layoutId(): Int = R.layout.activity_game_cloud

    override fun initData() {

    }

    override fun initView() {
        tvTitle.centerTxt = getString(R.string.mine_game)
        tvTitle.setIvLine(View.GONE)
        recycler_view.layoutManager = LinearLayoutManager(this)
        headerView= layoutInflater.inflate(R.layout.game_cloude_header,
            recycler_view.parent as ViewGroup, false)
        var tvTip=headerView?.findViewById<TextView>(R.id.tvAddDuration)
        mTvDuration=headerView?.findViewById<TextView>(R.id.tvDuration)
        tvTip?.setOnClickListener {
            shareData()
        }
        mAdapter?.addHeaderView(headerView!!)

        recycler_view.adapter = mAdapter
        initListener()
        basePresenter?.getPlayList(page)
    }
    fun shareData(){
        ShareDialog.create(supportFragmentManager)
            .apply {
                class_name = "no"
                share_url = shareBean?.app_download_url!!
                share_title = shareBean?.title!!
                share_desc = shareBean?.slogan!!
                share_image = shareBean?.logo_url!!
                userId = SpUtils.getString(com.tinytiger.common.R.string.user_id, "")
            }.setOnItemClickListener(object : ShareDialog.OnItemClickListener {
                override fun click(type: Int) {
                    basePresenter?.setShareRelation(shareBean?.code!!)
                }
            })
            .show()
    }

    fun initListener() {
        tvTitle.setBackOnClick { finish() }


        refreshLayout.setOnRefreshListener {
            page = 1
            basePresenter?.getPlayList(page)

        }

        refreshLayout.setOnLoadMoreListener {
            page++
            basePresenter?.getPlayList(page)

        }

    }


    fun getPlayBean(bean:GamePlayListBean){
        refreshLayout.visibility=View.VISIBLE
        headerView?.findViewById<TextView>(R.id.tvDetails)?.visibility=View.VISIBLE
        headerView?.findViewById<View>(R.id.view_line)?.visibility=View.VISIBLE
        this.shareBean=bean.share_info
        surplusTime=bean.remaining_time?.toInt()
        mTvDuration?.text=TimeZoneUtil.convertTime(surplusTime,false)
        if (bean.play_list?.current_page == 1) {
            if (bean.play_list?.data != null && bean.play_list?.data!!.size > 0) {
                mAdapter.setNewInstance(bean.play_list?.data)
            } else{
                headerView?.findViewById<TextView>(R.id.tvDetails)?.visibility=View.GONE
                headerView?.findViewById<View>(R.id.view_line)?.visibility=View.GONE
            }
        } else {
            mAdapter.addData(bean.play_list?.data!!)
        }

        if (bean.play_list?.current_page!! >= bean.play_list?.last_page!!) {
            refreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            refreshLayout.resetNoMoreData()
        }
    }
    fun showAddGameTime(time: Int){
        surplusTime = surplusTime?.plus(time)
        mTvDuration?.text=TimeZoneUtil.convertTime(surplusTime,false)
    }


}