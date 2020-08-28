package com.tinytiger.titi.ui.mine.setting.user

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.AppManager
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.presenter.mine.UserOperationPresenter
import com.tinytiger.titi.utils.StringFormatUtil
import kotlinx.android.synthetic.main.mine_activity_code_phone.*


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 原手机验证 Activity
*/
class CodePhoneActivity : BaseActivity(), UserOperationContract.View {

    //0 原手机验证=>修改手机  1 手机验证=>设置密码
    private var type = 0

    companion object {
        /**
         * 0 原手机验证=>修改手机号码
         * 1 验证手机号=>设置密码
         */
        private const val EXTRA_TYPE = "type"

        fun actionStart(context: Context, type: Int, phone: String) {
            val intent = Intent(context, CodePhoneActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            intent.putExtra("phone", phone)
            context.startActivity(intent)
        }

        fun actionStart(context: Context, type: Int, phone: String, is_password: Int) {
            val intent = Intent(context, CodePhoneActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)
            intent.putExtra("phone", phone)
            intent.putExtra("is_password", is_password)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { UserOperationPresenter() }

    override fun layoutId(): Int = R.layout.mine_activity_code_phone

    private var phone = ""
    private var is_password = 0
    override fun initData() {
        mPresenter.attachView(this)

        type = intent.getIntExtra(EXTRA_TYPE, 0)

        phone = intent.getStringExtra("phone")
        is_password = intent.getIntExtra("is_password", 0)
    }


    override fun initView() {
        if (type == 0) {
            title_view.centerTxt = "原手机验证"
            ll_password.visibility = View.VISIBLE
        } else {
            title_view.centerTxt = "验证手机号"
        }

        tv_phone.text = StringFormatUtil.getMobileReplace(phone)
        ll_password.visibility = if (is_password == 1) View.VISIBLE else View.GONE

        title_view.rightTxt = getString(R.string.next)
        title_view.setBackOnClick {
            finish()
        }
        title_view.setRightOnClick {
            if (FastClickUtil.isFastClickTiming()) {
                return@setRightOnClick
            }
            val code = et_code.text.toString().trim()
            if (type == 1) {
                when {
                    code.isEmpty() -> showToast(R.string.error_code_empty)
                    code.length < 4 -> showToast(R.string.error_code_match)
                    else -> {
//                        mPresenter.verificationPhone(phone, code)
                        mPresenter.recoverPassword(phone, code)
                    }
                }
            } else {
                if (is_password == 1) {
                    val password = et_input_password.text.toString().trim()
                    when {
                        code.isEmpty() -> showToast(R.string.error_code_empty)
                        code.length < 4 || code.length > 6 -> showToast(R.string.error_code_match)
                        is_password == 1 && password.isEmpty() -> showToast(getString(R.string.error_password_empty))
                        else -> {
                            mPresenter.verificationPhone(code, password)
                        }
                    }
                } else {
                    when {
                        code.isEmpty() -> showToast(R.string.error_code_empty)
                        code.length < 4 -> showToast(R.string.error_code_match)
                        else -> {
                            mPresenter.verificationPhone(code)

                        }
                    }
                }
            }

        }

        tv_get_code.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

                mPresenter.sendCode(phone)

        }

        cb_close.setOnCheckedChangeListener(mCheckedChangeListener)
    }


    override fun start() {

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

    override fun showCountdown() {
        dismissLoading()
        myCountDownTimer.start()
        showToast(R.string.sent_code)
    }


    override fun showUserInfo(bean: UserInfoData) {


    }

    override fun showResult() {
        showToast("验证成功")
        myCountDownTimer.cancel()
        if (type == 0) {
            ChangePhoneActivity.actionStart(this)
        } else {
            NewsPasswordActivity.actionStart(this)
            AppManager.getAppManager().finishActivity(CodePasswordActivity::class.java)

        }
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        myCountDownTimer.cancel()
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


    /**
     * ====================================倒计时============================================
     */
    private val myCountDownTimer = object : CountDownTimer(60 * 1000, 1000) {
        override fun onFinish() {
            tv_get_code.isClickable = true
            tv_get_code.text = getText(R.string.code_acquire)
        }

        override fun onTick(millisUntilFinished: Long) {
            tv_get_code.isClickable = false
            tv_get_code.text = "${millisUntilFinished / 1000}s"
        }
    }
}