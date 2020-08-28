package com.tinytiger.titi.ui.mine.other

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.text.Editable

import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat

import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger

import com.tinytiger.common.net.data.StringInfo
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.image.ImageUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.preference.SpUtils

import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.ReportAdapter

import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.titi.mvp.contract.mine.ReportContract
import com.tinytiger.titi.mvp.presenter.mine.ReportPresenter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_report.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 举报
 */
class ReportActivity : BaseBackActivity(), ReportContract.View {

    override fun getQiniuToken(qiniuTocken: String) {
        qiniutoken = qiniuTocken
        uploadQiniu(1)
    }

    override fun getReportData() {
        showToast("举报成功")
        finish()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    companion object {


        /**
         * reportType 举报类型1内容 2评论 3用户 4派对 5评价 6帖子
         * reportId 举报id 内容id ,评论id
         * userId 用户Id
         */
        fun actionStart(context: Context, reportType: Int, reportId: String, userId: String) {
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }

            val intent = Intent(context, ReportActivity::class.java)
            intent.putExtra("reportType", reportType)
            intent.putExtra("reportId", reportId)
            intent.putExtra("userId", userId)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.activity_report
    private val mPresenter by lazy { ReportPresenter() }
    private val mAdapter by lazy { ReportAdapter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

        reportType = intent.getIntExtra("reportType", 1)
        if (intent.hasExtra("reportId")) {
            reportId = intent.getStringExtra("reportId")
        }
        if (intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId")
        }
    }

    private var reportType = 0
    private var reportId = ""
    private var userId = ""
    //举报类型id
    private var reasonId = 0
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
    //1.举报用户 2.举报作品
    private var typeSelected = -1


    override fun initView() {
        tvTitle.centerTxt = "举报详情"
        tvTitle.setBackOnClick { finish() }
        //1=涉黄信息、2=涉及政治、3=涉军信息、4=谩骂侮辱(已舍弃)、5=垃圾广告、
        // 6=血腥暴力(已舍弃)、7=内容与标题严重不符、8=涉赌信息、9=其他

        val reportList = ArrayList<StringInfo>()
        reportList.add(StringInfo("内容涉黄", 1))
        reportList.add(StringInfo("内容涉赌", 8))
        reportList.add(StringInfo("内容涉政", 2))
        reportList.add(StringInfo("内容涉军", 3))
        reportList.add(StringInfo("垃圾广告", 5))

        recycler_view.adapter = mAdapter
        mAdapter.setNewInstance(reportList)
        setBtnType(false)

       if (reportType == 3) {
            setOpusViewGone()
        }

        isSelected(1)

        initListener()

    }

    /**
     * 隐藏作品View
     */
    private fun setOpusViewGone() {
        ivOpusBg.visibility = View.GONE
        ivOpusSelect.visibility = View.GONE
        tvOpus.visibility = View.GONE
        var mp = ViewGroup.MarginLayoutParams(
            resources.getDimensionPixelOffset(R.dimen.size15),
            resources.getDimensionPixelOffset(R.dimen.size15)
        )
        //分别是margin_top那四个属性
        mp.setMargins(
            0, 0, resources.getDimensionPixelOffset(R.dimen.size25)
            , 0
        );
        var lp = RelativeLayout.LayoutParams(mp);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL)
        ivUserBg.layoutParams = lp

        var mp1 = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //分别是margin_top那四个属性
        mp1.setMargins(
            0, 0, resources.getDimensionPixelOffset(R.dimen.size20)
            , 0
        );
        var lp1 = RelativeLayout.LayoutParams(mp1);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        lp1.addRule(RelativeLayout.CENTER_VERTICAL)
        ivUserSelect.layoutParams = lp1
    }

    /**
     * 显示作品View
     */
    private fun setOpusViewVisible() {
        ivOpusBg.visibility = View.VISIBLE
        ivOpusSelect.visibility = View.VISIBLE
        tvOpus.visibility = View.VISIBLE
    }

    /**
     * 1-用户 2-作品
     */
    private fun isSelected(index: Int) {
        typeSelected = index
        for (i in mAdapter.data){
            i.type=false
            if(i.id==7){
                mAdapter.remove(i)
            }
        }
        mAdapter.notifyDataSetChanged()
        if (index == 1) {
            ivUserSelect.visibility = View.VISIBLE
            ivOpusSelect.visibility = View.INVISIBLE
        } else {
            ivUserSelect.visibility = View.INVISIBLE
            ivOpusSelect.visibility = View.VISIBLE
            mAdapter.addData(StringInfo("内容与标题严重不符", 7))
        }
    }



    private fun initListener() {
        mAdapter.listener = object : ReportAdapter.onReportListener {
            override fun onType(type: Boolean, id: Int) {
                mHandler.postDelayed({ mAdapter.notifyDataSetChanged() }, 230)
                reasonId = id
                setBtnType(type)
                closeKeyBord(et_send)
            }
        }

        et_send.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }


            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val length = if (et_send.text.isEmpty()) 0 else et_send.text.toString().length
                tv_text_length.text = "$length/100"
            }

