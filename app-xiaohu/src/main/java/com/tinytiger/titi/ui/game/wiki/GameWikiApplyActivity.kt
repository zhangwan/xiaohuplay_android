package com.tinytiger.titi.ui.game.wiki

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent

import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.gametools.GameWikiContract
import com.tinytiger.titi.mvp.presenter.gametools.GameWikiPresenter
import kotlinx.android.synthetic.main.game_activity_wiki_apply.*
import kotlinx.android.synthetic.main.game_wiki_item_addform.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 上午 9:57
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 申请百科管理员
 */
class GameWikiApplyActivity : BaseBackActivity(), GameWikiContract.View , RadioGroup.OnCheckedChangeListener{

    override fun ApplyAdmin(msg:String) {
        wiki_type=2
        showToast(msg)
        setFinish()
    }

    override fun showWikiStatusList(bean: WikiStatusList) {

    }

    companion object {
        fun actionStart(context: Activity,type:Int,game_id:String ,game_name:String){
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, GameWikiApplyActivity::class.java)
            intent.putExtra("type",type)
            intent.putExtra("game_id",game_id)
            intent.putExtra("game_name",game_name)
            context.startActivityForResult(intent,99)
        }
    }


    private val mPresenter by lazy { GameWikiPresenter() }


    override fun layoutId(): Int = R.layout.game_activity_wiki_apply

    private var wiki_type=0
    private var game_id=""
    private var gameName=""
    override fun initData() {
        mPresenter.attachView(this)
        setWindowFeature()
        wiki_type = intent.getIntExtra("type", 0)
        game_id = intent.getStringExtra("game_id")
        gameName = intent.getStringExtra("game_name")
    }


    override fun initView() {
        iv_back.setOnClickListener {
            setFinish()
        }

        when(wiki_type){
            -1->{
                llContent.removeView(iView)
                llContent.addView(getEmptyView(wiki_type))
            }
            -2->{
                iView.visibility=View.GONE
                llContent.addView(getEmptyView(wiki_type))
            }
            0->{
                setApply()
            }
            else->{
                llContent.removeView(iView)
                llContent.addView(getEmptyView(wiki_type))
            }
        }
    }

    override fun onBackPressed() {
        setFinish()
    }


    private fun setFinish(){
        val i = Intent()
        i.putExtra("wiki_type", wiki_type)
        setResult(Activity.RESULT_OK, i)

        finish()
    }

    override fun start() {

    }

    override fun showCollectGameWiki(is_collect: Int) {

    }

    override fun showWikiModularList(bean: WikiModularList) {

    }

    override fun showWikiModularOtherList(bean: WikiModularOtherList) {

    }

    override fun showGameWikiDetail(bean: WikiDitailList) {

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


    private fun setApply(){
      //  ll_content.background=ContextCompat.getDrawable(this,R.mipmap.icon_wiki_apply2)
       // ll_content1.background=ContextCompat.getDrawable(this,R.drawable.solid_rectangle_5_ffffff)
        tvGameName.text=gameName

        setBtnType(true)
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
          /*  if (other_game_name.isNullOrEmpty()) {
                showToast("请输入备选游戏")
                return@setOnClickListener
            }*/

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

            mPresenter.addApplyAdmin(game_id, about_game,
                connect_info, has_experience,play_game_time,
                week_time,the_age, office_hours,
                other_game, other_game_name)
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

    private var has_experience = ""
    private var play_game_time = ""
    private var week_time = ""
    private var the_age = ""
    private var office_hours= ""
    private var other_game= ""
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
                other_game= "没有"
            }
            R.id.radiogroup6_se2 -> {
                other_game= "有1-2个备选"
            }
            R.id.radiogroup6_se3 -> {
                other_game= "很多游戏我都可以"
            }
        }
    }

    fun getEmptyView(type: Int): View {
        val view = layoutInflater.inflate(
            com.tinytiger.common.R.layout.view_empty_wiki,
            llContent.parent as ViewGroup,
            false
        )
        val empty_view_tv = view.findViewById<TextView>(com.tinytiger.common.R.id.empty_view_tv)
        val tvAddGame = view.findViewById<TextView>(com.tinytiger.common.R.id.tvAddGame)
        val btn_complete = view.findViewById<TextView>(com.tinytiger.common.R.id.btn_complete)
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.white))

        when(type){
            -1->{
                empty_view_tv.text = "审核失败"
                empty_view_tv.setTextColor(ContextCompat.getColor(this, R.color.color_red_ccfa3c55))
                tvAddGame.text = ""
            }
            -2->{
                empty_view_tv.text = "审核失败"
                empty_view_tv.setTextColor(ContextCompat.getColor(this, R.color.color_red_ccfa3c55))
                tvAddGame.text = ""
                tvAddGame.text = "重新申请"
                btn_complete.visibility=View.VISIBLE
                btn_complete.setOnClickListener {
                    iView.visibility=View.VISIBLE
                    llContent.removeView(view)
                    setApply()
                }
            }
            2->{
                empty_view_tv.text = "正在审核中…"
                empty_view_tv.setTextColor(ContextCompat.getColor(this, R.color.gray33))
                tvAddGame.text = ""
            }
            1->{
                empty_view_tv.text = "恭喜您通过了管理员审核！"
                empty_view_tv.setTextColor(ContextCompat.getColor(this, R.color.color_0fb50a))
                tvAddGame.text = "管理人员将会在1~3个工作日内联系您！"
                tvAddGame.setTextColor(ContextCompat.getColor(this, R.color.gray33))
            }
        }
        return view
    }
}