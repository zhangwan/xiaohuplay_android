package com.tinytiger.titi.ui.mine.wallet

import android.content.Context
import com.tinytiger.common.base.BaseBackActivity
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.ActivityUtil
import kotlinx.android.synthetic.main.activity_wallet.*

/**
 * @author lmq001
 * @date 2020/7/21 下午 4:44
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 我的钱包
 */
class WalletActivity : BaseBackActivity() {

    companion object {
        fun actionStart(context: Context) {
            ActivityUtil.startActivityKx(context, WalletActivity::class.java, true)
        }
    }

    override fun layoutId() = R.layout.activity_wallet

    override fun initData() {
        setWindowFeature()
    }

    override fun initView() {
        circle_like_comment.setPercentage(75f)

        left_back_iv.setOnClickListener { finish() }

        ll_like_comment.setOnClickListener { WalletEnergyActivity.actionStart(this) }

        ll_conduct_mining.setOnClickListener { WalletEnergyActivity.actionStart(this) }
    }

    override fun start() {

    }

}
