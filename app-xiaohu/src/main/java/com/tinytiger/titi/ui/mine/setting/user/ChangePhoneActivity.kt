package com.tinytiger.titi.ui.mine.setting.user

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.mvp.contract.mine.UserOperationContract
import com.tinytiger.titi.mvp.presenter.mine.UserOperationPresenter
import com.tinytiger.titi.utils.StringFormatUtil
import kotlinx.android.synthetic.main.mine_activity_change_phone.*


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 绑定手机号码 Activity
*/
class ChangePhoneActivity : BaseActivity(), UserOperationContract.View {



    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, ChangePhoneActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { UserOperationPresenter() }

    override fun layoutId(): Int = R.layout.mine_activity_change_phone

    override fun initData() {
        mPresenter.attachView(this)
    }


    override fun initView() {
        title_view.centerTxt = "绑定新手机号"
        title_view.rightTxt = getString(R.string.complete)
        title_view.setBackOnClick {
            back()
        }
        title_view.setRightOnClick {
            if (FastClickUtil.isFastClickTiming()) {
                return@setRightOnClick
            }
            val phone = et_phone_member.text.toString().trim()
            val code = et_code.text.toString().trim()

            when {
                phone.isEmpty() -> showToast(R.string.error_phone_member_empty)
                !StringFormatUtil.isMobileMatch(phone) -> showToast(R.string.error_phone_member_match)
                code.isEmpty() -> showToast(R.string.error_code_empty)
                code.length < 4 || code.length > 6 -> showToast(R.string.error_code_match)
                else -> mPresenter.changePhone(phone, code)
            }
        }

        tv_get_code.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            val phone = et_phone_member.text.toString().trim()
            when {
                phone.isEmpty() -> showToast(R.string.error_phone_member_empty)
                !StringFormatUtil.isMobileMatch(phone) -> showToast(R.string.error_phone_member_match)
                else -> mPresenter.sendCode(phone)
            }

        }
    }

    override fun onBackPressed() {
        back()
    }
    private fun back(){
        TextDialog.create(supportFragmentManager)
            .setMessage("确认要放弃更改手机号?")
            .setViewListener(object :TextDialog.ViewListener{
                override fun click() {
                  finish()
                }
            }).show()
    }

    override fun start() {

    }


    override fun showUserInfo(bean: UserInfoData) {

    }

    override fun showResult() {

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        myCountDownTimer.cancel()
        mPresenter.detachView()
    }

    override fun showCountdown() {
        dismissLoading()
        myCountDownTimer.start()
        showToast(R.string.sent_code)
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