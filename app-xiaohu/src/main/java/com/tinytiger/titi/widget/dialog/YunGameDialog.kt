package com.tinytiger.titi.widget.dialog


import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.widget.dialog.base.BaseDialog
import com.tinytiger.titi.R

import kotlinx.android.synthetic.main.dialog_yun_text.*


/**
 *
 * @author zhw_luke
 * @date 2020/7/6 0006 上午 11:13
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 云游戏
 */
class YunGameDialog : BaseDialog() {

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): YunGameDialog {
            val dialog = YunGameDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }


    override fun getLayoutRes(): Int = R.layout.dialog_yun_text

    var messageText: String = ""
    var cancelText: String = "取消"
    var confirmText: String = "确定"

    /**
     * 0显示8影藏4不可取消5 不可取消-弹出评价框
     */
    var cancelType=0

    override fun bindView(v: View?) {
        tv_sure.text = confirmText
        tv_message.text = messageText
        when (cancelType) {
            1 -> {
                vView.visibility=View.VISIBLE
                tv_cancel.visibility=View.VISIBLE
                tv_cancel.text = cancelText
                tv_cancel.setOnClickListener {
                    dismiss()
                }
            }
            5 -> {
                vView.visibility=View.VISIBLE
                tv_cancel.visibility=View.VISIBLE
                tv_cancel.text = cancelText
            }
            else -> {
                tv_cancel.visibility=View.GONE
                vView.visibility=View.GONE
            }
        }

        if (cancelType==4||cancelType==5){
            isCancelable=false
            mIsCancelOutside=false
        }

        tv_sure.setOnClickListener {
            dismiss()
            mDismissListener?.onDismiss(1)
        }

        tv_cancel.setOnClickListener {
            dismiss()
            mDismissListener?.onDismiss(0)
        }
    }

    fun setFragmentManager(manager: FragmentManager): YunGameDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): BaseDialog {
        show(mFragmentManager)
        return this
    }

    private var mDismissListener: DismissListener? = null
    fun setDismissListener(listener: DismissListener): YunGameDialog {
        this.mDismissListener = listener
        return this
    }

    interface DismissListener {
        fun onDismiss(type:Int)
    }

}
