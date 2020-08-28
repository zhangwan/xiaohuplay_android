package com.tinytiger.titi.ui.login

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.AppManager
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.presenter.mine.UserOperationPresenter
import kotlinx.android.synthetic.main.login_activity_first_password.*


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 设置密码 Activity
*/
class FirstPasswordActivity : BaseActivity(), UserOperationContract.View {
    private var phone: String? = null

    override fun showCountdown() {

    }

    override fun showUserInfo(bean: UserInfoData) {

    }

    companion object {
        private const val EXTRA_PHONE = "extra_phone"

        fun actionStart(context: Context, phone: String) {
            val intent = Intent(context, FirstPasswordActivity::class.java)
            intent.putExtra(EXTRA_PHONE, phone)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { UserOperationPresenter() }


    override fun layoutId(): Int = R.layout.login_activity_first_password

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        if (intent.hasExtra(EXTRA_PHONE)) {
            phone = intent.getStringExtra(EXTRA_PHONE)
            tv_phone.text = phone
        }

        title_view.rightTxt = "跳过"
        title_view.setRightTxtColor(ContextCompat.getColor(this, R.color.color_fbcc33))
        title_view.setBackOnClick {
            finish()
        }
        title_view.setRightOnClick {
            toSkip()
        }

        btn_complete.setOnClickListener {
            val password = et_input_password.text.toString().trim()
            mPresenter.onResetPassword(password, password)
        }

        setCompBtnClickable(false)

        et_input_password.addTextChangedListener(mTextChangeListener)
        //TCAgent.onEvent(this, "注册-手机号")
    }

    private fun setCompBtnClickable(type: Boolean) {
        btn_complete.isClickable = type
        btn_complete.isEnabled = type
        btn_complete.isSelected = type
        btn_complete.isPressed = type
    }

    private val mTextChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (et_input_password.text!!.isNotEmpty() && et_input_password.text!!.length > 7) {
                setCompBtnClickable(true)
            } else {
                setCompBtnClickable(false)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }


    override fun start() {

    }

    private fun toSkip() {
        AppManager.getAppManager().finishActivity(LoginActivity::class.java)
        finish()
    }


    override fun onBackPressed() {
        toSkip()//返回键等同于跳过
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun showResult() {
        showToast("设置密码成功")
        toSkip()
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg);
    }

}