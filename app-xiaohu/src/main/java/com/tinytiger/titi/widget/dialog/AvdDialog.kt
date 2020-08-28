package com.tinytiger.titi.widget.dialog


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.dialog.base.BaseDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.BannerStartUtils
import kotlinx.android.synthetic.main.dialog_avd.*
import java.util.*


/**
 *
 * @author zhw_luke
 * @date 2020/6/28 0028 下午 4:53
 * @Copyright 小虎互联科技
 * @since 3.3.0
 * @doc 开屏广告
 */
class AvdDialog : BaseDialog() {

    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): AvdDialog {
            val dialog = AvdDialog()
            dialog.setFragmentManager(manager)
            dialog.isCancelable = false
            return dialog
        }
    }


    override fun getLayoutRes(): Int = R.layout.dialog_avd

    var avdBean: AdBean? = null
    override fun bindView(v: View?) {
        GlideUtil.loadImgUrl(ivAvd, avdBean!!.image)
        tvOpen.setOnClickListener {
            dismiss()
            BannerStartUtils.setStartIntent(avdBean!!, 0)
            mOnAvdListener?.onDismiss(avdBean!!.id)
        }
        ivClos.setOnClickListener { dismiss() }

        if (avdBean?.extend != null) {
            tvOpen.text = avdBean?.extend!!.button
            tvOpen.setTextColor(Color.parseColor("#${avdBean?.extend!!.text_color}"))
            val normal = ContextCompat.getDrawable(context!!, R.drawable.solid_rectangle_20_ffcc03)
            val normalGroup = normal as GradientDrawable
            normalGroup.setColor(Color.parseColor("#${avdBean?.extend!!.button_color}"))
            tvOpen.background = normalGroup
        }
    }

    fun setFragmentManager(manager: FragmentManager): AvdDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): BaseDialog {
        show(mFragmentManager)
        return this
    }


    fun startADV() {
        //广告列表
        val open_page = SpUtils.getString(R.string.open_popup, "")
        if (!open_page.isNullOrEmpty()) {
            val open_page_id = SpUtils.getString(R.string.open_page_id, "[]")

            val open_page_list = ArrayList<String>()
            if (!open_page_id.isNullOrEmpty()) {
                JSON.parseArray(open_page_id).mapTo(open_page_list) { it.toString() }
            }

            val open_page = ArrayList<AdBean>(JSONArray.parseArray(open_page, AdBean::class.java))
            val time = System.currentTimeMillis()
            val list = ArrayList<AdBean>()
            for (i in open_page) {
                val start_time = TimeZoneUtil.getStringToDate(i.start_time)
                val end_time = TimeZoneUtil.getStringToDate(i.end_time)
                if (time in start_time until end_time) {
                    var t = false
                    for (j in open_page_list) {
                        if (j == i.id) {
                            t = true
                            continue
                        }
                    }

                    if (!t) {
                        list.add(i)
                    }
                }
            }

            if (list.size < 1) {
                return
            }else{
                avdBean = list[0]
                open_page_list.add(list[0].id)
                SpUtils.saveSP(R.string.open_page_id, JSON.toJSON(open_page_list).toString())
                show()
            }
        }
    }

    fun setAvdListener(listener: OnAvdListener): AvdDialog {
        mOnAvdListener = listener
        return this
    }

    var mOnAvdListener: OnAvdListener? = null

    interface OnAvdListener {
        fun onDismiss(id: String)
    }
}
