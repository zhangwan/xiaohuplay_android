package com.tinytiger.titi.ui.mine.talent


import android.content.Context
import android.content.Intent
import android.view.View
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.talent.ConditionBean
import com.tinytiger.common.net.data.mine.talent.TalentBean
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.mine.TalentContract
import com.tinytiger.titi.mvp.presenter.mine.TalentPresenter
import kotlinx.android.synthetic.main.talent_activity.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:44
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 达人认证
 */
class TalentActivity : BaseBackActivity(), TalentContract.View {


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun getQiniuToken(qiniuTocken: String) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }


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
            val intent = Intent(context, TalentActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.talent_activity
    private val mPresenter by lazy { TalentPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }

    var key = ""
    override fun initView() {
        title_view.setBackOnClick { finish() }
        rlContent1.setOnClickListener {
            if (mConditionBean != null) {
                TalentCertificationActivity.actionStart(this, mConditionBean!!)
            }
        }
        rlContent2.setOnClickListener {

            TalentCertification2Activity.actionStart(this, key)
        }
        btn_complete.setOnClickListener {
            setViewType(-1, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    override fun start() {
        mPresenter.masterApply()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
     }

    var mConditionBean: ConditionBean? = null
    override fun showResult(bean: TalentBean?) {
        if (bean!!.data == null) {
            setViewType(-1, 0)
            return
        }

        if (bean.data.examine_info != null) {
            setViewType(bean.data.examine_info.status, bean.data.examine_info.apply_type)
        } else {
            setViewType(-1, 0)
        }
        if (bean.data.condition != null) {
            key = bean.data.condition.key
            mConditionBean = bean.data.condition
        }
    }

    private fun setViewType(type: Int, apply_type: Int) {
        title_view.centerTxt = ""
        //# 申请达人状态  0 审核中 1 已通过  2拒绝
        ivEmpty.visibility = View.GONE
        when (type) {
            0 -> {
                //审核中
                llApplication.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
                llInfo1.visibility = View.VISIBLE
                llInfo2.visibility = View.GONE
                llInfo3.visibility = View.GONE
                ivEmpty.visibility = View.VISIBLE
            }
            1 -> {
                if (apply_type == 1) {
                    //审核通过,站内达人
                    llApplication.visibility = View.GONE
                    empty_view.visibility = View.VISIBLE
                    llInfo1.visibility = View.GONE
                    llInfo2.visibility = View.VISIBLE
                    llInfo3.visibility = View.GONE
                    ivTalentName.text = "站内达人"
                    ivTalent.setImageResource(R.mipmap.icon_talent_p1)
                } else {
                    //审核通过,站外达人
                    llApplication.visibility = View.GONE
                    empty_view.visibility = View.VISIBLE
                    llInfo1.visibility = View.GONE
                    llInfo2.visibility = View.VISIBLE
                    llInfo3.visibility = View.GONE
                    ivTalentName.text = "站外达人"
                    ivTalent.setImageResource(R.mipmap.icon_talent_p2)
                }
                ivEmpty.visibility = View.VISIBLE
                ivEmpty.setImageResource(R.mipmap.icon_empty2)
            }
            2 -> {
                //审核未通过,重新申请
                llApplication.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
                llInfo1.visibility = View.GONE
                llInfo2.visibility = View.GONE
                llInfo3.visibility = View.VISIBLE
                ivEmpty.visibility = View.VISIBLE
            }
            else -> {
                title_view.centerTxt = "达人认证"
                //未提交审核
                llApplication.visibility = View.VISIBLE
                empty_view.visibility = View.GONE
            }
        }
    }

}
