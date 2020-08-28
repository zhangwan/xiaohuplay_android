package com.tinytiger.titi.ui.mine.me.fans


import android.content.Context
import android.content.Intent


import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity


import kotlinx.android.synthetic.main.activity_base_frame.*



/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 查看他人粉丝列表
 */
class LookFansActivity : BaseActivity() {


    fun actionStart(context: Context, uid: String) {
        val intent = Intent(context, LookFansActivity::class.java)
        intent.putExtra("uid", uid)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.activity_base_frame


    override fun initData() {
        if (intent.hasExtra("uid")) {
            uid = intent.getStringExtra("uid")
        }
    }
    private var uid = ""
    private var mFragment: FansFragment? = null
    override fun initView() {
        tvTitle.centerTxt="Ta的粉丝"
        tvTitle.setBackOnClick {
            finish()
        }


        val transaction = supportFragmentManager.beginTransaction()
        mFragment?.let {
            transaction.show(it)
        } ?: FansFragment.getInstance(uid).let {
            mFragment = it
            transaction.add(R.id.fl_container, it, "mFragment1")
        }
        transaction.commitAllowingStateLoss()
    }

    override fun start() {

    }


}
