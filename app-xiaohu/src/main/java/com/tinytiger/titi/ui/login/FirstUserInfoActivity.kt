package com.tinytiger.titi.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.mine.CityBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.image.CropImageActivity

import com.tinytiger.titi.mvp.contract.mine.OperationContract
import com.tinytiger.titi.mvp.presenter.mine.OperationPresenter
import com.orhanobut.logger.Logger

import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.login_activity_first_user.*


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 设置个人信息 Activity
*/
class FirstUserInfoActivity : BaseActivity(), OperationContract.View {
    override fun showCityList(data: List<CityBean.ProvinceBean>) {

    }

    companion object {
        /**
         * type
         * 1 手机号登录
         * 2 微信登录
         * 3 QQ登录
         */
        fun actionStart(context: Context,type:Int) {
            val intent = Intent(context, FirstUserInfoActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }


    private val mPresenter by lazy { OperationPresenter() }


    override fun layoutId(): Int = R.layout.login_activity_first_user

    override fun initData() {
        mPresenter.attachView(this)
    }

    private var cb_boy="1"
    override fun initView() {
       val type = intent.getIntExtra("type", 0)

        tv_skip.setOnClickListener {
            toSkip()
        }

        iv_avatar.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }


            AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .rationale(DefaultRationale())
                .onGranted {
                    CropImageActivity.actionStart(this, 0, 44)
                }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                        PermissionSetting().showSettingDialog(this@FirstUserInfoActivity,permissions)
                    }
                }.start()
        }

        iv_boy.setOnClickListener {
            cb_boy="1"
            iv_boy.setImageResource(R.mipmap.icon_gender_male_2)
            iv_girl.setImageResource(R.mipmap.icon_gender_girl_1)
        }

        iv_girl.setOnClickListener {
            cb_boy="2"
            iv_boy.setImageResource(R.mipmap.icon_gender_male_1)
            iv_girl.setImageResource(R.mipmap.icon_gender_girl_2)
        }

        btn_complete.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

            if (pathIcon.isNotEmpty()) {
                if (qiniutoken.isNotEmpty()) {
                    uploadQiniu()
                } else {
                    mPresenter.loadQiniuToken()
                }

            } else {
                val gender = cb_boy
                mPresenter.setNoviceUserProfile("", et_input_nickname.text.toString(), gender)
            }
        }

        setBtnType(true)

        when(type){
            1->{
              //  TCAgent.onEvent(this, "个人资料填写页-手机号")
            }
            2->{
             //   TCAgent.onEvent(this, "个人资料填写页-微信")
            }
            3->{
              //  TCAgent.onEvent(this, "个人资料填写页-QQ")
            }
        }
    }


    override fun start() {

    }

    private fun setBtnType(type: Boolean) {
        btn_complete.isClickable = type
        btn_complete.isEnabled = type
        btn_complete.isSelected = type
        btn_complete.isPressed=type
    }
    private var qiniutoken = ""

    override fun getQiniuToken(qiniu: String) {
        qiniutoken = qiniu
        uploadQiniu()
    }

    override fun showUserInfo(bean: UserInfoData) {

    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onBackPressed() {
        toSkip()
    }


    private fun toSkip() {
        AppManager.getAppManager().finishActivity(LoginActivity::class.java)
        finish()
    }

    override fun showResult() {
        toSkip()
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    private var pathIcon = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 44) {
            pathIcon = data!!.getStringExtra("path")
            Logger.d(pathIcon)
            GlideUtil.loadImg( iv_avatar,pathIcon)
        }
    }


    //图片上传
    private fun uploadQiniu() {
        Logger.d(pathIcon)
        val key = "img" + System.currentTimeMillis() + ".png"
        QiNiuUtil.sUploadManager(qiniutoken,  key,pathIcon, object : QiNiuUtil.OnQiNiuListener {
            override fun onType(type: Boolean) {
                if (type) {
                    val gender = cb_boy
                    mPresenter.setNoviceUserProfile(key, et_input_nickname.text.toString(), gender)
                } else {
                    hideProgress()
                    showToast("上传失败")
                }
            }
        })
    }

}