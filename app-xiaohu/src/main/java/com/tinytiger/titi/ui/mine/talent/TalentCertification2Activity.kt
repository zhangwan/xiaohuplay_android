package com.tinytiger.titi.ui.mine.talent


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.view.View
import com.bumptech.glide.Glide
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.talent.TalentBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.image.ImageUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.mine.TalentContract
import com.tinytiger.titi.mvp.presenter.mine.TalentPresenter
import com.tinytiger.titi.widget.view.inputfilter.SizeFilterWithTextAndLetter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.talent_activity_info2.*


/**
 *
 * @author zhw_luke
 * @date 2020/5/5 0005 下午 4:44
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 达人认证_外部认证
 */
class TalentCertification2Activity : BaseBackActivity(), TalentContract.View {


    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun getQiniuToken(qiniuTocken: String) {
        qiniutoken = qiniuTocken
        uploadQiniu(1)
    }

    override fun showResult(bean: TalentBean?) {
        //AppManager.getAppManager().finishActivity(TalentActivity::class.java)
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
            val intent = Intent(context, TalentCertification2Activity::class.java)
            intent.putExtra("key", key)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.talent_activity_info2
    private val mPresenter by lazy { TalentPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
        setWindowFeature()
        key = intent.getStringExtra("key")
    }


    override fun initView() {
        iv_back.setOnClickListener { finish() }
        editTextMessage.setFilters(arrayOf<InputFilter>(SizeFilterWithTextAndLetter(8, 8)))


        ivDelete.setOnClickListener {
            ivDelete.visibility = View.GONE
            ivIcon.visibility = View.GONE
            ivIcon_1.visibility = View.VISIBLE
            image1=""
        }
        ivDelete2.setOnClickListener {
            ivDelete2.visibility = View.GONE
            ivIcon2.visibility = View.GONE
            ivIcon2_1.visibility = View.VISIBLE
            image2=""
        }

        ivIcon_1.setOnClickListener {
            imageType = true
            requestPermission()
        }
        ivIcon.setOnClickListener {
            imageType = true
            requestPermission()
        }

        ivIcon2_1.setOnClickListener {
            imageType = false
            requestPermission()
        }
        ivIcon2.setOnClickListener {
            imageType = false
            requestPermission()
        }
        btn_complete.setOnClickListener {
            if (editTextMessage.text.isEmpty()) {
                showToast("请输入外站站名")
                return@setOnClickListener
            }

            if (editTextMessage2.text.isEmpty()) {
                showToast("请输入外站昵称")
                return@setOnClickListener
            }

            if (editTextMessage3.text.isEmpty()) {
                showToast("请输入认证简介")
                return@setOnClickListener
            }

            if (image1.isEmpty() && image2.isEmpty()) {
                showToast("请上传认证图片")
                return@setOnClickListener
            }

            if (images_url_1.isNotEmpty() || images_url_2.isNotEmpty()) {
                setReport()
            }else{
                if (qiniutoken.isEmpty()) {
                    mPresenter.loadQiniuToken()
                } else {
                    uploadQiniu(1)
                }
            }
        }
        setBtnType(true)
    }
    private fun setBtnType(type: Boolean) {
        btn_complete.isClickable = type
        btn_complete.isEnabled = type
        btn_complete.isSelected = type
        btn_complete.isPressed=type
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
                startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 9162)
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
            val imgFile = ImageUtil.getImageAbsolutePath(this, data.data)
            var tpe = false
            if (imgFile.contains("png")) {
                tpe = true
            } else if (imgFile.contains("jpg")) {
                tpe = true
            } else if (imgFile.contains("jpeg")) {
                tpe = true
            }
            if (!tpe) {
                showToast("图片格式不正确,请上传png,jpg,jpeg等格式图片")
                return
            }

            if (imageType) {
                image1 = imgFile
                ivDelete.visibility = View.VISIBLE
                ivIcon.visibility = View.VISIBLE
                ivIcon_1.visibility = View.GONE
                Glide.with(this)
                    .load(imgFile)
                    .into(ivIcon)
            } else {
                image2 = imgFile
                ivDelete2.visibility = View.VISIBLE
                ivIcon2.visibility = View.VISIBLE
                ivIcon2_1.visibility = View.GONE
                Glide.with(this)
                    .load(imgFile)
                    .into(ivIcon2)
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
        val site_name=editTextMessage.text.toString()
        if (site_name.isEmpty()) {
            showToast("请输入外站站名")
            return
        }

        val external_name=editTextMessage2.text.toString()
        if (external_name.isEmpty()) {
            showToast("请输入外站昵称")
            return
        }
        val profile=editTextMessage3.text.toString()
        if (profile.isEmpty()) {
            showToast("请输入认证简介")
            return
        }

        mPresenter.submitMasterApply1(key,external_name,site_name,images_url_1,images_url_2,profile)

    }
}
