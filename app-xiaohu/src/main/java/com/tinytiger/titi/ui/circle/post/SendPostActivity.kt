package com.tinytiger.titi.ui.circle.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.postsend.DraftDetailBean
import com.tinytiger.common.net.data.circle.postsend.DraftListBean
import com.tinytiger.common.net.data.circle.postsend.FriendBean
import com.tinytiger.common.net.data.circle.postsend.SelectTopicBean
import com.tinytiger.common.utils.*
import com.tinytiger.common.utils.QiNiuUtils.OnQiniuListener
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.FriendAdapter
import com.tinytiger.titi.adapter.circle.post.SelectImageAdapter
import com.tinytiger.titi.adapter.circle.post.TopicAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.data.circle.ImageBean
import com.tinytiger.titi.event.PostEvent
import com.tinytiger.titi.mvp.contract.circle.CirclesPost1Contract
import com.tinytiger.titi.mvp.presenter.circle.CirclesPost1Presenter
import com.tinytiger.titi.ui.video.VideoActivity
import com.tinytiger.titi.utils.CheckUtils
import com.tinytiger.titi.utils.UIHelper
import com.tinytiger.titi.utils.image.AlbumHelper
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.AlbumFile
import kotlinx.android.synthetic.main.circler_activity_sendpost.*
import org.greenrobot.eventbus.EventBus

/**
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 4:26
 * @Copyright 小虎互联科技
 * @doc 发布帖子
 */
class SendPostActivity : BaseActivity(), CirclesPost1Contract.View {

    private val mPresenter by lazy { CirclesPost1Presenter() }
    private val mAdapter by lazy { SelectImageAdapter() }
    private val mAdapterTopic by lazy { TopicAdapter() }
    private val mAdapterFriend by lazy { FriendAdapter() }

    // 发帖类型 -1 动态帖 1问答贴
    private var postType = -1

    // 底部按钮 -1点击返回 0-选择图片，1-选择视频，2-更换封面
    private var chooseType = 0

    // 是否是草稿 (必填) 1:是 -1:否
    private var is_draft = 1

    private var mDraft = false
    private var circleType = false
    private var post_draft_id = 0
    private var circleId = ""
    private var circleName = ""
    private var moduleId = ""
    private var moduleName = ""
    private val QiniuList = ArrayList<AlbumFile>()
    private var QiniuVideo: AlbumFile? = null
    private var coverUrl = ""
    private var videoUrl = ""
    private var video_length = ""
    private var title_content = ""
    private var imgs: ArrayList<String>? = null

    companion object {
        fun actionStart(context: Context) {
            actionStart(context, "", "")
        }

        fun actionStart(context: Context, circleId: String, circleName: String) {
            if (FastClickUtil.isFastClickTiming()) return
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            val intent = Intent(context, SendPostActivity::class.java)
            if (circleId.isNotEmpty()) {
                intent.putExtra("circleId", circleId)
                intent.putExtra("circleName", circleName)
            }
            context.startActivity(intent)
        }
    }

    override fun layoutId(): Int = R.layout.circler_activity_sendpost

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun initView() {
        initAdapter()
        initListener()
        if (intent.hasExtra("circleId")) {
            circleType = true
            circleId = intent.getStringExtra("circleId")
            circleName = intent.getStringExtra("circleName")
            tvCircleName.text = "$circleName/"
            mAdapterFriend.circleId = circleId
        }
        mAdapterTopic.mActivity = this
        mAdapterTopic.addData(SelectTopicBean(0))
        mAdapterFriend.mActivity = this
        val lp = iv_video_pic!!.layoutParams
        lp.height =
            ((ScreenUtil.getScreenWidth() - Dp2PxUtils.dip2px(this, 30)) * 150 / 345f).toInt()
        iv_video_pic?.layoutParams = lp
    }

