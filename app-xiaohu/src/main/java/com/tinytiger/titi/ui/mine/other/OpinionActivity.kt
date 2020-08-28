package com.tinytiger.titi.ui.mine.other


import android.content.Context
import android.content.Intent
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.titi.mvp.contract.mine.ReportContract
import com.tinytiger.titi.mvp.presenter.mine.ReportPresenter
import kotlinx.android.synthetic.main.activity_report.et_send
import kotlinx.android.synthetic.main.mine_activity_opinion.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 意见反馈
 */
class OpinionActivity : BaseActivity(), ReportContract.View {
    override fun getQiniuToken(qiniuTocken: String) {

    }

    override fun getReportData() {
        showToast("提交成功")
        finish()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun showLoading() {
        showProgress()
    }

    override fun dismissLoading() {
        hideProgress()
    }

    /**
     *reportType 举报类型1内容 2评论
     * reportId 举报id 内容id ,评论id
     */
    fun actionStart(context: Context) {
        val intent = Intent(context, OpinionActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.mine_activity_opinion
    private val mPresenter by lazy { ReportPresenter() }

    init {
        mPresenter.attachView(this)
    }

    override fun initData() {

    }



    override fun initView() {
        title_view.centerTxt = "意见反馈"
        title_view.setBackOnClick { finish() }

        title_view.rightTxt="提交"
        title_view.setRightOnClick {
            val search=et_send.text.trim().toString()
            if (search.isEmpty()){
                showToast("请输入内容")
                return@setRightOnClick
            }
            var feedback_type=1
            when(card_bottom.checkedRadioButtonId){
                R.id.card_rb_1->{
                    feedback_type=2
                }
                R.id.card_rb_2->{
                    feedback_type=3
                }
                R.id.card_rb_3->{
                    feedback_type=4
                }
                else->{
                    feedback_type=1
                }
            }
            et_send.setText("")
            mPresenter.feedback(feedback_type,search)
        }
        
    }


    override fun start() {

    }




    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }


}
