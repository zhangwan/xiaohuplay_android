package com.tinytiger.titi.ui.props.exchage

import android.content.Context
import android.content.Intent
import android.view.View
import com.alibaba.fastjson.JSON
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsExchangeListBean
import com.tinytiger.common.net.exception.ErrorStatus
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.props.PropsSearchAdapter
import com.tinytiger.titi.mvp.contract.props.PropsExchangeContract
import com.tinytiger.titi.mvp.presenter.props.PropsExchangePresenter

import kotlinx.android.synthetic.main.props_activity_exchange_coupon.*
/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 道具兑换，兑换券确认页
 */
class PropsExchangeCouponActivity : BaseActivity(), PropsExchangeContract.View {

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {

        }
    }


    override fun getSubmitExchange(bean: PropsExchangeListBean) {

    }

    override fun getSubmit(bean: BaseBean) {
        rlOver.visibility= View.VISIBLE


    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    companion object {
        fun actionStart(context: Context,json:String,code:String) {
            val intent = Intent(context, PropsExchangeCouponActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("json", json)
            context.startActivity(intent)
        }
    }


    override fun layoutId(): Int = R.layout.props_activity_exchange_coupon
    private val mPresenter by lazy { PropsExchangePresenter() }

    private val mAdapter by lazy { PropsSearchAdapter() }

    init {
        mPresenter.attachView(this)
    }

    var code=""
    override fun initData() {
         code = intent.getStringExtra("code")
    }


    override fun initView() {
        tvTitle.centerTxt="道具兑换"
        tvTitle.setBackOnClick {
                finish()
        }
        val json = intent.getStringExtra("json")
        val bean = JSON.parseObject(json, PropsExchangeListBean::class.java)

        tvDesc.text="合计：${bean.data.totalCate}种道具，共${bean.data.totalNum}件"

        mAdapter.searchTxt=false
        mAdapter.setNewInstance(bean.data.detail)

        recycler_view.adapter = mAdapter

        btn_See.setOnClickListener {
            AppManager.getAppManager().finishActivity(PropsExchangeActivity::class.java)
            finish()
        }


        tvComplete.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            showLoading()
            mPresenter.submitExchange(code)
        }
    }

    override fun onBackPressed() {
        mFinish()
    }
    private fun mFinish(){
        finish()
    }

    override fun start() {


    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }




}
