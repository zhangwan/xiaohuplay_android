package com.tinytiger.titi.ui.home2


import android.content.Context
import android.content.Intent


import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils


import kotlinx.android.synthetic.main.activity_base_frame.*



/**
 *
 * @author zhw_luke
 * @date 2020/7/27 0027 下午 5:39
 * @Copyright 小虎互联科技
 * @since 3.4.0
 * @doc 安利文
 */
class AmwayActivity : BaseActivity() {


    fun actionStart(context: Context) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (!MyNetworkUtil.isNetworkAvailable(BaseApp._instance)) {
            ToastUtils.show(context, "无网络")
            return
        }
        val intent = Intent(context, AmwayActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.activity_base_frame


    override fun initData() {

    }

    private var mFragment: HomeAmwayFragment? = null
    override fun initView() {
        tvTitle.centerTxt="安利墙"
        tvTitle.setBackOnClick {
            finish()
        }


        val transaction = supportFragmentManager.beginTransaction()
        mFragment?.let {
            transaction.show(it)
        } ?: HomeAmwayFragment.getInstance().let {
            mFragment = it
            transaction.add(R.id.fl_container, it, "mFragment1")
        }
        transaction.commitAllowingStateLoss()
    }

    override fun start() {

    }


}
