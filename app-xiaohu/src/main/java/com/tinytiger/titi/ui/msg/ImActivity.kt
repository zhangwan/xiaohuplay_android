package com.tinytiger.titi.ui.msg


import android.Manifest
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat

import androidx.fragment.app.FragmentTransaction
import com.netease.nim.uikit.business.recent.RecentContactsCallback
import com.netease.nim.uikit.business.recent.RecentContactsFragment
import com.netease.nim.uikit.support.permission.MPermission
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.event.MsgEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.dialog.TextDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.im.session.SessionHelper
import com.tinytiger.titi.im.session.extension.AssessAttachment
import com.tinytiger.titi.im.session.extension.PostAttachment
import com.tinytiger.titi.im.session.extension.StickerAttachment
import com.tinytiger.titi.im.session.extension.SystemAttachment
import kotlinx.android.synthetic.main.activity_msg.*

import org.greenrobot.eventbus.EventBus
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2018/7/28 0028 下午 2:26
 * @Copyright 小虎互联科技
 * @since 3.3.0
 * @doc im联系人列表
 *
 */
class ImActivity : BaseBackActivity() {

    companion object {

        fun actionStart(context: Context) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
                ToastUtils.show(context, "无网络")
                return
            }
            val intent = Intent(context, ImActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun layoutId(): Int {
        return R.layout.activity_msg
    }

    override fun initView() {
        title_view.centerTxt = "消息"
        title_view.rightTxt = "一键已读"
        title_view.setRightV(View.GONE)
        title_view.setBackOnClick {
            finish()
        }
        addRecentContactsFragment()
    }

    override fun start() {

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        val unreadNum = NIMClient.getService(MsgService::class.java).getTotalUnreadCount()
        if (unreadNum>0){
            title_view.setRightV(View.VISIBLE)
        }else{
            title_view.setRightV(View.GONE)
        }
    }


   override fun onBackPressed() {
        finish()
    }


    /**
     * 基本权限管理
     */
    private val BASIC_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun mAndPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS)
        MPermission.with(this)
                .setRequestCode(100)
                .permissions(*BASIC_PERMISSIONS)
                .request()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @OnMPermissionGranted(100)
    fun onBasicPermissionSuccess() {
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS)
    }

    @OnMPermissionDenied(100)
    @OnMPermissionNeverAskAgain(100)
    fun onBasicPermissionFailed() {
       // ToastShort("未全部授权，部分功能可能无法正常运行！")
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS)
    }

    private fun addRecentContactsFragment() {
        val fragment = RecentContactsFragment()
        addFragmengt(fragment)
        fragment.setCallback(object : RecentContactsCallback {
            override fun onHeaderIndex(index: Int) {
                if (!isLoginStart()) {
                    return
                }
                when (index) {
                    1 -> {
                        CommentActivity().actionStart(this@ImActivity)
                    }
                    0 -> {
                        LikeActivity().actionStart(this@ImActivity)
                    }
                    2 -> {
                        SessionHelper.startP2PSession(this@ImActivity, getString(R.string.im_secretary))
                    }
                }
            }
            override fun onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
                mAndPermission()
            }
            override fun onUnreadCountChange(unreadCount: Int) {
                EventBus.getDefault().post(MsgEvent(unreadCount, ""))
                if (unreadCount>0){
                    title_view.setRightV(View.VISIBLE)
                }else{
                    title_view.setRightV(View.GONE)
                }
            }

            override fun onItemClick(recent: RecentContact) {
                NIMClient.getService(MsgService::class.java).clearUnreadCount(recent.contactId, SessionTypeEnum.P2P)
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                SessionHelper.startP2PSession(this@ImActivity, recent.contactId)
            }

            override fun getDigestOfAttachment(recentContact: RecentContact, attachment: MsgAttachment): String? {
                if (attachment is StickerAttachment) {
                    return "[图片]"
                } else if (attachment is SystemAttachment) {
                    return "[系统消息]"
                } else if (attachment is PostAttachment) {
                    return "[帖子消息]"
                } else if (attachment is AssessAttachment) {
                    return "[安利文消息]"
                }
                return null
            }

            override fun getDigestOfTipMsg(recent: RecentContact): String? {
                val uuids = ArrayList<String>(1)
                uuids.add(recent.recentMessageId)
                val msgs =
                    NIMClient.getService(MsgService::class.java).queryMessageListByUuidBlock(uuids)
                if (msgs != null && !msgs.isEmpty()) {
                    val msg = msgs[0]
                    val content = msg.remoteExtension
                    if (content != null && !content.isEmpty()) {
                        return content["content"] as String
                    }
                }
                return null
            }
        })

        title_view.setRightTxtColor(ContextCompat.getColor(this,R.color.color_ffcc03))
        title_view.setRightOnClick {
            val unreadNum = NIMClient.getService(MsgService::class.java).totalUnreadCount
            if (unreadNum>0){
                TextDialog.create(supportFragmentManager)
                    .setMessage("一键已读后只会更改未读消息状态，不会清除消息，确定一键已读所有未读消息吗？")
                    .setViewListener(object : TextDialog.ViewListener {
                        override fun click() {
                            fragment.setReadIm()
                            title_view.setRightV(View.GONE)
                        }
                    }).show()
            }else{
              showToast("暂无未读消息")
            }
        }
    }

    private fun addFragmengt(fragment: RecentContactsFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.messages_fragment, fragment)
        transaction.addToBackStack("RecentContactsFragment")
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

}
