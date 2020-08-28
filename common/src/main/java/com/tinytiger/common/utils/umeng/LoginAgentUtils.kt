package com.tinytiger.common.utils.umeng

import com.tinytiger.common.R
import com.tinytiger.common.base.BaseApp
import com.tinytiger.common.utils.preference.SpUtils
import com.umeng.analytics.MobclickAgent


/**
 *
 * @author zhw_luke
 * @date 2020/6/9 0009 下午 5:52
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 友盟登录事件
 */
object LoginAgentUtils {

    /**
     * 手机号注册登录
     */
    fun setLoginPhone() {
        MobclickAgent.onProfileSignIn("Phone", SpUtils.getString(R.string.user_id,""))
    }

    /**
     * 微信注册登录
     */
    fun setLoginWx() {
        MobclickAgent.onProfileSignIn("WEIXIN", SpUtils.getString(R.string.user_id,""))
    }


    /**
     * qq注册登录
     */
    fun setLoginQQ() {
        MobclickAgent.onProfileSignIn("QQ", SpUtils.getString(R.string.user_id,""))
    }

    /**
     * 非标准页面进行统计开始
     */
    fun setFragmentStart(tag:String){
        MobclickAgent.onPageStart(tag)
    }

    /**
     * 非标准页面进行统计结束
     */
    fun setFragmentEnd(tag:String){
        MobclickAgent.onPageEnd(tag)
    }

    /**
     * 云信自定义事件上传接口
     */
    fun setlog(title: String,map: MutableMap<String, Any>){
        MobclickAgent.onEventObject(BaseApp._instance, title, map)
//        EventBus.getDefault().post(TestEvent(title, map))
    }
}