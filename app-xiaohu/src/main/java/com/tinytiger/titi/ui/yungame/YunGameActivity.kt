package com.tinytiger.titi.ui.yungame


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnSystemUiVisibilityChangeListener
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.haima.hmcp.Constants
import com.haima.hmcp.HmcpManager
import com.haima.hmcp.beans.Message
import com.haima.hmcp.beans.UserInfo
import com.haima.hmcp.enums.ErrorType
import com.haima.hmcp.enums.NetWorkState
import com.haima.hmcp.enums.ScreenOrientation
import com.haima.hmcp.listeners.HmcpPlayerListener
import com.haima.hmcp.listeners.OnUpdataGameUIDListener
import com.haima.hmcp.utils.CryptoUtils
import com.haima.hmcp.utils.StatusCallbackUtil
import com.haima.hmcp.widgets.HmcpVideoView
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.basis.BasisActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.ShareInfo
import com.tinytiger.common.net.data.yungaem.YunTimeBean
import com.tinytiger.common.utils.AnimationUtil
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.ShareGameDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.yun.QualityAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.yungame.vp.YunGamePresenter
import com.tinytiger.titi.widget.dialog.YunGameDialog
import com.xwdz.download.core.QuietDownloader
import kotlinx.android.synthetic.main.yun_activity_game.*
import org.greenrobot.eventbus.EventBus
import java.lang.Exception


/**
 *
 * @author zhw_luke
 * @date 2020/7/2 0002 下午 2:21
 * @Copyright 小虎互联科技
 * @since 3.3.0
 * @doc 云游戏
 */
