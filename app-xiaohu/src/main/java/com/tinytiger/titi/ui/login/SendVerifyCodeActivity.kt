package com.tinytiger.titi.ui.login

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.LoginAgentUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.VerifyCodeView
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.login.LoginContract
import com.tinytiger.titi.mvp.presenter.LoginPresenter
import kotlinx.android.synthetic.main.login_activity_forget_password.*
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 发送验证码 Activity
*/
class SendVerifyCodeActivity : BaseActivity(), LoginContract.View {

    /**
     * 0-输入验证码登录页面
     * 1-找回密码页面
     * 2-绑定手机号
     * 3-原手机号获取验证码
     * 4-修改密码
     * 5-更换绑定手机号
     */
    private var pageType = 0

    private var phone = ""
    private var bind_type = 1

    override fun showAgreement(url: String) {

    }

    companion object {
        fun actionStart(context: Context, phone: String, pageType: Int) {
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, SendVerifyCodeActivity::class.java)
            intent.putExtra("phone", phone)
            intent.putExtra("pageType", pageType)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { LoginPresenter() }

    override fun layoutId(): Int = R.layout.login_activity_forget_password

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        pageType = intent.getIntExtra("pageType", 0)
        phone = intent.getStringExtra("phone")!!

        tv_phone.text = phone
        tv_title_name.text =
            if (pageType == 1) getString(R.string.look_back_password) else getString(R.string.input_verify_code)

        title_view.setBackOnClick {
            finish()
        }
        // 保存
        tv_get_code.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            start()
        }

        verify_code_view.setInputCompleteListener(object : VerifyCodeView.InputCompleteListener {
            override fun inputComplete() {
                verifyInputComplete()
            }

            override fun invalidContent() {
            }
        })

        setGetCodeClickable(false)
        start()
    }

    /**
     * 输入完成  0-输入验证码登录页面 1-找回密码页面 2-绑定手机号页面
     *          3-原手机号获取验证码 4-修改密码 5-更换绑定手机号
     */
    private fun verifyInputComplete() {
        when (pageType) {
            0 -> {
                mPresenter.onCodeLogin(phone, verify_code_view.editContent)
            }
            1, 4 -> {
                mPresenter.recoverPassword(phone, verify_code_view.editContent)
            }
            2 -> {
                bind_type = SpUtils.getInt(R.string.bind_type, 1)
                val bind_unionid = SpUtils.getString(R.string.bind_unionid, "")
                val nickname = SpUtils.getString(R.string.nickname, "")
                val avatar = SpUtils.getString(R.string.avatar, "")
                val gender = SpUtils.getString(R.string.gender, "")
                mPresenter.onBindPhone(
                    bind_type, bind_unionid, nickname, avatar,
                    gender, phone, verify_code_view.editContent
                )
            }
            3 -> {
                mPresenter.verificationPhone(verify_code_view.editContent)
            }
            5 -> {
                mPresenter.changePhone(phone, verify_code_view.editContent)
            }
        }
    }

    /***
     *  登录成功
     *  @type 1-微信 2-QQ 3-密码 4-手机号
     */
    override fun showLoginSuccess(type: Int) {
        myCountDownTimer.cancel()
        dismissLoading()

        when (pageType) {
            0 -> {      //输入验证码登录页面
                EventBus.getDefault().post(MainLoginEvent(true))
                AppManager.getAppManager().finishActivity(LoginActivity::class.java)
            }
            1 -> {      //找回密码页面
                EventBus.getDefault().post(MainLoginEvent(true))
                ResetPasswordActivity.actionStart(this, phone, 0)
            }
            2 -> {      //绑定手机号页面
                EventBus.getDefault().post(MainLoginEvent(true))
                AppManager.getAppManager().finishActivity(LoginActivity::class.java)
//                if (type == 2) {
//                    if (bind_type == 2) {
//                        FirstUserInfoActivity.actionStart(this, 3)
//                    } else {
//                        FirstUserInfoActivity.actionStart(this, 2)
//                    }
//                } else {
//                    AppManager.getAppManager().finishActivity(LoginActivity::class.java)
//                }
                if (bind_type == 2) {
                    LoginAgentUtils.setLoginQQ()
                } else {
                    LoginAgentUtils.setLoginWx()
                }
            }
            3 -> {        //原手机号获取验证码
                BindPhoneActivity.actionStart(this, 1)
            }
            4 -> {        //修改密码
                ResetPasswordActivity.actionStart(this, phone, 1)
            }
            5 -> {        //更换绑定手机号
                EventBus.getDefault().post(MainLoginEvent("mine", true))
            }
        }

        finish()
    }

    /**
     * 登录或注册
     * @param type 1 验证码注册 2 微信 3  QQ
     */
    override fun showLoginRegister(type: Int) {
        myCountDownTimer.cancel()
        dismissLoading()
        when (type) {
            1 -> {
                EventBus.getDefault().post(MainLoginEvent(true))
                FirstPasswordActivity.actionStart(this, phone)
                LoginAgentUtils.setLoginPhone()
//                finish()
            }
            2, 3 -> {
                BindPhoneActivity.actionStart(this)
            }
        }
    }

    override fun start() {
        mPresenter.sendCode(phone)
    }

    /**
     * 自动弹软键盘
     */
    fun showKeyBord(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        myCountDownTimer.cancel()
    }

    override fun showCountdown() {
        dismissLoading()
        myCountDownTimer.start()
        showToast(R.string.sent_code)
        showKeyBord(verify_code_view.editText)
    }

    override fun showLoading() {
        LoadingUtils.getInstance().setTimeOut(30 * 1000).show(this)
    }

    override fun dismissLoading() {
        LoadingUtils.getInstance().dismiss()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            myCountDownTimer.cancel()
            tv_get_code.text = "获取验证码"
            setGetCodeClickable(true)
        }
    }

    /**
     * ====================================倒计时============================================
     */
    private val myCountDownTimer = object : CountDownTimer(60 * 1000, 1000) {
        override fun onFinish() {
            setGetCodeClickable(true)
            tv_get_code.text = "获取验证码"
        }

        override fun onTick(millisUntilFinished: Long) {
            setGetCodeClickable(false)
            tv_get_code.text = "获取验证码(${millisUntilFinished / 1000}S)"
        }
    }

    private fun setGetCodeClickable(type: Boolean) {
        tv_get_code.isClickable = type
        tv_get_code.isEnabled = type
        tv_get_code.isSelected = type
        tv_get_code.isPressed = type
    }

}