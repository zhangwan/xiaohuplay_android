package com.tinytiger.titi.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.logger.Logger
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.UserInfo
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.RefreshView
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.LoadingUtils
import com.tinytiger.common.widget.dialog.TextDialog

import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.GameCommentTwoAdapter
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.im.config.preference.Preferences
import com.tinytiger.titi.mvp.contract.gametools.GameCommentContract
import com.tinytiger.titi.mvp.presenter.gametools.GameCommentPresenter
import com.tinytiger.titi.ui.mine.other.ReportActivity
import com.tinytiger.titi.utils.CheckUtils
import com.tinytiger.titi.widget.popup.User2Popup
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author zhw_luke
 * @date 2020/5/11 0011 下午 5:30
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 公告
 */
class FullAnnouncementDialog : BottomSheetDialogFragment() {

    private var mBehavior: BottomSheetBehavior<*>? = null
    var content=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_announcement, null)
        dialog.setContentView(view)

        view.findViewById<TextView>(R.id.tv_send).text=content

        mBehavior = BottomSheetBehavior.from(view.parent as View)
        mBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss()
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            }

            override fun onSlide(view: View, v: Float) {

            }
        })

        view.findViewById<ImageView>(R.id.iv_close_left).setOnClickListener { dismiss() }

        val layoutParams = view.layoutParams
        layoutParams.height = (context!!.resources.displayMetrics.heightPixels * 0.6).toInt()
        view.layoutParams = layoutParams

        return dialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }



}