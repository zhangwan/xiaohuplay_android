package com.tinytiger.titi.ui.login

import android.content.Context
import android.content.Intent
import androidx.core.widget.addTextChangedListener
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.login.LoginContract
import com.tinytiger.titi.mvp.presenter.LoginPresenter
import com.tinytiger.titi.utils.StringFormatUtil
import kotlinx.android.synthetic.main.login_activity_bind_phone.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 绑定手机号码 Activity
*/
class BindPhoneActivity : BaseActivity(), LoginContract.View {

    //0-首次绑定手机号码 1-更换绑定手机号
    private var pageType = 0

    override fun showAgreement(url: String) {

    }

    companion object {
        private const val PAGE_TYPE = "pageType"

        fun actionStart(context: Context) {
            actionStart(context, 0)
        }

        fun actionStart(context: Context, pageType: Int) {
            val intent = Intent(context, BindPhoneActivity::class.java)
            intent.putExtra(PAGE_TYPE, pageType)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { LoginPresenter() }

    override fun layoutId(): Int = R.layout.login_activity_bind_phone

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        pageType = intent.getIntExtra(PAGE_TYPE, 0)

        setLoginClickable(false)

        ib_close.setOnClickListener { finish() }

        //下一步按钮
        btn_next.setOnClickListener { clickNext() }

        //手机号
        et_phone.addTextChangedListener {
            setLoginClickable(et_phone.text!!.isNotEmpty() && et_phone.text!!.length == 11)
        }

//        if (bind_type == 2) {
//            //TCAgent.onEvent(this, "注册-QQ")
//        } else {
//            //  TCAgent.onEvent(this, "注册-微信")
//        }
    }

    private fun clickNext() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        val phoneStr = et_phone.text.toString().trim()
        when {
            phoneStr.isEmpty() -> showToast(getString(R.string.error_phone_member_empty))
            !StringFormatUtil.isMobileMatch(phoneStr) -> showToast(getString(R.string.error_phone_member_match))
            else -> {
                if (pageType == 0) {
                    SendVerifyCodeActivity.actionStart(this, phoneStr, 2)
                } else {
                    SendVerifyCodeActivity.actionStart(this, phoneStr, 5)
                }
            }
        }
    }

    override fun start() {

    }

    private fun setLoginClickable(type: Boolean) {
        btn_next.isClickable = type
        btn_next.isEnabled = type
        btn_next.isSelected = type
        btn_next.isPressed = type
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainLoginEvent(event: MainLoginEvent) {
        if (event.logintype) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        EventBus.getDefault().unregister(this)
    }

    override fun showCountdown() {
        dismissLoading()
        showToast(R.string.sent_code)
    }

    override fun showLoginSuccess(type: Int) {

    }

    override fun showLoginRegister(type: Int) {
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        dismissLoading()
    }

}