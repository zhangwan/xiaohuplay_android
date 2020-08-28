package com.tinytiger.titi.ui.mine.setting


import android.content.Context
import android.content.Intent
import com.tinytiger.common.net.data.mine.UserPrivacyBean
import com.tinytiger.titi.R
import com.tinytiger.common.base.BaseActivity
import com.tinytiger.common.base.BaseBackActivity

import com.tinytiger.titi.mvp.contract.mine.SettingPrivacyContract
import com.tinytiger.titi.mvp.presenter.mine.SettingPrivacyPresenter
import com.tinytiger.titi.widget.dialog.PrivacyDialog
import kotlinx.android.synthetic.main.setting_activity_pricacy.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/5 0005 下午 1:56
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 隐私
 */
class PrivacyActivity : BaseBackActivity() , SettingPrivacyContract.View {
    override fun getUserPrivateConfig(bean: UserPrivacyBean) {
        letterInfo = bean.data.user_config.private_letter
        cityInfo=  bean.data.user_config.city_show
        setText()
    }

    override fun showPrivacyData() {
        showToast("设置成功")
        setText()
    }

    override fun showErrorMsg(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
    }

    override fun dismissLoading() {
        hideProgress()
    }

    override fun showLoading() {
       showProgress()
    }

    /**
     *reportType 举报类型1内容 2评论
     * reportId 举报id 内容id ,评论id
     */
    fun actionStart(context: Context) {
        val intent = Intent(context, PrivacyActivity::class.java)
        context.startActivity(intent)
    }

    override fun layoutId(): Int = R.layout.setting_activity_pricacy
    private val mPresenter by lazy { SettingPrivacyPresenter() }
    init {
           mPresenter.attachView(this)
    }

    override fun initData() {

    }

    override fun initView() {
        tvTitle.centerTxt="隐私"
        tvTitle.setBackOnClick { finish() }
        item_Black.setOnClickListener {
            BlackListActivity().actionStart(this)
        }
        item_letter.setContentText("所有人")
        item_letter.setOnClickListener {
            PrivacyDialog.create(supportFragmentManager)
                .setOnNewsMoreListener(object : PrivacyDialog.onPrivacyListener{
                    override fun onPrivacyItem(index: Int) {
                        letterInfo=index
                        mPresenter.modifyPrivateConfig(1,index)
                    }

                }).show()
        }

        item_city.setContentText("所有人")
        item_city.setOnClickListener {
            PrivacyDialog.create(supportFragmentManager)
                .setNotShow()
                .setOnNewsMoreListener(object : PrivacyDialog.onPrivacyListener{
                    override fun onPrivacyItem(index: Int) {
                        cityInfo= index
                        mPresenter.modifyPrivateConfig(2,index)
                    }

                }).show()
        }

        start()
    }


   // "private_letter": 1, #1所有人 2我关注的人
   // "city_show": 1 #1所有人 2我关注的人 3不显示
    private var cityInfo= 1
    private var letterInfo= 1

    private fun setText(){


        var letter="所有人"
        if (letterInfo==2){
            letter="我关注的人"
        }
        item_letter.setContentText(letter)

        var city="所有人"
        when(cityInfo){
            2->{
                city="我关注的人"
            }
            3->{
                city="不显示"
            }
        }
        item_city.setContentText(city)
    }


    override fun start() {
        mPresenter.loadUserPrivateConfig()
    }



    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}