    private fun initListener() {
        iv_back.setOnClickListener { onBackPressed() }
        rlCircle.setOnClickListener {
            if (circleType) {
                SelectModuleActivity.actionStart(this, circleId)
            } else {
                SelectCirclerActivity.actionStart(this)
            }
        }
        //点击发送
        tvSend.setOnClickListener { sendPos() }
        //点击草稿
        ivDraft.setOnClickListener { DraftActivity.actionStart(this) }
        //点击视频
        iv_video_pic.setOnClickListener {
            if (videoUrl.isNotEmpty()) VideoActivity.actionStart(this, videoUrl)
        }
        //添加标题
        ll_add_title.setOnClickListener {
            showPostTitle(
                et_title.visibility == View.VISIBLE,
                et_title.text.toString(), et_send.text.toString()
            )
        }
        //选图片
        ivChoosePic.setOnClickListener { openGallery(0) }
        //选视频
        ivChooseVideo.setOnClickListener { openGallery(1) }
        //点击更换封面
        tv_replace_cover.setOnClickListener { openGallery(2) }
        //点击继续上传
        pb_video.setOnClickListener {
            QiNiuVideoUtils.appQiniu?.setCancel(true)
            mHandler.postDelayed({
                QiNiuVideoUtils.appQiniu?.setCancel(false)
                uploadQiniuVideoInfo(QiniuVideo, breakUpload = true)
            }, 300)
        }
        //点击删除视频
        ivDeleteVideo.setOnClickListener { clickDeleteVideo() }
    }

    private fun clickDeleteVideo() {
        mDraft = true
        clearVideoInfo()
        showPicVideoVisible(picVisible = true, videoVisible = false)
        setPicVideoEnable(picEnable = true, videoEnable = true)
    }

    /**
     * 清空视频信息，且停止上传
     */
    private fun clearVideoInfo() {
        QiniuVideo = null
        coverUrl = ""
        videoUrl = ""
        video_length = ""
        if (QiNiuVideoUtils.appQiniu != null) {
            QiNiuVideoUtils.appQiniu?.setCancel(true)
        }
    }

    private fun showPostTitle(titleVisible: Boolean, title: String, content: String) {
        if (titleVisible) {
            et_title.visibility = View.GONE
            iv_add_title_left.setImageResource(R.mipmap.ic_post_add_title)
            tv_add_title.text = "添加标题"
            title_content = title
            et_title.setText("")
            et_title.clearFocus()
            et_send.requestFocus()
            et_send.setSelection(et_send.text.toString().length)
        } else {
            et_title.visibility = View.VISIBLE
            tv_add_title.text = "取消标题"
            iv_add_title_left.setImageResource(R.mipmap.ic_post_cancel_title)
            et_title.setText(title_content)
            et_title.setSelection(et_title.text.toString().length)
            et_title.requestFocus()
            et_send.clearFocus()
        }
        et_send.addTextChangedListener { mDraft = et_send.text.toString() != content }
        et_title.addTextChangedListener { mDraft = et_title.text.toString() != title }
    }

    private fun initAdapter() {
        rvImage.layoutManager = GridLayoutManager(this, 4)
        rvImage.adapter = mAdapter
        UIHelper().initLayoutManager(this, rvCircle)
        rvCircle.adapter = mAdapterTopic
        mAdapterTopic.tvCirclerNum = tvCirclerNum
        UIHelper().initLayoutManager(this, rvInvite)
        rvInvite.adapter = mAdapterFriend
        mAdapterFriend.tvCirclerNum = tvCircleFiend

        mAdapter.addChildClickViewIds(R.id.ivIcon1)
        mAdapter.setOnItemDeleteListener(object : SelectImageAdapter.OnItemDeleteListener {
            override fun onItemDelete(item: ImageBean) {
                mDraft = true
                setPicVideoEnable(picEnable = true, videoEnable = (mAdapter.itemCount == 0))
            }
        })
        mAdapter.setOnItemChildClickListener { _, view, _ ->
            when (view.id) {
                R.id.ivIcon1 -> openGallery(0)
            }
        }
    }

