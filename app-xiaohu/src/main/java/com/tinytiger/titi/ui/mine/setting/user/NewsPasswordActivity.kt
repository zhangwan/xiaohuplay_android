package com.tinytiger.titi.ui.mine.setting.user


import android.content.Context
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.CompoundButton
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.presenter.mine.UserOperationPresenter
import kotlinx.android.synthetic.main.setting_activity_newspassword.*


/**
 *
 * @author zhw_luke
 * @date 2019/12/4 0004 下午 5:58
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 设置新密码
 */
class NewsPasswordActivity : BaseActivity(), UserOperationContract.View {


    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, NewsPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { UserOperationPresenter() }

    override fun layoutId(): Int = R.layout.setting_activity_newspassword

    override fun initData() {
        mPresenter.attachView(this)
    }


    override fun initView() {
        tvTitle.centerTxt = "设置新密码"
        tvTitle.rightTxt = getString(R.string.complete)
        tvTitle.setBackOnClick {
            back()
        }

        tvTitle.setRightOnClick {
            if (FastClickUtil.isFastClickTiming()) {
                return@setRightOnClick
            }
            val password = et_input_password.text.toString().trim()
            val confirmPassword = et_input_password_again.text.toString().trim()

            when {
                password.isEmpty() -> showToast(R.string.error_password_no_equal)
                password.length < 8 -> showToast("密码不能小于8位")
                confirmPassword.isEmpty() -> showToast(R.string.error_password_no_equal)
                confirmPassword.length < 8 -> showToast("密码不能小于8位")
                password != confirmPassword -> showToast(R.string.error_password_no_equal)
                else -> mPresenter.onResetPassword(password, confirmPassword)
            }

        }

        cb_close.setOnCheckedChangeListener(mCheckedChangeListener)
        cb_close_again.setOnCheckedChangeListener(mCheckedChangeListener)
    }


    override fun start() {

    }

    override fun onBackPressed() {
        back()
    }
    private fun back(){
        TextDialog.create(supportFragmentManager)
            .setMessage("确认要放弃设置新密码?")
            .setViewListener(object : TextDialog.ViewListener{
                override fun click() {
                    finish()
                }
            }).show()
    }


    private val mCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.id == R.id.cb_close) {
                if (isChecked) {
                    et_input_password.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                } else {
                    et_input_password.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }
            } else {
                if (isChecked) {
                    et_input_password_again.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                } else {
                    et_input_password_again.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                }
            }

        }


    override fun showUserInfo(bean: UserInfoData) {

    }

    override fun showResult() {
        showToast("修改密码成功")
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun showCountdown() {
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