package com.tinytiger.titi.ui.test

import android.content.Intent
import android.graphics.Bitmap
import com.tinytiger.common.basis.BasisActivity
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.test.vp.LoginPresenter
import com.tinytiger.titi.ui.test.vp.LoginView
import kotlinx.android.synthetic.main.login_fragment_login.*

/*
 * @author zwy
 * create at 2020/6/5 0005
 * description:
 */
class TestNetWorkActivity : BasisActivity<LoginPresenter>(), LoginView {
    override fun layoutId(): Int = R.layout.activity_login_by_psw

    override fun initData() {

    }

    override fun initView() {
        initListener()

    }

    private fun initListener() {
//        tv_get_code.setOnClickListener {
//            var edtPhone = et_phone.text.toString().trim()
//            basePresenter?.sendMsg(edtPhone)
//        }
        btn_next.setOnClickListener {
//            var edtPhone = et_phone.text.toString().trim()
//            var edtCode = et_code.text.toString().trim()
//            basePresenter?.loginByCode(edtPhone, edtCode)
        }
    }


    override fun setVerificationCode(bitmap: Bitmap) {
    }

    override fun startCountDown() {

    }

    override fun finishActivity() {

    }

    override fun startChange() {
        startActivity(Intent(this, TestActivity::class.java))
        this.finish()
    }
}