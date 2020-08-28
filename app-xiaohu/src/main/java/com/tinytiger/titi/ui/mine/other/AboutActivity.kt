package com.tinytiger.titi.ui.mine.other


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.data.mine.ShareAppBean
import com.tinytiger.common.net.data.mine.UserCenterBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.mvp.contract.mine.MineContract
import com.tinytiger.titi.mvp.presenter.mine.MinePresenter
import com.tinytiger.titi.ui.web.WebActivity
import com.tinytiger.titi.widget.dialog.AppStoreDialog

import kotlinx.android.synthetic.main.activity_report.tvTitle
import kotlinx.android.synthetic.main.setting_activity_about.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 关于小虎
 */
class AboutActivity : BaseBackActivity(), View.OnLongClickListener, MineContract.View {
    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun showUserCenter(login_status: Boolean, bean: UserCenterBean.DataBean?) {

    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.tvInfo1 -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(null, "${tvInfo1.text}"))
                showToast("${tvInfo1.text},已复制到粘贴板")
            }
            else -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(null, "${tvInfo2.text}"))
                showToast("${tvInfo2.text},已复制到粘贴板")
            }
        }
        return false
    }

    /**
     *reportType 举报类型1内容 2评论
     * reportId 举报id 内容id ,评论id
     */
    fun actionStart(context: Context) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        val intent = Intent(context, AboutActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.setting_activity_about
    private val mPresenter by lazy { MinePresenter() }
    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }

    private var type=0
    override fun initView() {
        tvTitle.centerTxt = "关于小虎Hoo"
        tvTitle.setBackOnClick { finish() }

        tvInfo1.setOnLongClickListener(this)
        tvInfo2.setOnLongClickListener(this)
        tvInfo3.setOnLongClickListener(this)
        tvInfo4.setOnLongClickListener(this)

        tvUserProtocol.setOnClickListener {
            if (shareBean!=null){
                WebActivity.actionStart(this, shareBean!!.data.app_agreement_url)
            }else{
                type=2
                mPresenter.shareApp()
            }
        }

        item_privacy.setOnClickListener {
            AppStoreDialog.create(supportFragmentManager).show()
        }


        bilShare.setOnClickListener {
            if (shareBean!=null){
                showShareDialog()
            }else{
                type=1
                mPresenter.shareApp()
            }

        }
    }


    override fun start() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    private var shareBean: ShareAppBean? = null
    override fun getShareAppBean(bean: ShareAppBean) {
        shareBean = bean
        if (type==1){
            showShareDialog()
        }else if (type==2){
            WebActivity.actionStart(this, shareBean!!.data.app_agreement_url)
        }
    }

    private fun showShareDialog() {
        if (shareBean!!.data.app_download_url==null){
            return
        }

        ShareDialog.create(supportFragmentManager)
            .apply {
                class_name = "no"
                share_url = shareBean!!.data.app_download_url
                share_title = shareBean!!.data.title
                share_desc = shareBean!!.data.slogan
                share_image = shareBean!!.data.logo_url
                userId= SpUtils.getString(com.tinytiger.common.R.string.user_id, "")
            }
            .show()
    }

}
