package com.tinytiger.titi.widget.dialog

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.widget.base.BaseBottomDialog
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.setting_dialog_pricacy.*


/**
 *
 * @author zhw_luke
 * @date 2019/3/12 0012 下午 4:59
 * @Copyright 小虎互联科技
 * @since 4.1.0
 * @doc 隐私
 */
class PrivacyDialog : BaseBottomDialog() {

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): PrivacyDialog {
            val dialog = PrivacyDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    override fun getLayoutRes(): Int = R.layout.setting_dialog_pricacy


    override fun bindView(v: View?) {

        tvCancel.setOnClickListener {
            dismiss()

        }

        tvAll.setOnClickListener {
            listener?.onPrivacyItem(1)
            dismiss()
        }

        tvAttention.setOnClickListener {
            listener?.onPrivacyItem(2)
            dismiss()
        }

        tvNotShow.setOnClickListener {
            listener?.onPrivacyItem(3)
            dismiss()
        }
        if (mNotShow) {
            tvNotShow.visibility = View.VISIBLE
        } else {
            tvNotShow.visibility = View.GONE
        }
    }

    private var mNotShow = false
    fun setNotShow(): PrivacyDialog {
        mNotShow = true
        return this
    }

    private fun setFragmentManager(manager: FragmentManager): PrivacyDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): PrivacyDialog {
        show(mFragmentManager)
        return this
    }


    interface onPrivacyListener {
        fun onPrivacyItem(index: Int)

    }

    var listener: onPrivacyListener? = null
    fun setOnNewsMoreListener(listener: onPrivacyListener): PrivacyDialog {
        this.listener = listener
        return this
    }
}