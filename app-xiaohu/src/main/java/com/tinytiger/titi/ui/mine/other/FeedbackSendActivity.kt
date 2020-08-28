package com.tinytiger.titi.ui.mine.other

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.common.net.data.mine.FeedbackListBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.FeedbackCateAdapter
import com.tinytiger.titi.mvp.contract.mine.FeedbackContract
import com.tinytiger.titi.mvp.presenter.mine.FeedbackPresenter
import kotlinx.android.synthetic.main.activity_feedback_send.*


/*
* @author Tamas
* create at 2020/1/7 0007
* Email: ljw_163mail@163.com
* description: 产品建议墙-  编辑建议
*/
class FeedbackSendActivity : BaseActivity(), FeedbackContract.View {


    private val mPresenter by lazy { FeedbackPresenter() }
    private val mAdapter by lazy { FeedbackCateAdapter() }

    companion object {

        private const val MAX_LENGTH = 300

        fun actionStart(context: Activity, requestCode: Int) {
            if (FastClickUtil.isFastClickTiming()) {
                return
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return
            }
            val intent = Intent(context, FeedbackSendActivity::class.java)
            context.startActivityForResult(intent, requestCode)
        }
    }

    override fun layoutId(): Int = R.layout.activity_feedback_send

    override fun initData() {
        mPresenter.attachView(this)
    }

  private  var opinion_cat_id = 0

    override fun initView() {
        title_view.centerTxt = "编辑建议"
        title_view.setBackOnClick {
            finish()
        }

        iv_submit.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (isLoginStart()) {
                //填写意见
                if (et_feedback.text.isEmpty()) {
                    showToast("请完善内容信息")
                    return@setOnClickListener
                }
                 opinion_cat_id = 0
                for (item in mAdapter.data) {
                    if (item.is_selected) {
                        opinion_cat_id = item.id
                    }
                }
                if (opinion_cat_id == 0) {
                    showToast("请选择建议类型")
                    return@setOnClickListener
                }

                mPresenter.addProposal(opinion_cat_id, et_feedback.text.toString().trim())
            }
        }
        tv_count.text = "0/$MAX_LENGTH"
        et_feedback.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(et_feedback.text)) {
                    tv_count.text = "0/$MAX_LENGTH"
                } else {
                    tv_count.text = "${et_feedback.text.toString().length}/$MAX_LENGTH"
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        recycler_view.layoutManager = GridLayoutManager(this, 3)
        recycler_view.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, view, position ->
            for (index in 0 until mAdapter.data.size) {
                if (index == position) {
                    if( !mAdapter.data[index].is_selected){
                        mAdapter.data[index].is_selected = true
                    }
                } else {
                    mAdapter.data[index].is_selected = false
                }

            }
            mAdapter.notifyDataSetChanged()
        }


        start()
    }


    override fun start() {
        mPresenter.getCatList()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


    /**
     * ================================================= Contract 接口 =================================================
     */

    override fun showCateList(bean: List<CatListBean.CateBean>) {
        val mList = ArrayList<CatListBean.CateBean>()

        if (bean.isNotEmpty()) {
            for (item in bean) {
                if (item.id != 0) {
                    mList.add(item)
                }
            }

        }
        if (mList.size > 0) {
            mList[0].is_selected = true
        }
        mAdapter.setNewInstance(mList)

    }


    override fun showProposalList(bean: FeedbackListBean.DataBean?) {

    }

    override fun showResult() {
        showToast("提交成功！我们会认真阅读您的建议，有效内容会在三个工作日内收到答复哦~")

        val intent = Intent()
        intent.putExtra("feedback", et_feedback.text.toString())
        intent.putExtra("opinion_cat_id", opinion_cat_id)
        setResult(RESULT_OK, intent)
        finish()

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