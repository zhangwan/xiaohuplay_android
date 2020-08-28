package com.tinytiger.titi.ui.props

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.data.props.PropsExchangeBean
import com.tinytiger.common.net.data.props.PropsInfoBean
import com.tinytiger.common.net.data.props.PropsInfoListBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.AmintionUtils
import com.tinytiger.common.widget.ShareDialog
import com.tinytiger.titi.adapter.props.PropsRecommendAdapter


import com.tinytiger.titi.mvp.contract.props.PropsInfoContract
import com.tinytiger.titi.mvp.presenter.props.PropsInfoPresenter
import com.tinytiger.titi.ui.props.exchage.PropsExchangeActivity
import com.tinytiger.titi.widget.dialog.PropsBuyDialog
import com.tinytiger.titi.widget.dialog.PropsExchangeDialog
import com.tinytiger.titi.widget.popup.PropsPopup
import kotlinx.android.synthetic.main.props_activity_detail.*


import kotlin.collections.ArrayList

/**
 *
 * @Author 李佳维
 * @Date 2020-02-04 16:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具商城详情
 *
 */
class PropsDetailActivity : BaseActivity(), PropsInfoContract.View {



    private var tool_id:String = "0"
    private var cate_id:String = "0"

    //核心用户
    private var shop_privilege:Int = 0

    companion object {

        private const val EXTRA_TOOL_ID = "props_tool_id"

        fun actionStart(context: Context,tool_id:String) {
            val intent = Intent(context, PropsDetailActivity::class.java)
            intent.putExtra(EXTRA_TOOL_ID,tool_id)
            context.startActivity(intent)
        }

    }

    override fun layoutId(): Int = R.layout.props_activity_detail

    private val mPresenter by lazy {  PropsInfoPresenter() }

    private val mAdapter by lazy {  PropsRecommendAdapter() }


    init {
        mPresenter.attachView(this)
    }

    override fun initData() {
        tool_id = intent.getStringExtra(EXTRA_TOOL_ID)

        shop_privilege =  SpUtils.getInt(R.string.props_user,0)
    }


    private var mDemoPopup: PropsPopup? = null

    override fun initView() {

        title_view.centerTxt="背景"
        title_view.setRightIV( R.mipmap.icon_more_b)
        title_view.setBackOnClick {
            finish()
        }
        title_view.setRightIVClick {

            if (mDemoPopup == null) {
                mDemoPopup = PropsPopup(this)
                mDemoPopup!!.setShowAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f))
                    .setDismissAnimation(AmintionUtils().createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f))

                mDemoPopup?.setExchangeListener(mPropsInfoBean?.is_has == 1 && shop_privilege ==1) {
                    mDemoPopup?.dismiss()
                    showExchangeDialog()
                }
                mDemoPopup?.setShareListener {
                    mDemoPopup?.dismiss()
                    showShareDialog()

                }
            }

            val location = IntArray(2)
            it.getLocationInWindow(location)
            it.getLocationOnScreen(location)

            mDemoPopup!!.setBackground(null)
                .setBlurBackgroundEnable(false)
                .showPopupWindow(
                    location[0] - Dp2PxUtils.dip2px(this, 60),
                    location[1] + Dp2PxUtils.dip2px(this, 25)
                )
        }


        btn_next.setOnClickListener {
            if(mPropsInfoBean?.is_has == 1){
                //已擁有
                onWearAction()
            }else{
                //未擁有
                onBuyAction()
            }
        }

        tv_buy.setOnClickListener {
            onBuyAction()
        }

        tv_wear.setOnClickListener {
            onWearAction()
        }

        btn_back.setOnClickListener {
            PropsMeActivity.actionStart(this)
            finish()

        }

        recycler_view.layoutManager=  GridLayoutManager(this, 3) as GridLayoutManager
        recycler_view.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            mDemoPopup = null
            tool_id = mAdapter.data[position].id

            start()
        }


        start()
    }

    private fun showShareDialog() {

                if (mPropsInfoBean != null) {
                    ShareDialog.create(supportFragmentManager)
                        .apply {
                            class_name = "no"
                            share_url = mPropsInfoBean!!.shop_tool_share_url
                            share_title = mPropsInfoBean!!.name
                            share_desc = mPropsInfoBean!!.cate_name
                            share_image = mPropsInfoBean!!.image
                        }
                        .show()
                }
    }

    private fun showExchangeDialog(){
        if(mPropsInfoBean==null)
            return
        PropsExchangeDialog.create(supportFragmentManager)
            .setPropsMaxNum(mPropsInfoBean!!.num)
            .setViewListener(object :PropsExchangeDialog.ViewListener{
                override fun click(num: Int) {
                    if(num>0){
                        mPresenter.exchangeTools(mPropsInfoBean!!.id,num)
                    }else{
                        showToast("兑换数量必须大于0")
                    }

                }


            })
            .show()
    }

    private fun onWearAction(){

        mPresenter.wearProps(tool_id,cate_id)
    }

    private fun onBuyAction(){
        PropsBuyDialog.create(supportFragmentManager)
            .setPropsInfo(mPropsInfoBean)
            .setViewListener(object :PropsBuyDialog.ViewListener{
                override fun click(tool_id: String, num: Int) {
                    mPresenter.buyTool(tool_id,num)

                    val num = (num*mPropsInfoBean!!.price).toString()
                    tv_number.text =num
                }


            })
            .show()
    }

    override fun start() {
        mPresenter.getPropsInfo(tool_id)
    }


    private var mPropsInfoBean :PropsInfoBean?=null

    override fun showPropsInfo(bean: PropsInfoListBean.DataBean) {
        //商品的詳細
        if(bean.info!=null){
            mPropsInfoBean =bean.info
            mPropsInfoBean?.shop_privilege = shop_privilege
            GlideUtil.loadImg(iv_image,bean.info.image)
            tv_name.text =  bean.info.name
            tv_price.text=   "${bean.info.price.toInt()} H币"
            title_view.centerTxt=bean.info.cate_name

            cate_id=bean.info.cate_id
           if(bean.info.is_has == 1){
               btn_next.text =if(bean.info.is_wear ==1 ) "取下" else "佩戴"
               tv_wear.text =if(bean.info.is_wear ==1 ) "取下" else "佩戴"
           }else{
               btn_next.text ="立即购买"
           }

            if(bean.info.is_has == 1 && shop_privilege==1){
                btn_next.visibility  = View.GONE
                ll_privilege.visibility  = View.VISIBLE
            }else{
                btn_next.visibility  = View.VISIBLE
                ll_privilege.visibility  = View.GONE
            }

        }

        //道具的推薦
        if(bean.indexToolsRecommend!=null){
            //商品的詳細
            mAdapter.setNewInstance(bean.indexToolsRecommend)

        }
    }

    override fun showWearResult(msg:String) {

        mPropsInfoBean?.is_wear = if( mPropsInfoBean?.is_wear ==1 ) 0 else 1
        btn_next.text =if( mPropsInfoBean?.is_wear ==1 ) "取下" else "佩戴"
        tv_wear.text =if(mPropsInfoBean?.is_wear  ==1 ) "取下" else "佩戴"
        showToast(msg)

    }


    override fun showExchange(bean: PropsExchangeBean.DataBean) {
        PropsExchangeCodeActivity.actionStart(this,bean)
    }


    override fun showResult() {
        layout_main.visibility = View.GONE
        layout_complete.visibility = View.VISIBLE
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
