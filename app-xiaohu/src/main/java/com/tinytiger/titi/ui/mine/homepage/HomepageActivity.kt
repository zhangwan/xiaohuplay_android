package com.tinytiger.titi.ui.mine.homepage


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.netease.nim.uikit.common.UserUtil
import com.tinytiger.common.adapter.TabAdapter
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.UserStatusEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.MyContentListBean
import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.CropImageActivity
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.Indicator
import com.tinytiger.common.view.text.ScaleTransitionPagerTitleView
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.mine.HomepageContract
import com.tinytiger.titi.mvp.presenter.mine.HomepagePresenter
import com.tinytiger.titi.ui.mine.me.fans.FriendActivity
import com.tinytiger.titi.ui.mine.me.fans.LookAttentionActivity
import com.tinytiger.titi.ui.mine.me.fans.LookFansActivity
import com.tinytiger.titi.ui.mine.other.MineMeritActivity
import com.tinytiger.titi.ui.mine.setting.user.SettingUserInfoActivity
import com.tinytiger.titi.ui.msg.LikeActivity
import com.tinytiger.titi.utils.CommonUtils
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.utils.net.NetworkUtil
import com.tinytiger.titi.widget.AppBarStateChangeListener
import com.tinytiger.titi.widget.popup.UserPopup
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.layout_home_title.*
import kotlinx.android.synthetic.main.mine_activity_homepage2.*
import kotlinx.android.synthetic.main.mine_include_homepage.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import java.util.*


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 用户主页
*/
class HomepageActivity : BaseBackActivity(), HomepageContract.View {

    override fun showBlackStatus(is_black: Int) {
        mUserCenterBean!!.userinfo.is_black = is_black
        if (is_black == 1) {
            EventBus.getDefault().post(UserStatusEvent(user_id, 1))
            UserUtil.addFromBlack(mUserCenterBean!!.userinfo.netease_id)
            showToast("拉黑成功")
        } else {
            EventBus.getDefault().post(UserStatusEvent(user_id, -1))
            UserUtil.removeFromBlack(mUserCenterBean!!.userinfo.netease_id)
            showToast("取消拉黑成功")
        }

        mDemoPopup = null
        refreshLetter()
//        showMutual(mUserCenterBean!!.is_mutual)
        showMutual(-1)
    }


    private var user_id = ""
    private var qiniutoken = ""
    private var isMyself = false