class YunGameActivity : BasisActivity<YunGamePresenter>(), HmcpPlayerListener,
    OnSystemUiVisibilityChangeListener {

    companion object {
        fun actionStart(context: Context, game_id: String, gamePackage: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, YunGameActivity::class.java)
            intent.putExtra("game_id", game_id)
            intent.putExtra("gamePackage", gamePackage)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.yun_activity_game
    private val mAdapter by lazy { QualityAdapter() }
    private var gamePackage = ""
    private var game_id = ""

    private var seq = 1
    private val handler: Handler = @SuppressLint("HandlerLeak") object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                10 -> {
                    removeMessages(10)
                    checkPlaying()
                }
                12 -> {
                    removeMessages(12)
                    setTime()
                    sendEmptyMessageDelayed(12, 1000)
                }
                30 -> {
                    //云游戏心跳
                    removeMessages(30)
                    basePresenter?.setPing(seq, showView)
                    seq++
                    sendEmptyMessageDelayed(30, 3000)
                }
            }
        }
    }

    override fun initData() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.decorView.setOnSystemUiVisibilityChangeListener(this)

        gamePackage = intent.getStringExtra("gamePackage")
        game_id = intent.getStringExtra("game_id")
        setSwipeBackEnable(false)
    }


    override fun initView() {
        iv_more.setOnClickListener {
            setMenuShow()
        }
        vView.setOnClickListener {
            if (FastClickUtil.isFastClick()) {
                return@setOnClickListener
            }
            setMenuShow()
        }
        rl_content.setOnClickListener {

        }
        tvSet.setOnClickListener {
            llMore.visibility = View.GONE
            rvQuality.visibility = View.VISIBLE
            mAdapter.notifyDataSetChanged()
        }
        tvShare.setOnClickListener {
            startShare(0)
        }
        tvAddTime.setOnClickListener {
            startShare(1)
        }
        tvClose.setOnClickListener {
            gameOver(0)
        }

        rvQuality.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.tvName)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            mAdapter.title = mAdapter.data[position].name
            SpUtils.saveSP(ConstantsUtils.clarityIndex, position)
            hmcpVideoView.onSwitchResolution(0, mAdapter.data[position], 0)
            setMenuShow()
            showToast("清晰度切换成功")

            GameAgentUtils.setYunGameDetail(game_id,gamePackage,5,position)
        }

        //暂停所有任务
        try {

            QuietDownloader.pauseAll()
        }catch (e:Exception){

        }

        val game_yun_time = SpUtils.getInt(R.string.game_yun_time, 0)
        if (game_yun_time < 3) {
            checkPlaying()
        } else {

            basePresenter?.addGameTime(SpUtils.getString(R.string.game_yun_id, "0"), game_yun_time,
                0)
            handler.sendEmptyMessageDelayed(10, 500)

            SpUtils.saveSP(R.string.game_yun_time, 0)
        }
    }

    /**
     * 关闭游戏实例
     */
    private fun checkPlaying() {
        //不能关闭远程tcp连接,只能关闭本地实例
        basePresenter?.getGameTime(game_id)
    }


    private var cToken = ""
    private fun startPlay() {
        GameAgentUtils.setYunGameDetail(game_id,gamePackage,1,0)

        LoadingUtils.getInstance().show(this)
        //设置用户信息
        val userInfo = UserInfo()
        userInfo.userId = SpUtils.getString(R.string.user_id, "0")
        userInfo.userToken = userInfo.userId + userInfo.userId
        hmcpVideoView.setUserInfo(userInfo)
        hmcpVideoView.setConfigInfo("")

        val bundle = Bundle()
        cToken = CryptoUtils.generateCToken(gamePackage, userInfo.userId, userInfo.userToken,
            "e87ec3dac90", SpUtils.getString(R.string.channel, "1"),
            "49d45c71e342f23d5a402c760bd41d3c")
        bundle.putSerializable(HmcpVideoView.ORIENTATION, ScreenOrientation.LANDSCAPE)
        bundle.putInt(HmcpVideoView.PLAY_TIME, surplusTime * 1000)
        bundle.putInt(HmcpVideoView.PRIORITY, Integer.valueOf("0"))
        bundle.putString(HmcpVideoView.APP_NAME, gamePackage)
        bundle.putString(HmcpVideoView.APP_CHANNEL, "hu")
        bundle.putString(HmcpVideoView.C_TOKEN, cToken)
        bundle.putString(HmcpVideoView.EXTRA_ID, "YunGameActivity")
        hmcpVideoView.play(bundle)
    }


    private fun startShare(share: Int) {
        setMenuShow()
        if (gameShare == null) {
            return
        }
        ShareGameDialog.create(supportFragmentManager).apply {
            class_name = "YunGame"
            game_id = game_id
            share_url = gameShare!!.share_url
            share_title = gameShare!!.title
            share_desc = gameShare!!.desc
            share_image = gameShare!!.img
            type = share
        }.setOnItemClickListener(object : ShareGameDialog.OnItemClickListener {
            override fun click(type: Int) {

                GameAgentUtils.setYunGameDetail(game_id, gamePackage, if(share==1) 4 else 3, 0)
                basePresenter?.setShareRelation(game_id, gameShare!!.code)
            }
        }).show()
    }

    override fun onBackPressed() {

    }

    override fun onStart() {
        hmcpVideoView.onStart()
        super.onStart()
    }

    override fun onRestart() {
        hmcpVideoView.onRestart(1000)
        super.onRestart()
    }

    /**
     * 显示时间
     */
    private var timeResume = 0
    /**
     *初始化分辨率
     */
    private var typeClarity = true

    override fun onResume() {
        showView = true
        hmcpVideoView.onResume()
        super.onResume()
        mHandler.sendEmptyMessageDelayed(12, 1000)
        timeResume = surplusTime
    }

    override fun onPause() {
        showView = false
        hmcpVideoView.onPause()
        super.onPause()


        if (surplusTime != 900) {
            //有网,直接上传游戏时长
            if (showTime) {
                basePresenter?.addGameTime(game_id, timeResume - surplusTime, 0)
                SpUtils.saveSP(R.string.game_yun_time, 0)
            } else {
                //断网记录时长,下次打开上传
                SpUtils.saveSP(R.string.game_yun_id, game_id)
                SpUtils.saveSP(R.string.game_yun_time, timeResume - surplusTime)
            }

        }
    }

    override fun onStop() {
        hmcpVideoView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        hmcpVideoView?.onDestroy()
        handler.removeCallbacksAndMessages(null)
        if (surplusTime != 900) {
            basePresenter?.addGameTime(game_id, 0, 1)
        }
        super.onDestroy()
        //恢复游戏下载队列
        QuietDownloader.recoverAll()
    }

    override fun onPlayStatus(p0: Int, p1: Long, p2: String?) {

    }

    override fun onPlayerError(p0: String?, p1: String?) {

    }

    override fun onNetworkChanged(p0: NetWorkState?) {

    }

    override fun onSuccess() {

    }

    override fun HmcpPlayerStatusCallback(data: String) {
        Logger.d(data)
        val obj = JSON.parseObject(data)
        val statusCode = obj.getIntValue(StatusCallbackUtil.STATUS)
        when (statusCode) {
            Constants.STATUS_PLAY_INTERNAL -> {
                hmcpVideoView.play()
                handler.sendEmptyMessage(30)
            }
            Constants.STATUS_START_PLAY -> {
                showTime = true
            }
            Constants.STATUS_OPERATION_HMCP_ERROR -> {
                showToast("加载失败，请稍后再试")
                hmcpVideoView.onExitGame()
                finish()
            }
            Constants.STATUS_FIRST_FRAME_ARRIVAL -> {
                LoadingUtils.getInstance().dismiss()
                if (HmcpManager.getInstance().resolutionDatas.size > 3) {
                    HmcpManager.getInstance().resolutionDatas.removeAt(0)
                }
                if (SpUtils.getInt(ConstantsUtils.clarityIndex, -1) == -1) {
                    SpUtils.saveSP(ConstantsUtils.clarityIndex, 1)
                }
                if (typeClarity) {
                    typeClarity = false
                    mAdapter.setNewInstance(HmcpManager.getInstance().resolutionDatas)
                    val index = SpUtils.getInt(ConstantsUtils.clarityIndex, 1)
                    if (mAdapter.data.size > index) {
                        mAdapter.title = HmcpManager.getInstance().resolutionDatas[index].name
                        hmcpVideoView.onSwitchResolution(0, mAdapter.data[index], 0)
                        mAdapter.notifyDataSetChanged()
                    }
                }

                showTime = true

                handler.sendEmptyMessageDelayed(12, 100)
                // Logger.d(HmcpManager.getInstance().cloudId)

            }
            Constants.STATUS_NETWORK_UNAVAILABLE -> {
                //网络断开
                showTime = false
            }
            Constants.STATUS_OPERATION_GAME_OVER -> {
                //游戏时长到达

            }
            Constants.STATUS_SWITCH_RESOLUTION -> {
                //切换分辨率
            }
        }
    }

    /**
     * 是否在显示状态
     *
     */
    private var showView = false
    /**
     * 时间是否在计时
     */
    private var showTime = false
    private var time = 0L
    private fun setTime() {
        if (!showView) {
            return
        }
        if (System.currentTimeMillis() - time < 1000) {
            return
        }
        time = System.currentTimeMillis()
        if (hmcpVideoView.clockDiffVideoLatencyInfo != null) {
            val netDelay = hmcpVideoView.clockDiffVideoLatencyInfo.netDelay
            tvTime.text = "${netDelay}ms"

            if (netDelay < 100) {
                tvTime.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, R.drawable.oval_6dd400), null, null, null)
            } else if (netDelay < 200) {
                tvTime.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, R.drawable.oval_ffcc03_8), null, null, null)
            } else {
                tvTime.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(this, R.drawable.oval_ff2d2d), null, null, null)
            }
        }
        if (showTime) {
            surplusTime -= 1
        }

        tvTimeOver.text = TimeZoneUtil.formatTimeMS(surplusTime)
        if (surplusTime == 1) {
            gameOver(1)
        } else if (surplusTime < 1) {
            surplusTime = 1
        }
    }

    override fun onInputMessage(p0: String?) {

    }

    override fun onMessage(p0: Message?) {

    }

    override fun onInputDevice(p0: Int, p1: Int) {

    }

    override fun onSceneChanged(p0: String?) {

    }

    override fun onError(p0: ErrorType?, p1: String?) {

    }

    override fun onExitQueue() {

    }


    /**
     * 侧栏目状态
     */
    private var isExpand = true

    /**
     * 设置右侧菜单
     */
    fun setMenuShow() {
        if (isExpand) {
            rl_content.visibility = View.VISIBLE
            llMore.visibility = View.VISIBLE
            vView.visibility = View.VISIBLE
            iv_more.visibility = View.GONE
            rvQuality.visibility = View.GONE
            rl_content.animation = AnimationUtil.moveToViewLocationLeft()
        } else {
            rl_content.animation = AnimationUtil.moveToViewLocationRight()
            rl_content.postDelayed({
                rl_content.visibility = View.GONE
                vView.visibility = View.GONE
                iv_more.visibility = View.VISIBLE
            }, 200)
        }
        isExpand = !isExpand
    }

    override fun onSystemUiVisibilityChange(p0: Int) {
        //保持布局状态
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or 0x00001000
        window.decorView.systemUiVisibility = uiOptions
    }


    /**
     * 结束游戏
     * type 0正常手动退出,1自动结束
     */
    private fun gameOver(type: Int) {
        YunGameDialog.create(supportFragmentManager).apply {
            if (type == 1 && isAssess == 0) {
                messageText = "都玩到这里\n不如写下你的游戏体验？"
                cancelType = 5
                cancelText = "下次点评"
                confirmText = "现在就去"
            } else if (type == 0 && isAssess == 0) {
                messageText = "都玩到这里\n不如写下你的游戏体验？"
                cancelType = 1
                cancelText = "下次点评"
                confirmText = "现在就去"
            } else if (type == 0) {
                messageText = "确定要退出游戏吗?"
                cancelType = 8
            } else {
                messageText = "今日秒玩体验时长已耗尽 明天再来玩吧"
                cancelType = 4
            }
        }.setDismissListener(object : YunGameDialog.DismissListener {
            override fun onDismiss(type: Int) {
                hmcpVideoView.onExitGame()
                if (isAssess == 0 && type == 1) {
                    GameDetailActivity.actionStart(this@YunGameActivity, game_id, 0, "1001")
                    finish()
                } else {
                    finish()
                }
            }
        }).show()
    }

    /**
     * 游戏剩余时间
     */
    private var surplusTime = 900
    private var gameShare: ShareInfo? = null
    /**
     * 是否评价游戏
     */
    private var isAssess = 0

    fun showGameTime(any: YunTimeBean) {
        surplusTime = any.surplusTime
        isAssess = any.assess
        timeResume = surplusTime
        if (any.surplusTime < 30) {
            showToast("您今日秒玩时间已不足，明天再来吧")
            this.finish()
            return
        }
        timeResume = surplusTime
        any.share_info!!.share_url=any.share_info!!.share_url+"?game_id=$game_id"
        gameShare = any.share_info
        startPlay()
    }

    /**
     * 增加游戏时间
     * time 增加的时长
     */
    fun showAddGameTime(time: Int) {

        GameAgentUtils.setYunGameDetail(game_id,gamePackage,4,0)
        surplusTime += time
        val bundle = Bundle()
        bundle.putLong(HmcpVideoView.PLAY_TIME, surplusTime * 1000L)
        bundle.putString(HmcpVideoView.USER_ID,
            SpUtils.getString(R.string.user_id, "0") + System.currentTimeMillis())
        bundle.putString(HmcpVideoView.TIPS_MSG, "")
        bundle.putString(HmcpVideoView.PAY_PROTO_DATA,
            SpUtils.getString(R.string.user_id, "0") + System.currentTimeMillis()) //随机数字
        bundle.putString(HmcpVideoView.C_TOKEN, cToken)

        hmcpVideoView.updateGameUID(bundle, object : OnUpdataGameUIDListener {
            override fun fail(p0: String?) {

            }

            override fun success(p0: Boolean) {

            }
        })
    }
}