            override fun afterTextChanged(s: Editable) {
            }
        })


        ivIcon1.setOnClickListener {
            imageType = true
            requestPermission()
        }
        ivIcon2.setOnClickListener {
            imageType = false
            requestPermission()
        }

        btn_complete.setOnClickListener {
            if (image1.isEmpty() && image2.isEmpty()) {
                setReport()
            } else {
                if (qiniutoken.isEmpty()) {
                    mPresenter.loadQiniuToken()
                } else {
                    uploadQiniu(1)
                }
            }
        }


        ivDetele1.setOnClickListener {
            ivDetele1.visibility = View.GONE
            image1 = ""
            ivbg1.visibility = View.VISIBLE
            ivIcon1.setImageDrawable(ContextCompat.getDrawable(this, R.color.color_line))
            setSize()
        }
        ivDetele2.setOnClickListener {
            ivDetele2.visibility = View.GONE
            ivbg2.visibility = View.VISIBLE
            image2 = ""
            ivIcon2.setImageDrawable(ContextCompat.getDrawable(this, R.color.color_line))
            setSize()
        }
        ivOpusBg.setOnClickListener {
            isSelected(2)
        }
        tvOpus.setOnClickListener {
            isSelected(2)
        }
        ivUserBg.setOnClickListener {
            isSelected(1)
        }
        tvUser.setOnClickListener {
            isSelected(1)
        }
    }

    private fun setBtnType(type: Boolean) {
        btn_complete.isClickable = type
        btn_complete.isEnabled = type
        btn_complete.isSelected = type
        btn_complete.isPressed = type
    }


    override fun start() {

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

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


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
                ivDetele1.visibility = View.VISIBLE
                ivbg1.visibility = View.GONE
                Glide.with(this)
                    .load(imgFile)
                    .into(ivIcon1)
            } else {
                image2 = imgFile
                ivbg2.visibility = View.GONE
                ivDetele2.visibility = View.VISIBLE
                Glide.with(this)
                    .load(imgFile)
                    .into(ivIcon2)
            }
            setSize()
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
        var fpath1 = image1
        if (index == 2) {
            fpath1 = image2
        }

        var fpath = ImageUtil.CACHE_IMG + System.currentTimeMillis() + ".png"//压缩后图片的路径
        if (!fpath1.contains("gif")) {
            val scale = ImageUtil.compressBitmap_float(fpath1, 1000, fpath)//进行图片压缩，返回宽高比
            if (!scale) {
                fpath = fpath1
            }
        } else {
            fpath = fpath1
        }

        val fileSuffix = fpath.substring(fpath.lastIndexOf("."))
        val path = "phone" + System.currentTimeMillis() + SpUtils.getString(
            R.string.user_id,
            ""
        ) + fileSuffix

        QiNiuUtil.sUploadManager(qiniutoken, path, fpath, object : QiNiuUtil.OnQiNiuListener {
            override fun onType(type: Boolean) {
                if (type) {
                    if (index == 1) {
                        images_url_1 = path
                        uploadQiniu(2)
                    } else {
                        images_url_2 = path
                        setReport()
                    }
                } else {
                    hideProgress()
                    showToast("上传失败")
                }
            }
        })
    }

    private fun setSize() {
        var size = 0
        if (image1.isNotEmpty()) {
            size = +1
        }

        if (image2.isNotEmpty()) {
            size += 1
        }

        tvIconSzie.text = "截图留证($size/2)"
    }

    private fun setReport() {
        val reason = et_send.text.trim().toString()
        if (typeSelected == 1) {
            reportId = userId
            reportType=3
        }
        mPresenter.report(reportType, reportId, reasonId, reason, images_url_1, images_url_2)
    }
    //---- 6------- 525-------- 4458------

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        mEditText.clearFocus()
        mEditText.isCursorVisible = false
    }
}
