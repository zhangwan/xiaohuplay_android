package com.tinytiger.titi.ui.mine.wallet

import android.content.Context
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.ActivityUtil

/**
 * @author lmq001
 * @date 2020/7/21 下午 4:44
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 我的钱包-抽奖页
 */
class WalletLotteryActivity : BaseBackActivity() {

    companion object {
        fun actionStart(context: Context) {
            ActivityUtil.startActivityKx(context, WalletLotteryActivity::class.java, true)
        }
    }

    override fun layoutId() = R.layout.activity_lottery

    override fun initData() {
        setWindowFeature()
    }

    override fun initView() {

    }

    override fun start() {

    }

}
