package com.tinytiger.titi.widget.dialog


import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.FragmentManager
import com.alibaba.fastjson.JSONObject
import com.orhanobut.logger.Logger
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.QiNiuUtils
import com.tinytiger.common.utils.permission.DefaultRationale
import com.tinytiger.common.utils.permission.PermissionSetting
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.view.web.X5WebView
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.base.BaseBottomDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.event.AlbumEvent
import com.tinytiger.titi.ui.web.WebDialogInterface
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * @author zhw_luke
 * @date 2020/3/11 0011 上午 11:30
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 游戏安利文评价页
 */
class EvaluationDialog : BaseBottomDialog(),
    WebDialogInterface.onWebDialogListener {

    private var mFragmentManager: FragmentManager? = null

    companion object {
        var count=0
        fun create(manager: FragmentManager): EvaluationDialog {
            val dialog = EvaluationDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }


    private fun setFragmentManager(manager: FragmentManager): EvaluationDialog {
        mFragmentManager = manager
        return this
    }

    private var mOnAmwayListener:OnAmwayListener?=null

    private var comment_url:String?=""

    fun setGameListener(game_id: String,url:String,listener: OnAmwayListener): EvaluationDialog {
        gameId = game_id
        comment_url = url
        mOnAmwayListener = listener
        return this
    }

    fun show(): EvaluationDialog {

        show(mFragmentManager)
        return this
    }

    override fun getLayoutRes(): Int = R.layout.bottom_sheet_score

    override fun onComplete() {
        mOnAmwayListener?.onComplete()
        dismiss()
    }

    override fun onGameScore(game_id: String, score: String) {
        GameAgentUtils.setGameDetailScore(game_id,score)
    }


    override fun onStartAlbum() {
        AndPermission.with(this)
            .runtime()
            .permission(Permission.Group.STORAGE)
            .rationale(DefaultRationale())
            .onGranted {
                startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 9162)
            }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                    PermissionSetting().showSettingDialog(activity!!, permissions)
                }
            }.start()
    }


    var gameId = ""
    var x5webView: X5WebView? = null
    override fun bindView(view: View) {
        val token = SpUtils.getString(R.string.token, "")

        setHeight(-2)

        x5webView = view.findViewById(R.id.x5webView)

        val mWebInterface = WebDialogInterface(this)
        mWebInterface.listener = this
        x5webView!!.setWebInterface(mWebInterface)
        x5webView!!.loadUrl("$comment_url?token=$token&game_id=$gameId")
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        //监听返回键（禁止返回）
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true
                }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (x5webView != null) {
            x5webView?.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (x5webView != null) {
            x5webView?.onPause()
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
        if (x5webView != null) {
            x5webView?.destroy()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onShareEvent(event: AlbumEvent) {
        if (FastClickUtil.isFastClick()){
            return
        }

        LoadingUtils.getInstance().show(activity)
        QiNiuUtils.appQiniu?.uploadManager("web", event.album, object : QiNiuUtils.OnQiniuListener {
            override fun onResult(result: String, type: Boolean) {
                if (type) {
                    val jb = JSONObject()
                    jb["img"] = result

                    Logger.d(jb.toString())
                    Handler(Looper.getMainLooper()).post {
                        x5webView?.loadUrl("javascript:getImgUrl(${jb.toString()})")
                    }

                } else {
                    ToastUtils.show(activity, "上传失败")
                }
                LoadingUtils.getInstance().dismiss()
            }
        })
    }

}