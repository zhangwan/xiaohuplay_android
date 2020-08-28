package com.tinytiger.common.widget.dialog

import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.R
import com.tinytiger.common.widget.dialog.base.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_bottom_gender.*


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description: 
*/
class GenderDialog : BaseBottomDialog() {

    companion object {
        fun create(manager: FragmentManager): GenderDialog {
            val dialog = GenderDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    private var mFragmentManager: FragmentManager? = null

    private fun setFragmentManager(manager: FragmentManager): GenderDialog {
        mFragmentManager = manager
        return this
    }

    override fun getLayoutRes(): Int = R.layout.dialog_bottom_gender

    override fun bindView(v: View?) {
        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_sure.setOnClickListener {
            mViewListener?.click(wheel_view.selectedItemPosition + 1)
            dismiss()
        }

        //初始化数据
        val list = arrayListOf<String>()
        list.add(getString(R.string.boy))
        list.add(getString(R.string.girl))




        wheel_view.data = list
        wheel_view.setTextSize(20f, true)

        wheel_view.dividerHeight = 1f
        wheel_view.isShowDivider = true
        wheel_view.setDividerColorRes(R.color.color_line)

        wheel_view.setNormalItemTextColorRes(R.color.gray99)
        wheel_view.setSelectedItemTextColorRes(R.color.gray28)
        wheel_view.setTextBoundaryMargin(16f, true)


        wheel_view.selectedItemPosition = (gender-1)


    }


    fun show(): BaseBottomDialog {

        show(mFragmentManager)
        return this
    }


    fun setViewListener(listener: ViewListener): GenderDialog {
        this.mViewListener = listener
        return this
    }

    private var gender = 1
    fun setGender(gender: Int): GenderDialog {
        this.gender = gender
        return this
    }

    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(gender: Int)
    }
}