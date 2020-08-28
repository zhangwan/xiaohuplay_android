package com.tinytiger.titi.ui.props.exchage

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.alibaba.fastjson.JSON
import com.king.zxing.Intents
import com.king.zxing.util.CodeUtils
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsExchangeListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.mvp.contract.props.PropsExchangeContract
import com.tinytiger.titi.mvp.presenter.props.PropsExchangePresenter
import com.umeng.socialize.utils.DeviceConfigInternal
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.props_activity_exchange.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 道具兑换,
 */
class PropsExchangeActivity : BaseActivity(), PropsExchangeContract.View {

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {

        }
    }


    override fun getSubmitExchange(bean: PropsExchangeListBean) {

        PropsExchangeCouponActivity.actionStart(this,JSON.toJSON(bean).toString(),exchange_code)
    }

    override fun getSubmit(bean: BaseBean) {

    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    companion object {

        fun actionStart(context: Context) {
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, PropsExchangeActivity::class.java)
            context.startActivity(intent)
        }

    }


    override fun layoutId(): Int = R.layout.props_activity_exchange
    private val mPresenter by lazy { PropsExchangePresenter() }


    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
       // TitiApplication().getRefWatcher(this).watch(this)
    }



    override fun initView() {
        tvTitle.centerTxt="道具兑换"
        tvTitle.setBackOnClick {
            finish()
        }

        ivIcon1.setOnClickListener {
            AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE,Permission.Group.CAMERA)
                .rationale(DefaultRationale())
                .onGranted {
                    val intent = Intent(this, CustomCaptureActivity::class.java)
                    ActivityCompat.startActivityForResult(this,intent,REQUEST_CODE_SCAN,null)
                }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                        PermissionSetting().showSettingDialog(this,permissions)
                    }
                }.start()
        }




        btn_complete.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }

            start()
        }

    }

    override fun onBackPressed() {
        mFinish()
    }
    private fun mFinish(){
        finish()
    }

    var exchange_code=""
    override fun start() {
        val sent=et_send.text.toString()
        if (sent.isEmpty()){
            showToast("请输入有效的兑换码")
            return
        }
        if (sent.length<12){
            showToast("请输入有效的兑换码")
            return
        }

        val s= sent.split("\n")
        val ss=ArrayList<String>()

        for (a in s){
            if (a.length>11){
                ss.add(a)
            }
        }

        showLoading()
        exchange_code=JSON.toJSON(ss).toString()
        mPresenter.submitExchangePreview(exchange_code)
    }


    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    val REQUEST_CODE_SCAN = 0X01
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
               REQUEST_CODE_SCAN -> {
                   exchange_code = data.getStringExtra(Intents.Scan.RESULT)

                   showLoading()
                   mPresenter.submitExchangePreview(exchange_code)
                }
            }
        }
    }


    /**
     * 生成二维码
     * @param content
     */
   /* private fun createQRCode(content: String) {
        Thread(Runnable {
            //生成二维码相关放在子线程里面
           // val logo = BitmapFactory.decodeResource(resources, R.drawable.logo)
            val bitmap = CodeUtils.createQRCode(content, 600)
            runOnUiThread {
                //显示二维码
              //  ivCode.setImageBitmap(bitmap)
            }
        }).start()
    }*/
}
