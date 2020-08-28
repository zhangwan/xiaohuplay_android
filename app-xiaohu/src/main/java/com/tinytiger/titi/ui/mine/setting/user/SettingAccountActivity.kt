package com.tinytiger.titi.ui.mine.setting.user

import android.content.Context
import android.content.Intent
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.mine.CityBean
import com.tinytiger.common.net.data.user.UserInfoData
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.mine.OperationContract
import com.tinytiger.titi.mvp.presenter.mine.OperationPresenter
import com.tinytiger.titi.ui.login.SendVerifyCodeActivity
import com.tinytiger.titi.utils.net.NetworkType
import com.tinytiger.titi.utils.net.NetworkUtil
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.mine_activity_setting.title_view
import kotlinx.android.synthetic.main.mine_activity_setting_account.*


/*
* @author Tamas
* create at 2019/11/13 0013
* Email: ljw_163mail@163.com
* description: 账号与安全 Activity
*/
class SettingAccountActivity : BaseActivity(), OperationContract.View {
    override fun showCityList(data: List<CityBean.ProvinceBean>) {

    }

    override fun getQiniuToken(qiniu: String) {

    }


    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, SettingAccountActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val mPresenter by lazy { OperationPresenter() }

    override fun layoutId(): Int = R.layout.mine_activity_setting_account

    override fun initData() {
        mPresenter.attachView(this)
    }

    private var bandType = false
    override fun initView() {
        title_view.centerTxt = getString(R.string.account_security)
        title_view.setBackOnClick {
            finish()
        }
        //更换号码
        item_edit_phone.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

            if (mUserInfoData != null) {
                SendVerifyCodeActivity.actionStart(this, mUserInfoData!!.username, 3)
//                CodePhoneActivity.actionStart(
//                    this,
//                    0,
//                    mUserInfoData!!.username,
//                    mUserInfoData!!.is_password
//                )
            }
        }
        //设置密码,修改密码
        item_edit_password.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (mUserInfoData != null) {
                SendVerifyCodeActivity.actionStart(this, mUserInfoData!!.username, 4)
//                if (mUserInfoData!!.is_password == 1) {
//                    CodePasswordActivity.actionStart(this, mUserInfoData!!.username)
//                } else {
//                    CodePhoneActivity.actionStart(this, 1, mUserInfoData!!.username)
//                }
            }
        }
        //是否绑定微信 1已绑定 0未绑定
        tv_bind_wechat.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (NetworkUtil.getNetworkType(this) == NetworkType.NETWORK_NO) {
                showToast(R.string.error_network)
                return@setOnClickListener
            }
            if (mUserInfoData == null) {
                showToast("数据获取失败")
                return@setOnClickListener
            }

            if (mUserInfoData!!.is_bind_wx == 0) {
                startAuth(1)
            } else {
                TextDialog.create(supportFragmentManager)
                    .setMessage("确定要解除绑定?")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            bandType = false
                            mPresenter.unBindSocialPlatform(1)
                        }
                    }).show()

            }
        }
        //是否绑定QQ 1已绑定 0未绑定
        tv_bind_qq.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (NetworkUtil.getNetworkType(this) == NetworkType.NETWORK_NO) {
                showToast(R.string.error_network)
                return@setOnClickListener
            }
            if (mUserInfoData == null) {
                showToast("数据获取失败")
                return@setOnClickListener
            }

            if (mUserInfoData!!.is_bind_qq == 0) {
                startAuth(2)
            } else {
                TextDialog.create(supportFragmentManager)
                    .setMessage("确定要解除绑定?")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            bandType = false
                            mPresenter.unBindSocialPlatform(2)
                        }
                    }).show()

            }
        }

        //注销账号
        item_cancel_account.setOnClickListener {
            CancelAccountActivity.actionStart(this)
        }
    }


    override fun onResume() {
        super.onResume()
        mPresenter.getAccountSecurityData()
    }

    override fun start() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private val mAuthListener = object : UMAuthListener {
        override fun onComplete(media: SHARE_MEDIA?, p1: Int, map: MutableMap<String, String>?) {
            dismissLoading()
            if (map == null) {
                showToast("获取信息失败")
                return
            }

            val name = map["name"]
            if (name == null) {
                showToast("获取信息失败")
                return
            }

            if (media == SHARE_MEDIA.WEIXIN) {
                val unionid = map["unionid"]
                if (unionid != null) {
                    bandType = true
                    mPresenter.bindSocialPlatform(1, unionid, name)
                } else {
                    showToast("绑定微信失败")
                }
            } else {
                val unionid = map["openid"]
                if (unionid != null) {
                    bandType = true
                    mPresenter.bindSocialPlatform(2, unionid, name)
                } else {
                    showToast("绑定QQ失败")
                }
            }
        }

        override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
            dismissLoading()
        }

        override fun onError(p0: SHARE_MEDIA?, p1: Int, throwable: Throwable?) {
            if (p1 == 2 && qqtype) {
                qqtype = false
                startAuth(2)
            } else {
                showToast(throwable?.message!!)
                dismissLoading()
            }
        }

        override fun onStart(p0: SHARE_MEDIA?) {
            showLoading()
        }
    }

    private fun startAuth(type: Int) {
        if (type == 1) {
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, mAuthListener)
        } else {
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, mAuthListener)
        }

    }

    private var qqtype = true
    private var mUserInfoData: UserInfoData? = null
    override fun showUserInfo(bean: UserInfoData) {
        mUserInfoData = bean

        item_edit_phone.setSubtitleText(bean.username)

        var p = bean.username
        if (bean.username.length == 11) {
            p = bean.username.substring(0, 3) + "****" + bean.username.substring(7, 11)
        }

        item_edit_phone.setSubtitleText(p)

        iv_image_wechat.isSelected = bean.is_bind_wx == 1
        iv_image_qq.isSelected = bean.is_bind_wx == 1

        item_edit_password.setTitleText(if (bean.is_password == 1) "修改密码" else "设置密码")

        if (bean.is_bind_wx == 1) {
            iv_image_wechat.isSelected = true
            tv_bind_wechat.isSelected = false
            tv_bind_wechat.text = getString(R.string.unbind)
        } else {
            iv_image_wechat.isSelected = false
            tv_bind_wechat.isSelected = true
            tv_bind_wechat.text = getString(R.string.bind)
        }

        if (bean.is_bind_qq == 1) {
            iv_image_qq.isSelected = true
            tv_bind_qq.isSelected = false
            tv_bind_qq.text = getString(R.string.unbind)
        } else {
            iv_image_qq.isSelected = false
            tv_bind_qq.isSelected = true
            tv_bind_qq.text = getString(R.string.bind)
        }
    }

    override fun showResult() {
        if (bandType) {
            showToast("绑定成功")
        } else {
            showToast("解绑成功")
        }

        mPresenter.getAccountSecurityData()
    }


    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        hideProgress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

}