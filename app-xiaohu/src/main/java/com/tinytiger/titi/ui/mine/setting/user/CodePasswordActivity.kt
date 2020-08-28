package com.tinytiger.titi.ui.mine.setting.user

import android.content.Context
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.CompoundButton
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.presenter.mine.UserOperationPresenter
import kotlinx.android.synthetic.main.setting_activity_codepassword.*


/**
 *
 * @author zhw_luke
 * @date 2019/12/4 0004 下午 6:16
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 修改手机号码---验证密码
 */
class CodePasswordActivity : BaseActivity(), UserOperationContract.View {

    companion object {
        fun actionStart(context: Context,phone:String) {
            val intent = Intent(context, CodePasswordActivity::class.java)
            intent.putExtra("phone", phone)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { UserOperationPresenter() }

    override fun layoutId(): Int = R.layout.setting_activity_codepassword
    private var  phone=""
    override fun initData() {
        mPresenter.attachView(this)
        phone=intent.getStringExtra("phone")
    }

    override fun initView() {
        title_view.centerTxt = "验证密码"

        title_view.rightTxt = getString(R.string.next)
        title_view.setBackOnClick {
            finish()
        }
        title_view.setRightOnClick {
            if (FastClickUtil.isFastClickTiming()) {
                return@setRightOnClick
            }

            val password = et_input_password.text.toString().trim()

            when {
                password.isEmpty() -> showToast(getString(R.string.error_password_empty))
                password.length < 8 -> showToast("登录密码长度小于8位")
                else -> {
                    mPresenter.modifyPassword(password)
                }
            }
        }

        cb_close.setOnCheckedChangeListener(mCheckedChangeListener)

        tvCode.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            CodePhoneActivity.actionStart(this, 1,phone)
        }
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
    }

    override fun showUserInfo(bean: UserInfoData) {
    }

    override fun showResult() {
        showToast("验证成功")
        NewsPasswordActivity.actionStart(this)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
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