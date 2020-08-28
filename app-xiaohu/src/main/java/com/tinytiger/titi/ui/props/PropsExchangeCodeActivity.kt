package com.tinytiger.titi.ui.props

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager

import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.props.PropsExchangeBean
import com.tinytiger.common.net.data.props.PropsInfoListBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.adapter.props.PropsCodeAdapter
import com.tinytiger.titi.mvp.contract.props.PropsInfoContract
import com.tinytiger.titi.mvp.presenter.props.PropsInfoPresenter
import kotlinx.android.synthetic.main.props_activity_exchange_code.*

/**
 *
 * @Author 李佳维
 * @Date 2020-02-04 16:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具兑换
 *
 */
class PropsExchangeCodeActivity : BaseActivity() , PropsInfoContract.View {


    private var mPropsExchangeBean:PropsExchangeBean.DataBean?=null
    private var mCodeId = "0"

    companion object {

        private const val EXTRA_EXCHANGE_CODE = "exchange_code"
        private const val EXTRA_EXCHANGE_CODE_ID = "exchange_code_id"

        fun actionStart(context: Context,bean:PropsExchangeBean.DataBean?) {
            val intent = Intent(context, PropsExchangeCodeActivity::class.java)
            if(bean!=null){
                intent.putExtra(EXTRA_EXCHANGE_CODE,bean)
            }

            context.startActivity(intent)
        }


        fun actionStart(context: Context,id:String) {
            val intent = Intent(context, PropsExchangeCodeActivity::class.java)
            intent.putExtra(EXTRA_EXCHANGE_CODE_ID,id)

            context.startActivity(intent)
        }

    }

    override fun layoutId(): Int = R.layout.props_activity_exchange_code


    private val mAdapter by lazy {  PropsCodeAdapter() }

    private val mPresenter by lazy {  PropsInfoPresenter() }



    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
        if(intent.hasExtra(EXTRA_EXCHANGE_CODE)){
            mPropsExchangeBean = intent.getSerializableExtra(EXTRA_EXCHANGE_CODE) as PropsExchangeBean.DataBean
        }
        if(intent.hasExtra(EXTRA_EXCHANGE_CODE_ID)){
            mCodeId = intent.getStringExtra(EXTRA_EXCHANGE_CODE_ID)
        }
    }



    override fun initView() {

        title_view.centerTxt="道具兑换"
        title_view.setBackOnClick {
            finish()
        }

        recycler_view.layoutManager=  LinearLayoutManager(this)
        recycler_view.adapter = mAdapter

        if(mPropsExchangeBean != null){
            refreshStatus()
        }else{
            start()
        }

    }


    private fun refreshStatus(){
        tv_name.text = "${mPropsExchangeBean?.name}  x${mPropsExchangeBean?.tool_number}"
        tv_cate.text = mPropsExchangeBean?.cate_name
        GlideUtil.loadImg(iv_code,mPropsExchangeBean?.exchange_img)

        btn_copy.setOnClickListener {
            if(mPropsExchangeBean?.exchange_code!=null){

                val s = StringBuilder()
                for(string in  mPropsExchangeBean!!.exchange_code){
                    s.append(string).append("\n")
                }

                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText(null, s))
                showToast("兑换码已复制到粘贴板")
            }
        }
        mAdapter.setNewInstance(mPropsExchangeBean?.exchange_code)
    }


    override fun start() {
        mPresenter.getExchangeInfo(mCodeId)
    }

    override fun showExchange(bean: PropsExchangeBean.DataBean) {
            mPropsExchangeBean = bean
            refreshStatus()

    }


    override fun showPropsInfo(bean: PropsInfoListBean.DataBean) {

    }

    override fun showResult() {
    }

    override fun showWearResult(msg: String) {
    }



    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }




}
