package com.tinytiger.titi.widget.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.widget.dialog.base.BaseDialog
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.dialog_bottom_props_buy.*
import kotlinx.android.synthetic.main.dialog_props_exchange.*
import kotlinx.android.synthetic.main.dialog_props_exchange.et_num
import kotlinx.android.synthetic.main.dialog_props_exchange.tv_add
import kotlinx.android.synthetic.main.dialog_props_exchange.tv_sub


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description: 道具兑换
*/
class PropsExchangeDialog : BaseDialog() {

    companion object {
        fun create(manager: FragmentManager): PropsExchangeDialog {
            val dialog = PropsExchangeDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    private var max_num = 10

    private var mFragmentManager: FragmentManager? = null

    private fun setFragmentManager(manager: FragmentManager): PropsExchangeDialog {
        mFragmentManager = manager
        return this
    }

    override fun getLayoutRes(): Int = R.layout.dialog_props_exchange

    override fun bindView(v: View?) {

        tv_cancel.setOnClickListener {
            dismiss()
        }

        tv_exchange.setOnClickListener {
            if(et_num.text.isNotEmpty()){
                mViewListener?.click(et_num.text.toString().toInt())
            }
            dismiss()
        }


        et_num.setText(max_num.toString())

        tv_add.setOnClickListener {
            val num = et_num.text.toString().toInt()+1
            et_num.setText(num.toString())
        }

        tv_sub.setOnClickListener {
            var num = et_num.text.toString().toInt()-1
            et_num.setText(num.toString())
        }

        et_num.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                et_num.setSelection(s.toString().length);

                if (s.toString().isNotEmpty()) {
                    if (s.toString().length >= 2 && s.toString().indexOf("0") == 0) {

                        et_num.setText(s.toString().substring(1, s.toString().length))
                    }

                    var num = s.toString().toInt()
                    if(num>max_num){
                        et_num.setText(max_num.toString())
                    }else if(num<0){
                        et_num.setText("0")
                    }

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {


            }

        })
    }




    fun show(): BaseDialog {

        show(mFragmentManager)
        return this
    }


    fun setViewListener(listener: ViewListener): PropsExchangeDialog {
        this.mViewListener = listener
        return this
    }


    fun setPropsMaxNum(num: Int): PropsExchangeDialog {
        max_num = if(num>max_num) max_num else num
        return this
    }

    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(num:Int)
    }
}