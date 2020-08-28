package com.tinytiger.common.widget.dialog


import android.content.DialogInterface
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.R
import com.tinytiger.common.widget.dialog.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_text.*


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description:  通用文本框
*/
class TextDialog : BaseDialog() {

    var messageText: String = ""
    var title: String = "提示"
    var cancelText: String = "取消"
    var confirmText: String = "确定"

    //是否显示标题
    var showTitle = false

    //标题字体颜色
    var titleColor = R.color.gray99

    //内容字体颜色
    var messageColor = R.color.gray33

    private var isShowCancelButton = true
    private var isShowConfirmButton = true

    private var isShowTitle = false

    private var mFragmentManager: FragmentManager? = null


    companion object {
        fun create(manager: FragmentManager): TextDialog {
            val dialog = TextDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }


    override fun getLayoutRes(): Int = R.layout.dialog_text

    override fun bindView(v: View?) {
        tv_title.visibility = if (showTitle) View.VISIBLE else View.GONE

        if (!title.isNullOrEmpty()) {
            tv_title.text = title
        }
        if (!messageText.isNullOrEmpty()) {
            tv_message.text = messageText
        }

        tv_title.setTextColor(ContextCompat.getColor(activity!!, titleColor))
        tv_message.setTextColor(ContextCompat.getColor(activity!!, messageColor))

        tv_sure.text = confirmText
        tv_cancel.text = cancelText

        tv_cancel.setOnClickListener {
            dismiss()
            mDismissListener?.onDismiss()
        }

        tv_sure.setOnClickListener {
            mViewListener?.click()
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mDismissListener?.onDismiss()
    }


    fun setFragmentManager(manager: FragmentManager): TextDialog {
        mFragmentManager = manager
        return this
    }

    fun setShowButton(isCancel: Boolean, isConfirm: Boolean): TextDialog {
        this.isShowConfirmButton = isConfirm
        this.isShowCancelButton = isCancel
        return this
    }

    fun isCancelOutside(isCancel: Boolean): TextDialog {
        this.mIsCancelOutside = isCancel
        return this
    }

    fun isShowTitle(isShow: Boolean): TextDialog {
        this.isShowTitle = isShow
        return this
    }

    fun setMessage(message: String): TextDialog {
        this.messageText = message
        return this
    }

    fun show(): BaseDialog {
        show(mFragmentManager)
        return this
    }

    fun setViewListener(listener: ViewListener): TextDialog {
        this.mViewListener = listener
        return this
    }

    private var mDismissListener: DismissListener? = null
    fun setDismissListener(listener: DismissListener): TextDialog {
        this.mDismissListener = listener
        return this
    }

    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click()
    }


    interface DismissListener {
        fun onDismiss()
    }

}
