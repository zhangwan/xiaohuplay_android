package com.tinytiger.titi.ui.login

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.presenter.mine.UserOperationPresenter
import kotlinx.android.synthetic.main.login_activity_reset_password.*
import org.greenrobot.eventbus.EventBus

/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 重置密码 Activity
*/
class ResetPasswordActivity : BaseActivity(), UserOperationContract.View {

    private var phone: String? = null

    //0-重置密码 1-修改密码
    private var pageType = 0

    companion object {
        private const val PAGE_TYPE = "pageType"
        private const val EXTRA_PHONE = "extra_phone"

        fun actionStart(context: Context, phone: String, pageType: Int) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra(EXTRA_PHONE, phone)
            intent.putExtra(PAGE_TYPE, pageType)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { UserOperationPresenter() }

    override fun layoutId(): Int = R.layout.login_activity_reset_password

    override fun initData() {
        mPresenter.attachView(this)

        if (intent.hasExtra(EXTRA_PHONE)) {
            phone = intent.getStringExtra(EXTRA_PHONE)
        }
        if (intent.hasExtra(PAGE_TYPE)) {
            pageType = intent.getIntExtra(PAGE_TYPE, 0)
        }
    }

    override fun initView() {
        if (phone != null) {
            tv_phone.text = phone
        }
        tv_title_name.text = if (pageType == 0) "重置密码" else "设置密码"

        title_view.setBackOnClick {
            back()
        }

        btn_complete.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            val password = et_input_password.text.toString().trim()
            mPresenter.onResetPassword(password, password)
        }

        et_input_password.addTextChangedListener(mTextChangeListener)
        setCompBtnClickable(false)
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


    override fun showUserInfo(bean: UserInfoData) {

    }

    override fun showResult() {
        showToast(if (pageType == 0) "重置密码成功" else "设置密码成功")
        SpUtils.saveSP(R.string.username, phone)
        EventBus.getDefault().post(MainLoginEvent())
        finish()
    }

    override fun showCountdown() {
    }

    override fun onBackPressed() {
        back()
    }

    private fun back() {
        TextDialog.create(supportFragmentManager)
            .setMessage("确认要放弃" + if (pageType == 0) "重置密码?" else "设置密码?")
            .setViewListener(object : TextDialog.ViewListener {
                override fun click() {
                    finish()
                }
            }).show()
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
        dismissLoading()
        showToast(errorMsg)
    }


}