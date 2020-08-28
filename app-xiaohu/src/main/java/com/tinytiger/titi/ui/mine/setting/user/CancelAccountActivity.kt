package com.tinytiger.titi.ui.mine.setting.user

import android.content.Context
import android.content.Intent
import android.view.View
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.MainLoginEvent
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.im.config.LogoutHelper
import com.tinytiger.titi.mvp.contract.mine.CancelAccountContract
import com.tinytiger.titi.mvp.presenter.mine.CancelAccountPresenter
import com.tinytiger.titi.ui.login.LoginActivity
import com.tinytiger.titi.ui.main.MainActivity
import kotlinx.android.synthetic.main.user_activity_cancel.*
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2020/1/9 0009
* Email: ljw_163mail@163.com
* description: 
*/
class CancelAccountActivity :BaseActivity() , CancelAccountContract.View {


    private var isApply = false

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CancelAccountActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mPresenter by lazy { CancelAccountPresenter() }

    override fun layoutId(): Int  = R.layout.user_activity_cancel

    override fun initData() {
        mPresenter.attachView(this)

    }

    override fun initView() {
        btn_apply.isSelected = true
        btn_complete.isSelected = true

        title_view.centerTxt = "注销账号"
        title_view.setOnClickListener {
            finish()
        }

        btn_apply.setOnClickListener {
            TextDialog.create(supportFragmentManager)
                .setMessage("请确认您要注销该账号，此操作不可恢复")
                .isShowTitle(true)
                .setViewListener(object :TextDialog.ViewListener{
                    override fun click() {
                        mPresenter.cancellationAccount()

                    }

                })
                .show()
        }

        btn_complete.setOnClickListener {
            MyUserData.setUserClear()
            LogoutHelper.logout()
            EventBus.getDefault().post(MainLoginEvent(false))
            MainActivity().actionStart(this,0)
            finish()
        }

    }

    override fun start() {
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    override fun onBackPressed() {
        if(isApply){
            MyUserData.setUserClear()
            LogoutHelper.logout()
            EventBus.getDefault().post(MainLoginEvent(false))
            MainActivity().actionStart(this,0)
            finish()
        }else{
            super.onBackPressed()
        }
    }


    override fun showResult(bean: BaseBean) {
        isApply = true
        layout_apply.visibility = View.GONE
        layout_success.visibility = View.VISIBLE
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