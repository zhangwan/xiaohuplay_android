package com.tinytiger.titi.ui.login


import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.MainLoginEvent
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
import kotlinx.android.synthetic.main.login_fragment_login.*
import org.greenrobot.eventbus.EventBus

/*
* @author Tamas
* create at 2019/11/12 0012
* Email: ljw_163mail@163.com
* description: Activity 验证码登录页面
*/
class LoginActivity : BaseActivity(), LoginContract.View {

    private val mPresenter by lazy { LoginPresenter() }

    companion object {
        fun actionStart(context: Context) {
            actionStart(context, 0)
        }

        /**
         * close 0正常流程无多余操作,1关闭多余页面状态 2.被提出
         */
        fun actionStart(context: Context, close: Int) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("close", close)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.login_fragment_login

    var close = 0
    override fun initData() {
        setWindowFeature()
        close = intent.getIntExtra("close", 0)
    }

    override fun initView() {
        mPresenter.attachView(this)
        setLoginClickable(false)
        setLoginByPswClickable(false)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.ll_content, LoginWayFragment.getInstance(true))
            .commit()

        ib_close.setOnClickListener { finish() }

        //切换密码登录
        tv_psw_login.setOnClickListener { switchLoginWay(true) }

        //切换验证码登录
        tv_code_login.setOnClickListener { switchLoginWay(false) }

        //下一步按钮
        btn_next.setOnClickListener { clickNext() }

        //忘记密码
        tv_forget_password.setOnClickListener { clickForgetPsw() }

        //登录按钮
        btn_login.setOnClickListener { clickBtnLogin() }

        //输入手机号监听
        et_phone.addTextChangedListener {
            setLoginClickable(et_phone.text!!.isNotEmpty() && et_phone.text!!.length == 11)
        }
        et_phoneByPsw.addTextChangedListener {
            val phoneClickable =
                et_phoneByPsw.text!!.isNotEmpty() && et_phoneByPsw.text!!.length == 11
            val pswClickable =
                et_input_password.text!!.isNotEmpty() && et_input_password.text!!.length > 7
            setLoginByPswClickable(phoneClickable && pswClickable)
            tv_forget_password.visibility = if (phoneClickable) View.VISIBLE else View.GONE
        }
        //输入密码监听
        et_input_password.addTextChangedListener {
            setLoginByPswClickable(
                et_phoneByPsw.text!!.isNotEmpty() && et_phoneByPsw.text!!.length == 11
                        && et_input_password.text!!.isNotEmpty() && et_input_password.text!!.length > 7
            )
        }

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

    /**
     * 切换登录方式
     * @param isByPsw 是否根据密码登录
     */
    private fun switchLoginWay(isByPsw: Boolean) {
        ll_loginByCode.visibility = if (isByPsw) View.GONE else View.VISIBLE
        ll_loginByPsw.visibility = if (isByPsw) View.VISIBLE else View.GONE
        if (isByPsw) {
            et_phoneByPsw.requestFocus()
            et_phoneByPsw.text = et_phone.text
            et_phoneByPsw.setSelection(et_phoneByPsw.text!!.length)
        } else {
            et_phone.requestFocus()
            et_phone.text = et_phoneByPsw.text
            et_phone.setSelection(et_phone.text!!.length)
        }
    }

    /**
     * 点击忘记密码
     */
    private fun clickForgetPsw() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        val phoneStr = et_phoneByPsw.text.toString().trim()
        when {
            phoneStr.isEmpty() -> showToast(getString(R.string.error_phone_member_empty))
            !StringFormatUtil.isMobileMatch(phoneStr) -> showToast(getString(R.string.error_phone_member_match))
            else -> {
                SendVerifyCodeActivity.actionStart(this, phoneStr, 1)
            }
        }
    }

    /**
     * 验证码登录
     */
    private fun clickNext() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        val phoneStr = et_phone.text.toString().trim()
        when {
            phoneStr.isEmpty() -> showToast(getString(R.string.error_phone_member_empty))
            !StringFormatUtil.isMobileMatch(phoneStr) -> showToast(getString(R.string.error_phone_member_match))
            else -> {
                SendVerifyCodeActivity.actionStart(this, phoneStr, 0)
            }
        }
    }

    /**
     * 密码登录
     */
    private fun clickBtnLogin() {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        val phone = et_phoneByPsw.text.toString().trim()
        val password = et_input_password.text.toString().trim()
        when {
            !StringFormatUtil.isMobileMatch(phone) -> showToast(getString(R.string.error_phone_member_match))
            else -> {
                mPresenter.onPasswordLogin(phone, password)
            }
        }
    }

    /**
     * 验证码登录-下一步按钮可点击性
     */
    private fun setLoginClickable(clickable: Boolean) {
        btn_next.isClickable = clickable
        btn_next.isEnabled = clickable
        btn_next.isSelected = clickable
        btn_next.isPressed = clickable
    }

    /**
     * 密码登录-登录按钮可点击性
     */
    private fun setLoginByPswClickable(clickable: Boolean) {
        btn_login.isClickable = clickable
        btn_login.isEnabled = clickable
        btn_login.isSelected = clickable
        btn_login.isPressed = clickable
    }

    override fun start() {

    }

    override fun showAgreement(url: String) {
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
            2, 3 -> {
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

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}

