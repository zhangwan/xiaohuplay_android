package com.tinytiger.titi.ui.mine.me.fans


import android.content.Context
import android.content.Intent


import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.data.MyUserData


import kotlinx.android.synthetic.main.activity_base_frame.*
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 查看他人关注列表
 */
class LookAttentionActivity : BaseActivity() {


    fun actionStart(context: Context, uid: String) {
        if (FastClickUtil.isFastClickTiming()) {
            return
        }
        if (MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
        val intent = Intent(context, LookAttentionActivity::class.java)
        intent.putExtra("uid", uid)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.activity_base_frame


    init {

    }

    override fun initData() {
        if (intent.hasExtra("uid")) {
            uid = intent.getStringExtra("uid")
        }
    }
    private var uid = ""
    private var mFragment: AttentionFragment? = null
    override fun initView() {


        tvTitle.centerTxt="Ta的关注"
        tvTitle.setBackOnClick {
            finish()
        }

        val transaction = supportFragmentManager.beginTransaction()

        mFragment?.let {
            transaction.show(it)
        } ?: AttentionFragment.getInstance(uid).let {
            mFragment = it
            transaction.add(R.id.fl_container, it, "AttentionFragment")
        }
        transaction.commitAllowingStateLoss()
    }

    override fun start() {

    }


}
