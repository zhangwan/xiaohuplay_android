package com.tinytiger.titi.widget.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.net.data.props.PropsInfoBean
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.base.BaseBottomDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.CashierInputFilter
import kotlinx.android.synthetic.main.dialog_bottom_props_buy.*
import kotlin.math.max
import android.text.InputFilter
import android.R.attr.editable
import android.text.TextUtils
import android.util.Log
import com.orhanobut.logger.Logger


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description: 道具詳情 購買 dialog
*/
class PropsBuyDialog : BaseBottomDialog() {

    companion object {
        fun create(manager: FragmentManager): PropsBuyDialog {
            val dialog = PropsBuyDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    private var cur_num = 1
    private var max_num = 1
    private var total_num = 0

    private var mFragmentManager: FragmentManager? = null

    private fun setFragmentManager(manager: FragmentManager): PropsBuyDialog {
        mFragmentManager = manager
        return this
    }

    override fun getLayoutRes(): Int = R.layout.dialog_bottom_props_buy

    override fun bindView(v: View?) {

        iv_back.setOnClickListener {
            dismiss()
        }

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
                    refreshStatus(et_num.text.toString().toInt())

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {


            }

        })

        btn_next.setOnClickListener {
            if (mPropsInfoBean != null) {
                mViewListener?.click(mPropsInfoBean!!.id, cur_num)
            }
            dismiss()
        }

        if (mPropsInfoBean != null) {


            val max = (mPropsInfoBean!!.p_coin / mPropsInfoBean!!.price).toInt()
            max_num = if(max <mPropsInfoBean!!.stock)  mPropsInfoBean!!.stock else  max

            tv_name.text = mPropsInfoBean?.name
            tv_price.text = "- ${mPropsInfoBean!!.price.toInt()} H币"
            et_num.setText(cur_num.toString())

            val params = iv_bottom.layoutParams

            if (mPropsInfoBean?.shop_privilege == 1) {
                // 1=核心用户 0=普通用户
                iv_image.visibility = View.VISIBLE
                ll_num.visibility = View.VISIBLE
                ll_total.visibility = View.VISIBLE
                GlideUtil.loadImg(iv_image,mPropsInfoBean?.image)

                params.height = ScreenUtil.dp2px(context, 250f)
            } else {
                iv_image.visibility = View.GONE
                ll_num.visibility = View.GONE
                ll_total.visibility = View.GONE
                params.height = ScreenUtil.dp2px(context, 180f)
            }
            iv_bottom.layoutParams = params

        }


    }

    private fun refreshStatus(current_num: Int) {

        cur_num = current_num
        total_num = (current_num * mPropsInfoBean!!.price).toInt()

        tv_total.setText("- ${total_num} H币")

        if (total_num > mPropsInfoBean!!.p_coin) {
            //餘額不足
            btn_next.isEnabled = false
            btn_next.isClickable = false
            btn_next.isSelected = false
            btn_next.text = "余额不足（剩余${mPropsInfoBean!!.p_coin} H币）"
        } else {
            btn_next.isEnabled = true
            btn_next.isClickable = true
            btn_next.isSelected = true
            btn_next.text = "立即购买"
        }
    }


    fun show(): BaseBottomDialog {

        show(mFragmentManager)
        return this
    }


    fun setViewListener(listener: ViewListener): PropsBuyDialog {
        this.mViewListener = listener
        return this
    }

    private var mPropsInfoBean: PropsInfoBean? = null

    fun setPropsInfo(bean: PropsInfoBean?): PropsBuyDialog {
        mPropsInfoBean = bean
        return this
    }

    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(tool_id: String, num: Int)
    }
}