    companion object {

        private const val EXTRA_USER_ID = "user_id"
        fun actionStart(context: Context, user_id: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, HomepageActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, user_id)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { HomepagePresenter() }

    override fun layoutId(): Int = R.layout.mine_activity_homepage2

    override fun initData() {
        useStatusBarColor = false
        setWindowFeature()
        mPresenter.attachView(this)

        if (intent.hasExtra(EXTRA_USER_ID)) {
            user_id = intent.getStringExtra(EXTRA_USER_ID)
        }
        isMyself = MyUserData.getUserId() == user_id
    }

    private var mDemoPopup: UserPopup? = null
    override fun initView() {
        if (isMyself) {
            tv_change_background.visibility = View.VISIBLE
            tv_edit_info.visibility = View.VISIBLE
            tv_attention.visibility = View.INVISIBLE
            tvTitleAttention.visibility = View.INVISIBLE
            tv_private_letter.visibility = View.INVISIBLE
        } else {
            tv_change_background.visibility = View.INVISIBLE
            tv_edit_info.visibility = View.INVISIBLE
            tv_attention.visibility = View.VISIBLE
            tvTitleAttention.visibility = View.VISIBLE
            tv_private_letter.visibility = View.VISIBLE
        }

        val dp210 = Dp2PxUtils.dip2px(this, 130)
        val dp80 = Dp2PxUtils.dip2px(this, 80)
        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onOffsetChanged(appBarLayout: AppBarLayout) {
                iv_top.alpha = -(dp80 + appBarLayout.top) / dp80.toFloat()
                if (appBarLayout.bottom < 200) {
                    useStatusBarColor = true
                    ll_title.visibility = View.VISIBLE
                    ll_top.visibility = View.INVISIBLE
                    ll_top2.visibility = View.VISIBLE
                    if (isMyself) {
                        ivShare.setImageResource(R.mipmap.game_icon_share_black)
                    } else {
                        ivShare.setImageResource(R.mipmap.icon_more_b)
                    }
                } else if (appBarLayout.bottom < appBarLayout.height - dp210) {
                    useStatusBarColor = true
                    ll_title.visibility = View.VISIBLE
                    ll_top.visibility = View.VISIBLE
                    ll_top2.visibility = View.GONE

                    iv_back!!.setImageResource(R.mipmap.icon_back)
                    if (isMyself) {
                        ivShare.setImageResource(R.mipmap.game_icon_share_black)
                    } else {
                        ivShare.setImageResource(R.mipmap.icon_more_b)
                    }
                } else {
                    useStatusBarColor = false
                    ll_title.visibility = View.GONE
                    ll_top.visibility = View.VISIBLE
                    ll_top2.visibility = View.GONE

                    iv_back!!.setImageResource(R.mipmap.icon_back_white)
                    if (isMyself) {
                        ivShare.setImageResource(R.mipmap.game_icon_share)
                    } else {
                        ivShare.setImageResource(R.mipmap.icon_more_w)
                    }
                }
                setStatusBar()
            }

            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state == State.COLLAPSED) {
                    ll_title.visibility = View.VISIBLE
                } else {
                    ll_title.visibility = View.GONE
                }
            }
        })


        iv_back.setOnClickListener {
            finish()
        }

        ivShare.setOnClickListener {
            clickShareMore()
        }

        tv_change_background.setOnClickListener {
            if (NetworkUtil.getNetworkType(this) == NetworkType.NETWORK_NO) {
                showToast(R.string.error_network)
                return@setOnClickListener
            }
            if (qiniutoken.isEmpty()) {
                mPresenter.loadQiniuToken()
            }
            AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .rationale(DefaultRationale())
                .onGranted {
                    CropImageActivity.actionStart(this, 1, 44)
                }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                        PermissionSetting().showSettingDialog(this@HomepageActivity, permissions)
                    }
                }.start()
        }
        tv_edit_info.setOnClickListener {
            refreshType = true
            SettingUserInfoActivity.actionStart(this)
        }

        ll_like.setOnClickListener {
            if (isMyself) {
                refreshType = true
                LikeActivity().actionStart(this)
            }
        }

        ll_follow.setOnClickListener {
            if (isMyself) {
                refreshType = true
                FriendActivity.actionStart(this, 0)
            } else {
                LookAttentionActivity().actionStart(this, user_id)
            }
        }
        ll_fans.setOnClickListener {
            if (isMyself) {
                refreshType = true
                FriendActivity.actionStart(this, 1)
            } else {
                if (MyUserData.isEmptyToken()) {
                    EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    return@setOnClickListener
                }
                LookFansActivity().actionStart(this, user_id)
            }

        }

        tvTitleAttention.setOnClickListener {
            clickAttention()
        }

        tv_attention.setOnClickListener {
            clickAttention()
        }

        start()
        //禁止拖动
        appbar.post {
            val params: CoordinatorLayout.LayoutParams =
                appbar.getLayoutParams() as (CoordinatorLayout.LayoutParams)
            val behavior: AppBarLayout.Behavior = params.getBehavior() as (AppBarLayout.Behavior)
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(p0: AppBarLayout): Boolean {
                    return false
                }
            })
        }

        initMagicIndicator(magic_indicator)
        initMagicIndicator(magic_indicator2)
    }

    private fun clickShareMore() {
        if (isMyself) {
            showShare()
        } else {
            if (mDemoPopup == null) {
                mDemoPopup = UserPopup(this)
                mDemoPopup!!.setShowAnimation(
                    AmintionUtils().createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f)
                ).setDismissAnimation(
                    AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f)
                )
                mDemoPopup!!.setPopupGravity(Gravity.BOTTOM)
            }
            if (mUserCenterBean == null) {
                return
            }

            val msg = if (mUserCenterBean!!.userinfo.is_black == 0) "拉黑" else "取消拉黑"
            mDemoPopup!!.setBack(msg, View.OnClickListener {
                mDemoPopup!!.dismiss()
                if (mUserCenterBean != null) {
                    if (mUserCenterBean!!.userinfo.is_black == 0) {
                        showBlackDialog()
                    } else {
                        mPresenter.addBlack(1, user_id)
                    }
                }

            })
            mDemoPopup!!.setShare(View.OnClickListener {
                mDemoPopup!!.dismiss()
                showShare()
            })

            val location = IntArray(2)
            ivShare.getLocationInWindow(location)
            ivShare.getLocationOnScreen(location)

            mDemoPopup!!.setBackground(null)
                .setBlurBackgroundEnable(false)
                .showPopupWindow(
                    location[0] - Dp2PxUtils.dip2px(this, 70),
                    location[1] + Dp2PxUtils.dip2px(this, 30)
                )
        }
    }

    /**
     * 点击关注按钮
     */
    private fun clickAttention() {
        if (MyUserData.isEmptyToken()) {
            mUserCenterBean!!.is_mutual = -1
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
        if (NetworkUtil.getNetworkType(this) == NetworkType.NETWORK_NO) {
            showToast(R.string.error_network)
            return
        }
        if (mUserCenterBean?.userinfo?.is_black == 1) {
            mPresenter.addBlack(1, user_id)
            return
        }


        if (mUserCenterBean!!.is_mutual == -1) {
            mPresenter.doFollow(mUserCenterBean!!.is_mutual, user_id)
        } else {
            TextDialog.create(supportFragmentManager)
                .setMessage("确认要取消关注吗?")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        mPresenter.doFollow(mUserCenterBean!!.is_mutual, user_id)
                    }
                }).show()
        }
    }


    override fun start() {
        mPresenter.getHomepageInfo(user_id)
        mPresenter.loadQiniuToken()
    }


    private fun showBlackDialog() {
        TextDialog.create(supportFragmentManager)
            .setMessage("确定要拉黑此用户吗?")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {
                    mPresenter.addBlack(0, user_id)
                }
            }).show()
    }

    private fun showShare() {
        if (mUserCenterBean != null) {
            val url = mUserCenterBean!!.share_url + "?user_id=" + user_id
            ShareDialog.create(supportFragmentManager)
                .apply {
                    class_name = "no"
                    share_url = url
                    share_title = mUserCenterBean!!.userinfo.nickname
                    share_desc = mUserCenterBean!!.userinfo.nickname
                    share_image = mUserCenterBean!!.userinfo.avatar
                    userId = user_id
                    collectionType = 0
                    report_type = 3
                }
                .show()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }


    private fun initMagicIndicator(magic_indicator: MagicIndicator) {
        val titles = arrayOf("动态", "  游戏")
        val mFragmentList = ArrayList<Fragment>()
        mFragmentList.add(HomeDynamicFragment.getInstance(user_id))
        mFragmentList.add(HomeGameListFragment.getInstance(user_id))


        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = false
        val mAdapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val titleView = ScaleTransitionPagerTitleView(context)
                titleView.text = (titles[index])
                titleView.textSize = 16f
                titleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                titleView.normalColor = ContextCompat.getColor(context, R.color.color_b3b3b3)
                titleView.selectedColor = ContextCompat.getColor(context, R.color.gray33)
                titleView.minScale = 0.8.toFloat()
                titleView.setOnClickListener {
                    mViewPager.currentItem = index
                }
                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                if (titles.size > 1) {
                    return Indicator.mLinePagerIndicator(context, R.color.color_ffcc03, 10)
                } else {
                    return null
                }
            }
        }

        commonNavigator.adapter = mAdapter
        magic_indicator.navigator = commonNavigator

        mViewPager.adapter = TabAdapter(supportFragmentManager, mFragmentList)
        ViewPagerHelper.bind(magic_indicator, mViewPager)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setSwipeBackEnable(position == 0)
            }
        })

    }

    override fun showMutual(isMutual: Int) {
        if (MyUserData.isEmptyToken()) {
            return
        }
        //如果是拉黑状态，不显示关注
        if (mUserCenterBean?.userinfo?.is_black == 1) {
            tv_attention.isSelected = false
            tv_attention.text = "解除拉黑"
            tv_attention.setTextColor(Color.parseColor("#333333"))
            tv_attention.setBackgroundResource(R.drawable.solid_rectangle_16_ffcc03_linear)
            tvTitleAttention.isSelected = false
            tvTitleAttention.text = "解除拉黑"
            tvTitleAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
            tvTitleAttention.setBackgroundResource(R.drawable.stroke_rectangle_20_aaaaaa)
            return
        }

        mUserCenterBean!!.is_mutual = isMutual
        when (isMutual) {
            0 -> {
                tv_attention.isSelected = true
                tv_attention.text = "已关注"
                tv_attention.setTextColor(Color.WHITE)
                tv_attention.setBackgroundResource(R.drawable.solid_rectangle_16_b3b3b3)
                tvTitleAttention.isSelected = true
                tvTitleAttention.text = "已关注"
                tvTitleAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
                tvTitleAttention.setBackgroundResource(R.drawable.stroke_rectangle_20_aaaaaa)
            }
            1 -> {
                tv_attention.isSelected = true
                tv_attention.text = "互相关注"
                tv_attention.setTextColor(Color.WHITE)
                tv_attention.setBackgroundResource(R.drawable.solid_rectangle_16_b3b3b3)
                tvTitleAttention.isSelected = true
                tvTitleAttention.text = "互相关注"
                tvTitleAttention.setTextColor(ContextCompat.getColor(this, R.color.grayAA))
                tvTitleAttention.setBackgroundResource(R.drawable.stroke_rectangle_20_aaaaaa)
            }
            else -> {
                tv_attention.isSelected = false
                tv_attention.text = "关注"
                tv_attention.setTextColor(Color.parseColor("#333333"))
                tv_attention.setBackgroundResource(R.drawable.solid_rectangle_16_ffcc03_linear)
                tvTitleAttention.isSelected = false
                tvTitleAttention.text = "+ 关注"
                tvTitleAttention.setTextColor(Color.parseColor("#ffcc03"))
                tvTitleAttention.setBackgroundResource(R.drawable.stroke_rectangle_16_ffcc03)
            }
        }
    }

    /**
     * 设置头部区域内容
     */
    private fun setRefreshHeader(bean: UserCenterBean.DataBean) {
        avUserTitle.setAvatar(bean.userinfo.avatar, bean.userinfo.master_type)
        tvNameTitle.setNickname(bean.userinfo.nickname)
        showMutual(bean.is_mutual)
    }


    override fun onResume() {
        super.onResume()
        if (refreshType) {
            refreshType = false
            mPresenter.getHomepageInfo(user_id)
        }
    }

    private var refreshType = false

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        if (mDemoPopup != null) {
            mDemoPopup!!.dismiss()
            mDemoPopup = null
        }
    }

    override fun showContentList(bean: MyContentListBean.DataBean) {
    }


    override fun showResult() {
        showToast("设置成功")

        if (outputFile.isNotEmpty()) {
            GlideUtil.loadImg(backdrop, outputFile)
        }
    }

    var mUserCenterBean: UserCenterBean.DataBean? = null

    override fun showHomepageInfo(bean: UserCenterBean.DataBean) {
        mUserCenterBean = bean
        setRefreshHeader(bean)
        tv_like_num.text = StringUtils.sizeToString(bean.like_num)
        tv_follow_num.text = StringUtils.sizeToString(bean.follow_num)
        tv_fans_num.text = StringUtils.sizeToString(bean.fans_num)

        if (bean.userinfo.background_img.isNotEmpty()) {
            GlideUtil.loadImg(backdrop, bean.userinfo.background_img)
        } else {
            backdrop.setBackgroundResource(R.mipmap.mine_icon_bg)
        }
        avUser.setAvatar(bean.userinfo.avatar, bean.userinfo.master_type)

        tv_nickname.text = bean.userinfo.nickname
        tv_desc.text = bean.userinfo.resume
        tv_talent_intro.text = bean.userinfo.master_profile
        if (bean.userinfo.master_profile != null && bean.userinfo.master_profile.isNotEmpty()) {
            when (bean.userinfo.master_type) {
                1 -> {
                    iv_talent_intro.visibility = View.VISIBLE
                    iv_talent_intro.setImageResource(R.mipmap.talent_icon_logo_on)
                }
                2 -> {
                    iv_talent_intro.visibility = View.VISIBLE
                    iv_talent_intro.setImageResource(R.mipmap.talent_icon_logo_in)
                }
                else -> {
                    iv_talent_intro.visibility = View.GONE
                }
            }
        }

        val str = StringBuilder()
        if (bean.userinfo.gender != 0) {
            val gender = if (bean.userinfo.gender == 1) "男" else "女"
            str.append(gender)
        }
        if (!bean.userinfo.constellation.isNullOrEmpty()) {
            if (str.isNotEmpty()) {
                str.append(" / ")
            }
            str.append(bean.userinfo.constellation)
        }
        if (str.isNotEmpty()) {
            str.append(" / ")
        }
        if (!bean.userinfo.provcn.isNullOrEmpty()) {
            if (CommonUtils.isLimitedCity(bean.userinfo.provcn)) {
                str.append(bean.userinfo.provcn)
            } else {
                str.append(bean.userinfo.provcn).append(" ").append(bean.userinfo.citycn)
            }

        } else {
            str.append("你的心里")
        }
        tv_info.text = str.toString()

        if (bean.userinfo.medal_image != null && bean.userinfo.medal_image.isNotEmpty()) {
            iv_medal_img.visibility = View.VISIBLE
            GlideUtil.loadImg(iv_medal_img, bean.userinfo.medal_image)
            iv_medal_img.setOnClickListener {
                MineMeritActivity.actionStart(this, bean.userinfo.medal_id)
            }
        } else {
            iv_medal_img.visibility = View.VISIBLE
        }

        showMutual(bean.is_mutual)

        refreshLetter()
    }


    private fun refreshLetter() {
        if (mUserCenterBean!!.userinfo.is_black != 1 && mUserCenterBean!!.userinfo.is_letter == 1 && mUserCenterBean!!.userinfo.netease_id != null) {
            tv_private_letter.isClickable = true
            tv_private_letter.setBackgroundResource(R.drawable.solid_rectangle_16_ffcc03_linear)
            tv_private_letter.setOnClickListener {
                if (MyUserData.isEmptyToken()) {
                    EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    return@setOnClickListener
                }
                refreshType = true
                EventBus.getDefault()
                    .post(ClassEvent("startP2PSession", mUserCenterBean!!.userinfo.netease_id))
            }

        } else {
            tv_private_letter.isClickable = false
            tv_private_letter.setBackgroundResource(R.drawable.solid_rectangle_16_b3b3b3)
        }
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
        if (mErrorView != null) {
            fl_content.removeView(mErrorView)
            mErrorView = null
        }
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            if (mUserCenterBean != null) {
                return
            }
            if (mErrorView == null) {
                mErrorView = getErrorLayout(true)
            }
            fl_content.addView(
                mErrorView,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
            )

        } else if (errorCode == 4000) {
            //用户被注销
            finish()
        } else {
            dismissLoading()
        }
    }


    override fun getQiniuToken(qiniu: String) {
        this.qiniutoken = qiniu
    }

    private var outputFile = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 44) {
            outputFile = data!!.getStringExtra("path")
            QiNiuUtil.uploadImages(qiniutoken, listOf(outputFile)) { result ->
                if (result != null) {
                    mPresenter.changeBackgroundImg(result)
                }
            }
        }
    }

}