package com.tinytiger.titi.ui.mine.setting.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.CityBean
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.CropImageActivity
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.picker.listener.OnOptionsSelectedListener
import com.tinytiger.common.widget.dialog.*
import com.tinytiger.titi.data.MyUserData

import com.tinytiger.titi.mvp.contract.mine.OperationContract
import com.tinytiger.titi.mvp.presenter.mine.OperationPresenter
import com.tinytiger.titi.utils.CommonUtils
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.utils.net.NetworkUtil
import com.tinytiger.titi.widget.dialog.EditNameDialog
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.mine_activity_setting_userinfo.*
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 编辑资料 Activity
*/
class SettingUserInfoActivity : BaseBackActivity(), OperationContract.View {


    companion object {
        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, SettingUserInfoActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val mPresenter by lazy { OperationPresenter() }

    override fun layoutId(): Int = R.layout.mine_activity_setting_userinfo

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        title_view.centerTxt = getString(R.string.user_info)
        title_view.setBackOnClick {
            finish()
        }
        //我的头像
        iv_avatar.setOnClickListener {

            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

            if (NetworkUtil.getNetworkType(this) == NetworkType.NETWORK_NO) {
                showToast(R.string.error_network)
                return@setOnClickListener
            }

            AndPermission.with(this).runtime().permission(Permission.Group.STORAGE)
                .rationale(DefaultRationale()).onGranted {
                    CropImageActivity.actionStart(this, 0, 44)
                }.onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                        PermissionSetting().showSettingDialog(this@SettingUserInfoActivity,
                            permissions)
                    }
                }.start()
        }
        item_nickname.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

            EditNameDialog.create(supportFragmentManager).apply {
                nickname = nickName
                title = "编辑昵称"
                maxLength = 10
            }.setViewListener(object : EditNameDialog.ViewListener {
                override fun click(string: String) {
                    mPresenter.onModifyNickname(string)
                }
            }).show()

        }
        //性别
        item_gender.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            GenderDialog.create(supportFragmentManager).setGender(gender)
                .setViewListener(object : GenderDialog.ViewListener {
                    override fun click(gender: Int) {
                        mPresenter.onModifyGender(gender.toString())
                    }

                }).show()
        }
        //生日
        item_age.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            val date = tv_age.text.toString()

            DateWheelDialog.create(supportFragmentManager).setViewDate(date)
                .setViewListener(object : DateWheelDialog.ViewListener {
                    override fun click(date: String) {
                        mPresenter.onModifyBirthday(date)
                    }

                }).show()
        }
        //城市
        item_city.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (mCityBean == null) {
                mPresenter.getAreaList()
                return@setOnClickListener
            }

            CityDialog.create(supportFragmentManager).setCityList(mCityBean!!)
                .setCity(provcn, citycn)
                .setOnSelectedListener(OnOptionsSelectedListener { _, _, _, opt2Data, _, _ ->
                    mPresenter.onModifyCity(opt2Data!!.name)
                }).show()
        }
        //简介
        item_desc.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            EditNameDialog.create(supportFragmentManager).apply {
                nickname = resume
                title = "编辑简介"
                maxLength = 30
            }.setViewListener(object : EditNameDialog.ViewListener {
                override fun click(string: String) {
                    mPresenter.onModifyResume(string)
                }
            }).show()
        }

        mPresenter.getUserInfo()
        mPresenter.loadQiniuToken()
        mPresenter.getAreaList()

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {

    }


    private var mCityBean: List<CityBean.ProvinceBean>? = null

    override fun showCityList(data: List<CityBean.ProvinceBean>) {
        mCityBean = data
    }

    private var gender = 1
    private var provcn = ""
    private var citycn = ""
    private var resume = ""
    private var nickName = ""
    override fun showUserInfo(bean: UserInfoData) {
        GlideUtil.loadImg(iv_avatar, bean.avatar)
        tv_nickname.text = bean.nickname
        nickName = bean.nickname
        gender = bean.gender

        if (bean.gender != 0) {
            tv_gender.text = if (gender == 1) "男" else "女"
        }

        if (bean.birthday != null) {
            tv_age.text =
                TimeZoneUtil.getDateToString(TimeZoneUtil.getToDate(bean.birthday), "yyyy年MM月dd日")
        }
        if (bean.citycn != null && bean.citycn.isNotEmpty()) {
            provcn = bean.provcn
            citycn = bean.citycn
            tv_city.text = if (CommonUtils.isLimitedCity(
                    bean.provcn)
            ) bean.provcn else "${bean.provcn}·${bean.citycn}"
        }

        if (bean.resume.isNullOrEmpty()) {
            tv_desc.text = "介绍一下自己吧~"
        } else {
            resume = bean.resume
            tv_desc.text = bean.resume
        }
    }


    override fun showResult() {
        mPresenter.getUserInfo()
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

    private var qiniutoken = ""

    override fun getQiniuToken(qiniu: String) {
        qiniutoken = qiniu
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 44) {
                val pathIcon = data!!.getStringExtra("path")

                QiNiuUtil.uploadImages(qiniutoken, listOf(pathIcon)) { result ->
                    if (result != null) {
                        mPresenter.onModifyAvatar(result)
                    }
                }
            }
        }

    }
}