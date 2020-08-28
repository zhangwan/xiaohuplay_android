package com.tinytiger.titi.ui.login

import android.content.Context
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import androidx.core.widget.addTextChangedListener
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.umeng.LoginAgentUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.mvp.contract.login.LoginContract
import com.tinytiger.titi.mvp.presenter.LoginPresenter
import com.tinytiger.titi.utils.StringFormatUtil
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.activity_login_by_psw.*
import org.greenrobot.eventbus.EventBus

/*
* @author lmq001
* create at 2019/11/12 0012
* Email: ljw_163mail@163.com
* description: Activity 密码登录页面
*/
class LoginByPswActivity : BaseActivity(), LoginContract.View {
    var close = 0
    var phone = ""

    private val mPresenter by lazy { LoginPresenter() }

    companion object {
        private const val EXTRA_PHONE = "extra_phone"

        fun actionStart(context: Context, phone: String) {
            actionStart(context, 0, phone)
        }

        /**
         * close 0正常流程无多余操作,1关闭多余页面状态 2.被提出
         */
        fun actionStart(context: Context, close: Int, phone: String) {
            val intent = Intent(context, LoginByPswActivity::class.java)
            intent.putExtra("close", close)
            intent.putExtra(EXTRA_PHONE, phone)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_login_by_psw


    override fun initData() {
        setWindowFeature()
        close = intent.getIntExtra("close", 0)
    }

    override fun initView() {
        mPresenter.attachView(this)

        if (intent.hasExtra(EXTRA_PHONE)) {
            phone = intent.getStringExtra(EXTRA_PHONE)
            et_phone.setText(phone)
            et_phone.setSelection(et_phone.text!!.length)
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.ll_content, LoginWayFragment.getInstance(true))
            .commit()

        ib_close.setOnClickListener {
            finish()
        }

        tv_psw_login.setOnClickListener {
            LoginActivity.actionStart(this)
        }

        //登录按钮
        btn_login.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            val phone = et_phone.text.toString().trim()
            val password = et_input_password.text.toString().trim()
            when {
                !StringFormatUtil.isMobileMatch(phone) -> showToast(getString(R.string.error_phone_member_match))
                else -> {
                    mPresenter.onPasswordLogin(phone, password)
                }
            }
        }

        setLoginClickable(false)
        //忘记密码
        tv_forget_password.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            val phoneStr = et_phone.text.toString().trim()
            when {
                phoneStr.isEmpty() -> showToast(getString(R.string.error_phone_member_empty))
                !StringFormatUtil.isMobileMatch(phoneStr) -> showToast(getString(R.string.error_phone_member_match))
                else -> {
                    SendVerifyCodeActivity.actionStart(this, phoneStr, 1)
                }
            }
        }
        //手机号
        et_phone.addTextChangedListener {
            val phoneClickable = et_phone.text!!.isNotEmpty() && et_phone.text!!.length == 11
            val pswClickable =
                et_input_password.text!!.isNotEmpty() && et_input_password.text!!.length > 7
            setLoginClickable(phoneClickable && pswClickable)
            tv_forget_password.visibility = if (phoneClickable) View.VISIBLE else View.GONE
        }

        //密码
        et_input_password.addTextChangedListener {
            setLoginClickable(
                et_phone.text!!.isNotEmpty() && et_phone.text!!.length == 11
                        && et_input_password.text!!.isNotEmpty() && et_input_password.text!!.length > 7
            )
        }

        cb_close.setOnCheckedChangeListener(mCheckedChangeListener)

        //强制清除用户数据
        MyUserData.setUserClear()
        //TCAgent.onEvent(this, "页面-登录与注册")

        mPresenter.mContext = this

        if (close == 2) {
            val msg = "您的账号正在另一台设备登录成功，本设备被强制退出登录，如要在本设备使用，请重新登录"
            TextDialog.create(supportFragmentManager)
                .setMessage(msg)
                .setShowButton(false, true).show()
        }
    }

    private fun setLoginClickable(clickable: Boolean) {
        btn_login.isClickable = clickable
        btn_login.isEnabled = clickable
        btn_login.isSelected = clickable
        btn_login.isPressed = clickable
    }

    override fun start() {

    }

    override fun showAgreement(url: String) {
    }

    private val mCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                et_input_password.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                et_input_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    /**
     * QQ 登录需要回调信息
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 登录成功
     */
    override fun showLoginSuccess(type: Int) {
        dismissLoading()
        EventBus.getDefault().post(MainLoginEvent(true))
        AppManager.getAppManager().finishActivity(LoginActivity::class.java)
        finish()
    }

    /**
     * 登录或注册
     * @param type 1 验证码注册 2 微信 3  QQ
     */
    override fun showLoginRegister(type: Int) {
        dismissLoading()
        when (type) {
            1 -> {
                EventBus.getDefault().post(MainLoginEvent(true))
                FirstPasswordActivity.actionStart(this, et_phone.text.toString().trim())
                LoginAgentUtils.setLoginPhone()
                finish()
            }
            2 -> {
                BindPhoneActivity.actionStart(this)
            }
            3 -> {
                BindPhoneActivity.actionStart(this)
            }
        }
    }

    override fun showCountdown() {
        showToast(R.string.sent_code)
    }


    override fun showLoading() {
        LoadingUtils.getInstance().show(this)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

}

