package com.tinytiger.titi.ui.mine.talent


import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.talent.ConditionBean
import com.tinytiger.common.net.data.mine.talent.TalentBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.mvp.contract.mine.TalentContract

import com.tinytiger.titi.mvp.presenter.mine.TalentPresenter
import kotlinx.android.synthetic.main.login_activity_reset_password.*
import kotlinx.android.synthetic.main.mine_activity_opinion.title_view
import kotlinx.android.synthetic.main.talent_activity_info.*
import kotlinx.android.synthetic.main.talent_activity_info.btn_complete


/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:44
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 达人认证_内部认证
 */
class TalentCertificationActivity : BaseBackActivity(), TalentContract.View {


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun getQiniuToken(qiniuTocken: String) {

    }

    override fun showResult(bean: TalentBean?) {

    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    companion object {
        fun actionStart(context: Context, key: ConditionBean) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, TalentCertificationActivity::class.java)
            intent.putExtra("key", key)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.talent_activity_info
    private val mPresenter by lazy { TalentPresenter() }

    init {
        mPresenter.attachView(this)
    }

    private var mConditionBean: ConditionBean? = null
    override fun initData() {
        setWindowFeature()
        mConditionBean = intent.getSerializableExtra("key") as ConditionBean
    }


    override fun initView() {
        iv_back.setOnClickListener { finish() }
        setBtnType(false)
        if (mConditionBean!=null){
            if (mConditionBean!!.works==1){
                ivCondition1.setImageResource(R.mipmap.talent_icon_sel)
            }else{
                ivCondition1.setImageResource(R.mipmap.talent_icon_nor)
            }
            if (mConditionBean!!.get_like==1){
                ivCondition2.setImageResource(R.mipmap.talent_icon_sel)
            }else{
                ivCondition2.setImageResource(R.mipmap.talent_icon_nor)
            }
            if (mConditionBean!!.follow_num==1){
                ivCondition3.setImageResource(R.mipmap.talent_icon_sel)
            }else{
                ivCondition3.setImageResource(R.mipmap.talent_icon_nor)
            }
            if (mConditionBean!!.cond_day==1){
                ivCondition4.setImageResource(R.mipmap.talent_icon_sel)
            }else{
                ivCondition4.setImageResource(R.mipmap.talent_icon_nor)
            }

            if (mConditionBean!!.works+mConditionBean!!.get_like+mConditionBean!!.follow_num+mConditionBean!!.cond_day==4){
                setBtnType(true)
            }else{
               // setBtnType(false)
            }

        }
    }


    override fun start() {

    }

    private fun setBtnType(type: Boolean) {
        if (type){
            btn_complete.setOnClickListener {
                TalentCertification3Activity.actionStart(this,mConditionBean!!.key)
            }
            btn_complete.background=ContextCompat.getDrawable(this,R.drawable.solid_gradient_20_ffcc03)

        }


        btn_complete.isClickable = type
        btn_complete.isEnabled = type
        btn_complete.isSelected = type
        btn_complete.isPressed = type
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


}
