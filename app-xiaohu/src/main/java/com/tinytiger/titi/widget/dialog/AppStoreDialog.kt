package com.tinytiger.titi.widget.dialog

import android.view.View

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tinytiger.common.widget.base.BaseBottomDialog
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.AppStoreAdapter
import com.tinytiger.titi.utils.MarketUtil
import kotlinx.android.synthetic.main.dialog_appstore.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/26 0026 上午 10:23
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 商店评分
 */
class AppStoreDialog : BaseBottomDialog() {


    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): AppStoreDialog {
            val dialog = AppStoreDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }

    }

    override fun getLayoutRes(): Int = R.layout.dialog_appstore

    override fun bindView(v: View?) {


        tv_cancel.setOnClickListener {
            dismiss()
        }


        val list = MarketUtil.loadApps(activity)
        if (list.size == 0) {
            MarketUtil().launchAppDetail(activity, "")

            dismiss()
        } else {
            val mAdapter by lazy { AppStoreAdapter() }
            recycler_view.layoutManager = GridLayoutManager(context, 4)
            recycler_view.adapter = mAdapter
            mAdapter.setNewInstance(list)

            mAdapter.setOnItemClickListener { _, _, position ->

                MarketUtil().launchAppDetail(
                    activity,
                    mAdapter.data[position].activityInfo.packageName
                )
                dismiss()
            }

        }

    }


    fun setFragmentManager(manager: FragmentManager): AppStoreDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): AppStoreDialog {
        show(mFragmentManager)
        return this
    }

}