    override fun start() {}

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            mDraft = true
            when (requestCode) {
                90 -> {
                    val bean = SelectTopicBean()
                    bean.id = data.getIntExtra("topicId", 0)
                    bean.name = data.getStringExtra("topicName")
                    mAdapterTopic.addTopicBean(bean)
                }
                91 -> {
                    circleId = data.getStringExtra("CircleId")
                    circleName = data.getStringExtra("CircleName")
                    moduleId = data.getStringExtra("ModuleId")
                    moduleName = data.getStringExtra("ModuleName")
                    postType = data.getIntExtra("ModuleType", -1)
                    setCircleType()
                }
                92 -> {
                    moduleId = data.getStringExtra("ModuleId")
                    moduleName = data.getStringExtra("ModuleName")
                    postType = data.getIntExtra("ModuleType", -1)
                    setCircleType()
                }
                93 -> mAdapterFriend.setlist(data.getParcelableArrayListExtra<FriendBean>("FriendBean"))
                94 -> {
                    post_draft_id = data.getIntExtra("draft", 0)
                    mPresenter.draftDetail("" + post_draft_id)
                }
            }
        }
    }

    private fun setCircleType() {
        tvCircleName.text = if (circleName.isNotEmpty()) "$circleName/" else "选择游戏圈子/"
        tvCircle1Name.text = if (moduleName.isNotEmpty()) moduleName else "请选择子模块"
        if (postType == 1) {
            llInvite.visibility = View.VISIBLE
            mAdapterFriend.circleId = circleId
            mAdapterFriend.data.clear()
            mAdapterFriend.notifyDataSetChanged()
            mAdapterFriend.addData(FriendBean(0))
        } else {
            llInvite.visibility = View.GONE
        }
    }

    /**
     * 获取本地图片，视频
     * @param openType 0-选择图片，1-选择视频，2-更换封面
     */
    private fun openGallery(openType: Int) {
        chooseType = openType
        AlbumHelper().openGallery(
            this, openType, 9 - mAdapter.data.size, Action<ArrayList<AlbumFile>> { result ->
                if (result.size > 0) {
                    QiniuVideo = result[0]
                    video_length = (QiniuVideo!!.duration / 1000).toInt().toString()
                }
                QiNiuVideoUtils.appQiniu?.setCancel(true)
                mHandler.postDelayed({
                    QiNiuVideoUtils.appQiniu?.setCancel(false)
                    uploadQiniuVideoInfo(QiniuVideo)
                }, 300)
            }, Action<ArrayList<AlbumFile>> { result ->
                if (openType == 0) {
                    QiniuList.clear()
                    QiniuList.addAll(result)
                    uploadQiniuPic(false, QiniuList[0])
                } else if (result.size > 0) { //上传封面
                    coverUrl = ""
                    uploadQiniuPic(true, result[0])
                }
            })
    }

    override fun sendPost(type: Int, is_draft: Int, detail: PostBean?) {
        if (is_draft != 1) {
            showToast("发布成功")
            //发布时如果有视频，则需要给其他页面发送本页面数据消息
            if (coverUrl.isNotEmpty() || videoUrl.isNotEmpty()) {
                val data = UIHelper().composePostBean(
                    detail, circleName, moduleName, imgs!!, circleType, mAdapterTopic.data
                )
                EventBus.getDefault().post(PostEvent(type, data))
            } else {    //发布时如果无视频，直接给其他页面发送消息，不携带数据
                EventBus.getDefault().post(PostEvent(type))
            }
            CircleAgentUtils.setCirclePost(circleId)
        }
        finish()
    }

    override fun getDrafts(bean: DraftListBean) {}

    override fun getDraftDetail(bean: DraftDetailBean) {
        mDraft = false
        postType = bean.data.type
        if (bean.data.circle_name.isNotEmpty()) {
            circleId = bean.data.circle_id
            circleName = bean.data.circle_name
            tvCircleName.text = "$circleName/"
            if (bean.data.modular_name.isNotEmpty()) {
                moduleId = bean.data.modular_id
                moduleName = bean.data.modular_name
                tvCircle1Name.text = "$moduleName"
            }
            mAdapterFriend.circleId = circleId
        }
        et_send.setText(bean.data.content)
        //显示标题部分显示隐藏
        title_content = bean.data.title
        showPostTitle(bean.data.title.isEmpty(), title_content, bean.data.content)
        //显示图片，视频
        mAdapter.data.clear()
        if (bean.data.img_url != null && bean.data.img_url.size > 0) {
            clearVideoInfo()
            showPicVideoVisible(picVisible = true, videoVisible = false)
            for (i in bean.data.img_url) {
                mAdapter.setList(ImageBean(i))
            }
        } else if (bean.data.cover_url != null && bean.data.cover_url.isNotEmpty()) {
            coverUrl = bean.data.cover_url
            videoUrl = bean.data.video_url
            video_length = bean.data.video_length
            showPicVideoVisible(false, true, true, false)
            iv_video_pic.isEnabled = true
            GlideUtil.loadImg(iv_video_pic, bean.data.cover_url)
        } else {
            showPicVideoVisible(picVisible = false, videoVisible = false)
        }
        //显示圈子
        UIHelper().showTopicData(bean, mAdapterTopic)
        setCircleType()
        if (bean.data.type == 1 && bean.data.user_ids != null && bean.data.user_ids.size > 0)
            mAdapterFriend.setlist(bean.data.user_ids)
        //有视频时，选图片按钮不可点击；有图片或有视频时，选视频按钮不可点击
        setPicVideoEnable(!fl_video.isVisible, mAdapter.itemCount == 0 && !fl_video.isVisible)
    }

    /**
     * picVisible-图片预览可见性 videoVisible-视频预览可见性 coverVisible-封面可见性 progressVisible-进度条可见性
     */
    private fun showPicVideoVisible(
        picVisible: Boolean, videoVisible: Boolean,
        coverVisible: Boolean = false, progressVisible: Boolean = false
    ) {
        rvImage.visibility = if (picVisible) View.VISIBLE else View.GONE
        fl_video.visibility = if (videoVisible) View.VISIBLE else View.GONE
        tv_replace_cover.visibility = if (videoVisible && coverVisible) View.VISIBLE else View.GONE
        fr_progress.visibility = if (videoVisible && progressVisible) View.VISIBLE else View.GONE
    }

    /**
     * 设置 picEnable-选图片按钮，videoEnable-选视频按钮 是否可以点击
     */
    private fun setPicVideoEnable(picEnable: Boolean, videoEnable: Boolean) {
        ivChoosePic.isEnabled = picEnable
        ivChoosePic.setImageResource(
            if (picEnable) R.mipmap.ic_post_pic_select else R.mipmap.ic_post_pic_unselect
        )
        ivChooseVideo.isEnabled = videoEnable
        ivChooseVideo.setImageResource(
            if (videoEnable) R.mipmap.ic_post_video_select else R.mipmap.ic_post_video_unselect
        )
    }

    /**
     * 上传图片
     * @param isCover 是否上传封面/普通图片  true-封面 false-普通图片
     * @param isCommit 是否点击发布按钮 true-点击后上传封面后再发布 false-没有点击发布
     */
    private fun uploadQiniuPic(
        isCover: Boolean, ablbumFile: AlbumFile?, isCommit: Boolean = false
    ) {
        if (ablbumFile == null) return
        showPicVideoVisible(picVisible = !isCover, videoVisible = isCover)

        val path = if (isCommit) ablbumFile.thumbPath else ablbumFile.path
        QiNiuUtils.appQiniu?.uploadManager("circle", path, object : OnQiniuListener {
            override fun onResult(result: String, success: Boolean) {
                if (success) {
                    mDraft = true
                    if (isCover) {  //上传封面
                        hideProgress()
                        coverUrl = result
                        if (!isFinishing) GlideUtil.loadLocalImg(iv_video_pic, path)
                        showPicVideoVisible(false, true, true, false)
                        //点击发布按钮，有视频时上传封面成功后，再发布帖子
                        if (isCommit) sendPos()

                    } else {    //上传普通图片
                        mAdapter.setList(ImageBean(result))
                        QiniuList.removeAt(0)
                        setPicVideoEnable(
                            picEnable = mAdapter.data.size != 9, videoEnable = false
                        )
                        if (QiniuList.size > 0) {   //迭代上传普通图片
                            uploadQiniuPic(isCover, QiniuList[0])
                        } else {    //上传普通图片结束(有视频时停止上传，且清空处理)
                            hideProgress()
                            clearVideoInfo()
                        }
                    }
                } else {
                    runOnUiThread {
                        hideProgress()
                        if (isCover) {
                            showToast(if (is_draft == -1) "发布失败" else "保留失败")
                        } else {
                            showToast("上传失败")
                        }
                    }
                }
            }
        })
    }

    /**
     * 上传视频
     * @param breakUpload 是否断点续传 true-是 false-否（默认）
     */
    private fun uploadQiniuVideoInfo(ablbumFile: AlbumFile?, breakUpload: Boolean = false) {
        if (ablbumFile == null) return
        showPicVideoVisible(false, true, false, true)
        setPicVideoEnable(picEnable = false, videoEnable = false)
        videoUrl = ""
        iv_video_pic.isEnabled = false
        GlideUtil.loadLocalImg(iv_video_pic, ablbumFile.thumbPath)

        QiNiuVideoUtils.appQiniu?.uploadManager(
            "circle", ablbumFile.path, object : QiNiuVideoUtils.OnQiniuListener {
                override fun onResult(result: String, success: Boolean) {
                    hideProgress()
                    handleVideoResult(success, result)
                }

                override fun onProgress(key: String, percent: Double) {
                    pb_video.progress = (percent * 100).toInt()
                    if (percent == 1.0) {
                        tv_video_progress.text = "上传成功"
                    } else {
                        pb_video.isEnabled = false
                        tv_video_progress.text = "上传中（${pb_video.progress}%）"
                        fr_progress.visibility = View.VISIBLE
                    }
                }
            }, breakUpload
        )
    }

    private fun handleVideoResult(success: Boolean, result: String) {
        if (success) {
            mDraft = true
            videoUrl = result
            mHandler.postDelayed({
                showPicVideoVisible(false, true, true, false)
                setPicVideoEnable(picEnable = false, videoEnable = false)
                iv_video_pic.isEnabled = true
                pb_video.isEnabled = true
            }, 1000)
        } else {
            pb_video.isEnabled = true
            tv_video_progress.text = "网络不可用，点击继续上传"
        }
    }

    override fun onBackPressed() {
        if (!mDraft && post_draft_id > 0) {
            QiNiuVideoUtils.appQiniu?.setCancel(true)
            finish()
            return
        }
        var hasDraft = false
        val title = et_title.text.toString().trim()
        val content = et_send.text.toString().trim()
        //判断是否有内容，标题，图片，视频，封面
        if (content.isNotEmpty() || title.isNotEmpty() ||
            (fl_video.isVisible && videoUrl.isNotEmpty())||
            (fl_video.isVisible && coverUrl.isNotEmpty())) {
            hasDraft = true
        }
        if (fl_video.isVisible && videoUrl.isEmpty()) {
            hasDraft = false
        }
        for (i in mAdapter.data) {
            if (i.url.isNotEmpty()) hasDraft = true
        }
        if (hasDraft) {
            TextDialog.create(supportFragmentManager).setMessage("帖子尚未发布，退出前是否保留内容至草稿箱？？")
                .setViewListener(object : TextDialog.ViewListener {
                    override fun click() {
                        chooseType = -1
                        sendPos()
                    }
                }).setDismissListener(object : TextDialog.DismissListener {
                    override fun onDismiss() {
                        QiNiuVideoUtils.appQiniu?.setCancel(true)
                        finish()
                    }
                }).show()
        } else {
            QiNiuVideoUtils.appQiniu?.setCancel(true)
            finish()
        }
    }

    /**
     * 上传帖子数据
     */
    private fun sendPos() {
        val postTitle = et_title.text.toString().trim()
        val content = et_send.text.toString().trim()
        if (postTitle.isNotEmpty() && CheckUtils.checkLocalAntiSpam(postTitle)) {
            showToast("标题含有敏感词汇")
        } else if (content.isNotEmpty() && CheckUtils.checkLocalAntiSpam(content)) {
            showToast("文字含有敏感词汇")
        } else if (fl_video.isVisible && fr_progress.isVisible && (pb_video.progress in 1..99)) {
            if (chooseType == 1) showToast("视频正在上传")
        } else if (coverUrl.isEmpty() && videoUrl.isNotEmpty()) {
            //有视频但是没有封面，先上传封面再发布
            uploadQiniuPic(true, QiniuVideo, true)
        } else {
            is_draft = if (chooseType == -1) 1 else -1
            val postParam = UIHelper().composeAddPostParam(
                postType, mAdapterTopic.data, mAdapter.data, mAdapterFriend.data
            )
            imgs = postParam.img_url
            var img_url = ""
            if (imgs!!.size > 0) img_url = JSON.toJSONString(imgs)

            mPresenter.addPost(
                postType, is_draft, post_draft_id, postParam.user_id, postParam.circle_id,
                postParam.circle_name, img_url, circleId, moduleId, content,
                coverUrl, videoUrl, video_length, postTitle
            )
        }
    }
}