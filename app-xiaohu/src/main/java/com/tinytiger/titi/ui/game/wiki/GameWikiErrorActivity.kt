package com.tinytiger.titi.ui.game.wiki

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.QiNiuUtil
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.SelectImageAdapter
import com.tinytiger.titi.data.circle.ImageBean
import com.tinytiger.titi.event.PostEvent
import com.tinytiger.titi.mvp.contract.game.GameWikiErrorContract
import com.tinytiger.titi.mvp.presenter.game.GameWikiErrorPresenter
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.game_activity_wiki_err_recovery.*
import org.greenrobot.eventbus.EventBus

/**
 * @author lmq001
 * @date 2020/06/01 11:10
 * @copyright 小虎互联科技
 * @doc 词条纠错页
 */
class GameWikiErrorActivity : BaseBackActivity(), GameWikiErrorContract.View {

    private val QiniuList = ArrayList<AlbumFile>()
    private var qiniutoken = ""
    private var content_id = ""

    private val mPresenter by lazy { GameWikiErrorPresenter() }
    private val mAdapter by lazy { SelectImageAdapter() }

    companion object {
        fun actionStart(context: Context, content_id: String) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, GameWikiErrorActivity::class.java)
            intent.putExtra("wiki_Id", content_id)
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int {
        return R.layout.game_activity_wiki_err_recovery
    }

    override fun initData() {
        mPresenter.attachView(this)
        content_id = intent.getStringExtra("wiki_Id")
    }

    override fun initView() {
        //点击返回
        iv_back.setOnClickListener {
            finish()
        }
        //点击提交
        tvCommit.setOnClickListener {
            clickCommit()
        }

        rv_photo.layoutManager = GridLayoutManager(this, 4)
        rv_photo.adapter = mAdapter
        mAdapter.addData(ImageBean(1))
        mAdapter.setLimit(3)
        mAdapter.addChildClickViewIds(R.id.ivIcon1)
        mAdapter.setOnItemChildClickListener { _, view, _ ->
            when (view.id) {
                R.id.ivIcon1 -> {
                    showImagePicker()
                }
            }
        }
    }

    override fun start() {

    }

    private fun showImagePicker() {
        var size = 3
        for (i in mAdapter.data) {
            if (i.type == 0) {
                size--
            }
        }
        if (size <= 0) {
            return
        }

        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .rationale(DefaultRationale())
            .onGranted {
                Album.image(this) // Image selection.
                    .multipleChoice()
                    .camera(true)
                    .columnCount(3)
                    .selectCount(size)
                    .checkedList(ArrayList())
                    .widget(
                        Widget.newLightBuilder(this).statusBarColor(Color.WHITE)
                            .title("选择图片")
                            .toolBarColor(Color.WHITE)
                            .mediaItemCheckSelector(Color.WHITE, ContextCompat.getColor(this, R.color.color_ffcc03))
                            .bucketItemCheckSelector(Color.GRAY, ContextCompat.getColor(this, R.color.color_ffcc03))
                            .build()
                    )
                    .filterSize { it > 5 * 1024 * 1024 }
                    //.filterMimeType() // Filter file format.
                    //.afterFilterVisibility() // Show the filtered files, but they are not available.
                    .onResult(object : Action<ArrayList<AlbumFile>> {
                        override fun onAction(result: ArrayList<AlbumFile>) {
                            QiniuList.clear()
                            QiniuList.addAll(result)
                            uploadQiniu()
                        }
                    })
                    .onCancel(object : Action<String?> {
                        override fun onAction(result: String) {

                        }
                    })
                    .start()
            }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                    PermissionSetting().showSettingDialog(this, permissions)
                }
            }.start()
    }

    //图片上传
    private fun uploadQiniu() {
        if (qiniutoken.isEmpty()) {
            mPresenter.loadQiniuToken()
            return
        }

        if (QiniuList.size < 1) {
            //上传结束
            hideProgress()
            return
        }

        val fpath = QiniuList[0].path

        QiNiuUtil.uploadManager(qiniutoken, "circle", fpath) { result, type ->
            if (type) {
                //  mDraft = true
                mAdapter.setList(ImageBean(result))
                QiniuList.removeAt(0)
                uploadQiniu()
            } else {
                hideProgress()
                showToast("上传失败")
            }
        }
    }

    /**
     * 点击提交
     */
    private fun clickCommit() {
        when {
            et_content.text.isEmpty() -> ToastUtils.show(this, "请完善内容信息")
            else -> {
                TextDialog.create(supportFragmentManager)
                    .setMessage("确定提交吗?")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            mPresenter.commitEntryError(content_id, et_content.text.toString(), getImagesPath())
                        }
                    }).show()
            }
        }
    }

    /**
     * 获取所有图片路径
     */
    private fun getImagesPath(): String {
        val imgs = ArrayList<String>()
        for (i in mAdapter.data) {
            if (i.url.isNotEmpty()) {
                imgs.add(i.url)
            }
        }

        var img_url = ""
        if (imgs.size > 0) {
            img_url = JSON.toJSONString(imgs)
        }
        return img_url
    }

    override fun getQiniuToken(qiniuTocken: String) {
        qiniutoken = qiniuTocken
        uploadQiniu()
    }

    override fun commitSuccess() {
        showToast("提交成功")
//        EventBus.getDefault().post(PostEvent(type))
        finish()
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

}
