package com.tinytiger.titi.ui.mine.wallet

import android.content.Context
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.ActivityUtil
import kotlinx.android.synthetic.main.activity_base_recycler.*

/**
 * @author lmq001
 * @date 2020/7/21 下午 4:44
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 我的钱包-能量页
 */
class WalletEnergyActivity : BaseBackActivity() {

    companion object {
        fun actionStart(context: Context) {
            ActivityUtil.startActivityKx(context, WalletEnergyActivity::class.java, true)
        }
    }

    override fun layoutId() = R.layout.activity_energy

    override fun initData() {
    }

    override fun initView() {
        tvTitle.centerTxt = "能量"
        tvTitle.setBackOnClick {
            finish()
        }
    }

    override fun start() {

    }

}
