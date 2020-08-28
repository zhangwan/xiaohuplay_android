package com.tinytiger.common.widget.dialog


import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.R
import com.tinytiger.common.widget.dialog.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_desc.*
import kotlinx.android.synthetic.main.dialog_text.tv_cancel


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description:  简介 输入 dialog
*/
class EditDescDialog : BaseDialog() {


    private var mFragmentManager: FragmentManager? = null


    companion object {
        fun create(manager: FragmentManager): EditDescDialog {
            val dialog = EditDescDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }


    }


    override fun getLayoutRes(): Int = R.layout.dialog_desc

    override fun bindView(v: View?) {


        tv_cancel.setOnClickListener {
            dismiss()
        }


        tv_save.setOnClickListener {
            mViewListener?.click(et_text.text.toString())
            dismiss()
        }

        et_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tv_num.text = "${et_text.text.toString().length}/30"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et_text.setText(descString)
    }


    fun setFragmentManager(manager: FragmentManager): EditDescDialog {
        mFragmentManager = manager
        return this
    }

    private var descString :String=""

    fun setDescString(desc: String?): EditDescDialog {
        if(desc==null){
            return this
        }
        descString = desc
        return this
    }


    fun show(): BaseDialog {
        show(mFragmentManager)
        return this
    }

    fun setViewListener(listener: ViewListener): EditDescDialog {
        this.mViewListener = listener
        return this
    }


    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(string: String)
    }


}
