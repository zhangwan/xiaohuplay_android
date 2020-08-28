package com.tinytiger.titi.widget.dialog


import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.widget.dialog.base.BaseDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.dialog_nickname.*


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description:  昵称输入 dialog
*/
class EditNameDialog : BaseDialog() {

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): EditNameDialog {
            val dialog = EditNameDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    var maxLength=16
    var nickname: String = ""
    var title=""
    override fun getLayoutRes(): Int = R.layout.dialog_nickname


    override fun bindView(v: View?) {
        tv_cancel.setOnClickListener {
            dismiss()
        }


        tv_save.setOnClickListener {
            val keyWord = et_text.text.toString()
            if (keyWord.isNullOrEmpty()) {
                ToastUtils.show(context,"输入不能为空")
                return@setOnClickListener
            }
            if (CheckUtils.checkLocalAntiSpam(keyWord)) {
                ToastUtils.show(context, "关键词含有敏感词汇")
                return@setOnClickListener
            }
            mViewListener?.click(et_text.text.toString())
            dismiss()
        }

        et_text.addTextChangedListener(watcher)
        et_text.setFilters(arrayOf<InputFilter>(LengthFilter(maxLength)))
        et_text.setText(nickname)
        tvSize.text = "${nickname.length}/$maxLength"
        tv_title.text=title
    }


    var watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            tvSize.text = "${p0.toString().length}/$maxLength"
        }
    }


    fun setFragmentManager(manager: FragmentManager): EditNameDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): BaseDialog {
        show(mFragmentManager)
        return this
    }

    fun setViewListener(listener: ViewListener): EditNameDialog {
        this.mViewListener = listener
        return this
    }

    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(string: String)
    }


}
