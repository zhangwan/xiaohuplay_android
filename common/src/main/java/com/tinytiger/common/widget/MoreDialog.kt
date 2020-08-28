package com.tinytiger.common.widget


import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.R

import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.base.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_more.*
import org.greenrobot.eventbus.EventBus

/**
 * @author: Tamas
 * @date: On 2018/10/24 0024
 * @desc: 收藏,举报
 */
class MoreDialog : BaseBottomDialog() {

    //内容id
    var contentId = ""

    //举报类型：0不显示 1内容 2评论 3用户 4派对 5评价
    var report_type=0
    var user_id = ""
    /**
     * 收藏
     * -1不显示
     * 0收藏
     * 1取消收藏
     * 1:是 0:否
     */
    var collectionType =0

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): MoreDialog {
            val dialog = MoreDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    override fun getLayoutRes(): Int = R.layout.dialog_more

    override fun bindView(v: View?) {

        when(collectionType){
            -1->{
                tvCollection.text="收藏"
                tvCollection.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(activity!!, R.mipmap.icon_collect_nor), null, null, null);
            }
            1->{
                tvCollection.text="取消收藏"
                tvCollection.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(activity!!, R.mipmap.icon_collect_sel), null, null, null);
            }
            else->{
                tvCollection.visibility=View.GONE
            }
        }

        tvCollection.setOnClickListener {
            dismiss()
            listener?.click(collectionType)
        }
        tvReport.setOnClickListener {
            if(SpUtils.getString(R.string.token,"").isEmpty()){
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
            }else{
                dismiss()
                EventBus.getDefault().post(ClassEvent("ReportActivity", report_type, contentId,user_id))

            }
        }


        tv_cancel.setOnClickListener {
            dismiss()
        }
    }


    fun setFragmentManager(manager: FragmentManager): MoreDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): MoreDialog {
        show(mFragmentManager)
        return this
    }


    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener): MoreDialog {
        this.listener = listener
        return this
    }

    interface OnItemClickListener {
        // 1 收藏
        fun click(type: Int)
    }


}