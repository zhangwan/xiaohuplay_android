package com.tinytiger.titi.ui.game.info

import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.orhanobut.logger.Logger
import com.tinytiger.common.base.BaseFragment
import com.tinytiger.common.net.data.gametools.GameInfoBean

import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.gametools.GameWikiContract
import com.tinytiger.titi.mvp.presenter.gametools.GameWikiPresenter
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.game.listener.OnGameStatusListener

import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.game_fragment_addwiki.*
import kotlinx.android.synthetic.main.game_wiki_item_addform.*
import kotlinx.android.synthetic.main.my_game_item_title.*


/**
 *
 * @author zhw_luke
 * @date 2020/3/27 0027 下午 2:45
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 游戏详情 Fragment 招募百科页
 */
class GameAddWikiFragment : BaseFragment(), GameWikiContract.View,
    RadioGroup.OnCheckedChangeListener {
    var name=""
    var gameId=""
    var logo=""
    fun showGameFollow(is_follow: Int) {

    }

    override fun ApplyAdmin(msg: String) {
        llContent.removeView(fl_head1)
        llContent.removeView(fl_head2)

        llContent.addView(getEmptyView(2))
        (activity as CirclesDetailsActivity).wiki_type = 2
    }

    override fun showWikiStatusList(bean: WikiStatusList) {

    }

    override fun showCollectGameWiki(is_collect: Int) {

    }


    companion object {

        fun getInstance(game_id: String,logo:String,name:String): GameAddWikiFragment {
            val fragment = GameAddWikiFragment()
            val bundle = Bundle()
            fragment.gameId = game_id
            fragment.logo=logo
            fragment.name=name
            fragment.arguments = bundle
            return fragment
        }

    }

    private val mPresenter by lazy { GameWikiPresenter() }


    override fun getLayoutId(): Int = R.layout.game_fragment_addwiki


    /**
     * 设置游戏信息
     */
    fun setGameInfo() {
        if(!TextUtils.isEmpty(logo.trim())){
            GlideUtil.loadImg(iv_avatar, logo)
        }else{
            iv_avatar.setBackgroundResource(R.mipmap.icon_error_down3)
        }

        tv_name.text = name
        this.gameId=gameId
        // mHandler.postDelayed({ setRun(game.background) }, 1200)
        val wiki_type = (activity as CirclesDetailsActivity).wiki_type

        when (wiki_type) {
            -1 -> {
                llContent.removeView(fl_head1)
                llContent.removeView(fl_head2)
                llContent.addView(getEmptyView(wiki_type))
            }
            -2 -> {
                fl_head1.visibility = View.GONE
                fl_head2.visibility = View.GONE
                llContent.addView(getEmptyView(wiki_type))
            }
            0 -> {
                addApply()
            }
            else -> {
                llContent.removeView(fl_head1)
                llContent.removeView(fl_head2)
                llContent.addView(getEmptyView(wiki_type))
            }
        }
    }


    override fun initView() {
        mPresenter.attachView(this)

        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener
        { _, _, scrollY, _, _ -> iv_top.alpha = scrollY / topHeight })
        fl_head1.visibility = View.GONE
        fl_head2.visibility = View.GONE
        setGameInfo()
    }

    private var topHeight = 300.0f

    override fun start() {
    }

    override fun showWikiModularList(bean: WikiModularList) {

    }

    override fun showWikiModularOtherList(bean: WikiModularOtherList) {

    }

    override fun showGameWikiDetail(bean: WikiDitailList) {

    }

    private fun addApply() {
        fl_head1.visibility = View.VISIBLE
        fl_head2.visibility = View.VISIBLE
        setBtnType(true)
        tvGameName.text =name
        btn_complete.setOnClickListener {

            val about_game = et_text1.text.trim().toString()
            if (about_game.isNullOrEmpty()) {
                showToast("请输入对该游戏认识")
                return@setOnClickListener
            }
            val connect_info = et_text2.text.trim().toString()
            if (connect_info.isNullOrEmpty()) {
                showToast("请输入联系方式")
                return@setOnClickListener
            }

            val other_game_name = et_text3.text.trim().toString()

            if (play_game_time.isNullOrEmpty()) {
                showToast("请选择游戏时长")
                return@setOnClickListener
            }
            if (has_experience.isNullOrEmpty()) {
                showToast("请选择是否有类似编辑")
                return@setOnClickListener
            }

            if (week_time.isNullOrEmpty()) {
                showToast("请选择百科投入时间")
                return@setOnClickListener
            }
            if (office_hours.isNullOrEmpty()) {
                showToast("请选择开始时间")
                return@setOnClickListener
            }
            if (the_age.isNullOrEmpty()) {
                showToast("请选择年龄")
                return@setOnClickListener
            }
            if (other_game.isNullOrEmpty()) {
                showToast("请选择备选游戏")
                return@setOnClickListener
            }
            mPresenter.addApplyAdmin(gameId, about_game, connect_info, has_experience,
                play_game_time, week_time, the_age, office_hours, other_game, other_game_name)
        }


        radiogroup1.setOnCheckedChangeListener(this)
        radiogroup2.setOnCheckedChangeListener(this)
        radiogroup3.setOnCheckedChangeListener(this)
        radiogroup4.setOnCheckedChangeListener(this)
        radiogroup5.setOnCheckedChangeListener(this)
        radiogroup6.setOnCheckedChangeListener(this)
    }

    private fun setBtnType(type: Boolean) {
        btn_complete.isClickable = type
        btn_complete.isEnabled = type
        btn_complete.isSelected = type
        btn_complete.isPressed = type
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    fun setBlurryBG(bit: Bitmap) {
        Blurry.with(context).radius(10).sampling(8).async().from(bit).into(iv_top)
    }

    private var has_experience = ""
    private var play_game_time = ""
    private var week_time = ""
    private var the_age = ""
    private var office_hours = ""
    private var other_game = ""
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.radiogroup1_se1 -> {
                play_game_time = "2小时内"
            }
            R.id.radiogroup1_se2 -> {
                play_game_time = "2-20小时"
            }
            R.id.radiogroup1_se3 -> {
                play_game_time = "20小时以上"
            }
            R.id.radiogroup2_se1 -> {
                has_experience = "完全没有"
            }
            R.id.radiogroup2_se2 -> {
                has_experience = "有一些经验"
            }
            R.id.radiogroup2_se3 -> {
                has_experience = "参与过且比较熟悉"
            }
            R.id.radiogroup3_se1 -> {
                week_time = "<7小时"
            }
            R.id.radiogroup3_se2 -> {
                week_time = "7-14小时"
            }
            R.id.radiogroup3_se3 -> {
                week_time = ">14小时"
            }
            R.id.radiogroup4_se1 -> {
                office_hours = "随时可以"
            }
            R.id.radiogroup4_se2 -> {
                office_hours = "三天内"
            }
            R.id.radiogroup4_se3 -> {
                office_hours = "一周左右时间再议"
            }
            R.id.radiogroup5_se1 -> {
                the_age = "18岁以下"
            }
            R.id.radiogroup5_se2 -> {
                the_age = "18-26岁"
            }
            R.id.radiogroup5_se3 -> {
                the_age = "26岁以上"
            }
            R.id.radiogroup6_se1 -> {
                other_game = "没有"
            }
            R.id.radiogroup6_se2 -> {
                other_game = "有1-2个备选"
            }
            R.id.radiogroup6_se3 -> {
                other_game = "很多游戏我都可以"
            }
        }
    }

    fun getEmptyView(type: Int): View {
        val view = activity!!.layoutInflater.inflate(com.tinytiger.common.R.layout.view_empty_wiki,
            llContent.parent as ViewGroup, false)
        val empty_view_tv = view.findViewById<TextView>(com.tinytiger.common.R.id.empty_view_tv)
        val tvAddGame = view.findViewById<TextView>(com.tinytiger.common.R.id.tvAddGame)
        val btn_complete = view.findViewById<TextView>(com.tinytiger.common.R.id.btn_complete)
        view.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.white))

        when (type) {
            -1 -> {
                empty_view_tv.text = "审核失败"
                empty_view_tv.setTextColor(
                    ContextCompat.getColor(activity!!, R.color.color_red_ccfa3c55))
                tvAddGame.text = ""
            }
            -2 -> {
                empty_view_tv.text = "审核失败"
                empty_view_tv.setTextColor(
                    ContextCompat.getColor(activity!!, R.color.color_red_ccfa3c55))
                tvAddGame.text = "重新申请"
                btn_complete.visibility = View.VISIBLE
                btn_complete.setOnClickListener {
                    fl_head1.visibility = View.VISIBLE
                    fl_head2.visibility = View.VISIBLE
                    llContent.removeView(view)

                    btn_complete
                    addApply()
                }
            }
            2 -> {
                empty_view_tv.text = "正在审核中…"
                empty_view_tv.setTextColor(ContextCompat.getColor(activity!!, R.color.gray33))
                tvAddGame.text = ""
            }
            1 -> {
                empty_view_tv.text = "恭喜您通过了管理员审核！"
                empty_view_tv.setTextColor(ContextCompat.getColor(activity!!, R.color.color_0fb50a))
                tvAddGame.text = "管理人员将会在1~3个工作日内联系您！"
                tvAddGame.setTextColor(ContextCompat.getColor(activity!!, R.color.gray33))
            }
        }
        return view
    }
}