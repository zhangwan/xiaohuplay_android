package com.tinytiger.titi.ui.mine.talent


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher

import com.bumptech.glide.Glide
import com.tinytiger.common.base.BaseApp
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.talent.TalentBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.image.CropImageActivity
import com.tinytiger.common.utils.image.ImageUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.mvp.contract.mine.TalentContract

import com.tinytiger.titi.mvp.presenter.mine.TalentPresenter
import com.tinytiger.titi.widget.view.inputfilter.SizeFilterWithTextAndLetter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.talent_activity_info3.*


/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:44
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 达人认证_内部认证_资料页
 */
class TalentCertification3Activity : BaseBackActivity(), TalentContract.View {


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun getQiniuToken(qiniuTocken: String) {
        qiniutoken = qiniuTocken
        uploadQiniu(1)
    }

    override fun showResult(bean: TalentBean?) {
       // AppManager.getAppManager().finishActivity(TalentActivity::class.java)
        AppManager.getAppManager().finishActivity(TalentCertificationActivity::class.java)

        finish()
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    companion object {
        fun actionStart(context: Context,key:String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, TalentCertification3Activity::class.java)
            intent.putExtra("key", key)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.talent_activity_info3
    private val mPresenter by lazy { TalentPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

        key = intent.getStringExtra("key")
    }


    override fun initView() {
        title_view.centerTxt = "申请达人认证"
        title_view.setBackOnClick {
            finish()
        }

        editTextMessage.setFilters(arrayOf<InputFilter>(SizeFilterWithTextAndLetter(8, 8)))

        editTextMessage2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editTextMessage2.text.toString().length>18){
                    editTextMessage2.setText(editTextMessage2.text.toString().subSequence(0,18))
                    editTextMessage2.setSelection(18)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        editTextMessage3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editTextMessage2.text.toString().length>18){
                    editTextMessage2.setText(editTextMessage2.text.toString().subSequence(0,18))
                    editTextMessage2.setSelection(18)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        rlCard1.setOnClickListener {
            imageType = true
            requestPermission()
        }

        rlCard2.setOnClickListener {
            imageType = false
            requestPermission()
        }

        btn_complete.setOnClickListener {
            if (editTextMessage.text.isEmpty()) {
                showToast("请输入真实姓名")
                return@setOnClickListener
            }

            if (editTextMessage2.text.isEmpty()) {
                showToast("请输入身份证号")
                return@setOnClickListener
            }

            if (editTextMessage3.text.isEmpty()) {
                showToast("请输入认证简介")
                return@setOnClickListener
            }

            if (image1.isEmpty()) {
                showToast("请上传身份证照片")
                return@setOnClickListener
            }

            if (image2.isEmpty()) {
                showToast("请上传身份证照片")
                return@setOnClickListener
            }

            if (!images_url_1.isEmpty() && !images_url_2.isEmpty()) {
                setReport()
            }else{
                if (qiniutoken.isEmpty()) {
                    mPresenter.loadQiniuToken()
                } else {
                    uploadQiniu(1)
                }
            }
        }


    }


    override fun start() {

    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    /**
     * 权限申请
     */
    private fun requestPermission() {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .rationale(DefaultRationale())
            .onGranted {
                CropImageActivity.actionStart(this, 2, 9162)
            }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                    PermissionSetting().showSettingDialog(
                        AppManager.getAppManager().currentActivity(),
                        permissions
                    )
                }
            }
            .start()
    }

    /**
     * 图片位置选择
     */
    private var imageType = true
    /**
     * 图片地址
     * image本地
     * images_url网络
     */
    private var image1 = ""
    private var image2 = ""
    private var images_url_1 = ""
    private var images_url_2 = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 9162 && data != null) {

            val imgFile  = data.getStringExtra("path")
            if (imageType) {
                image1 = imgFile
                Glide.with(this)
                    .load(imgFile)
                    .into(ivCard1)
            } else {
                image2 = imgFile
                Glide.with(this)
                    .load(imgFile)
                    .into(ivCard2)
            }
        }
    }

    private var qiniutoken = ""

    //图片上传
    private fun uploadQiniu(index: Int) {
        if (index == 1 && image1.isEmpty()) {
            uploadQiniu(2)
            return
        }

        if (index == 2 && image2.isEmpty()) {
            setReport()
            return
        }
        var fpath = image1
        if (index == 2) {
            fpath = image2
        }


        QiNiuUtil.uploadManager(qiniutoken, "talent", fpath
        ) { result, type ->
            if (type) {
                if (index == 1) {
                    images_url_1 = result
                    uploadQiniu(2)
                } else {
                    images_url_2 = result
                    setReport()
                }
            } else {
                hideProgress()
                showToast("上传失败")
            }
        }
    }

    var key=""
    private fun setReport() {
        val real_name=editTextMessage.text.toString()
        val id_number=editTextMessage2.text.toString()
        val profile=editTextMessage3.text.toString()
        if (profile.isEmpty()) {
            showToast("请输入认证简介")
            return
        }

        mPresenter.submitMasterApply(key,real_name,id_number,images_url_1,images_url_2,profile)

    }
}
