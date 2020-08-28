package com.tinytiger.titi.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.postsend.DraftDetailBean
import com.tinytiger.common.net.data.circle.postsend.FriendBean
import com.tinytiger.common.net.data.circle.postsend.SelectTopicBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.circle.post.TopicAdapter
import com.tinytiger.titi.data.circle.ImageBean
import com.tinytiger.titi.widget.popup.PopupGameInfo

/**
 * @author lmq001
 * @date 2020/06/22 09:48
 * @copyright 小虎互联科技
 * @doc UI展示封装工具类
 */
class UIHelper {

    /**
     * 设置列表Manager
     */
    fun initLayoutManager(context: Context, recyclerView: RecyclerView) {
        val manager = FlexboxLayoutManager(context)
        manager.flexDirection = FlexDirection.ROW
        manager.flexWrap = FlexWrap.WRAP
        manager.alignItems = AlignItems.STRETCH
        recyclerView.clipToPadding = false
        recyclerView.layoutManager = manager
    }

    /**
     * 组装帖子信息剩余字段数据
     */
    fun composePostBean(
        detail: PostBean?, circleName: String, moduleName: String, imgs: ArrayList<String>,
        circleType: Boolean, topicList: MutableList<SelectTopicBean>
    ): PostBean? {
        detail?.circle_name = circleName
        detail?.modular_name = moduleName
        detail?.img_url = imgs
        detail?.circleType = circleType
        detail?.circle_list = ArrayList<CircleBean>()
        if (topicList.size > 1) {
            for (item in topicList) {
                if (item.id > 0) {
                    var bean = CircleBean()
                    bean.post_id = detail?.id
                    bean.circle_id = item.id.toString()
                    bean.name = item.name
                    detail?.circle_list!!.add(bean)
                }
            }
        }
        return detail
    }

    /**
     * 组装拼接发布帖子的圈子，邀请回答的参数
     */
    fun composeAddPostParam(
        postType: Int,
        topicList: MutableList<SelectTopicBean>,
        imgsList: MutableList<ImageBean>,
        friendList: MutableList<FriendBean>
    ): PostBean {
        var bean = PostBean()
        var topic = ArrayList<Int>()
        var topic1 = ArrayList<String>()
        for (i in topicList) {
            if (i.id > 0) {
                topic!!.add(i.id)
            } else if (i.id == -1) {
                topic1!!.add(i.name)
            }
        }
        var circle_ids = ""
        var circle_name = ""
        if (topic!!.size > 0) circle_ids = JSON.toJSONString(topic)
        if (topic1!!.size > 0) circle_name = JSON.toJSONString(topic1)

        var imgs = ArrayList<String>()
        for (i in imgsList) {
            if (i.url.isNotEmpty()) imgs!!.add(i.url)
        }

        var users = ArrayList<Int>()
        if (postType == 1) {
            for (i in friendList) {
                if (i.user_id > 0) users!!.add(i.user_id)
            }
        }
        var user_ids = ""
        if (users!!.size > 0) user_ids = JSON.toJSONString(users)

        //参数结果
        bean.circle_id = circle_ids
        bean.circle_name = circle_name
        bean.user_id = user_ids
        bean.img_url = imgs

        return bean
    }

    /**
     * 显示话题圈子信息
     */
    fun showTopicData(bean: DraftDetailBean, topicAdapter: TopicAdapter) {
        topicAdapter.data.clear()
        if (bean.data.circle_ids != null && bean.data.circle_ids.size > 0) {
            topicAdapter.setNewInstance(bean.data.circle_ids)
        }
        if (bean.data.circle_names != null && bean.data.circle_names.size > 0) {
            topicAdapter.addTopicAdd(bean.data.circle_names)
        }
        topicAdapter.setCirclerNum()
        if (topicAdapter.itemCount < 3) {
            topicAdapter.addData(SelectTopicBean(0))
        }
    }

    var mPopupWindow: PopupGameInfo? = null

    /**
     * 设置删除弹框Popup
     */
    fun showPopupWindow(
        activity: Activity, rootView: View, info: String, listener: View.OnClickListener
    ) {
        if (mPopupWindow == null) {
            mPopupWindow = PopupGameInfo(activity)
            mPopupWindow!!.setShowAnimation(createTranslateAnimation(1f, 0f, 0f, 0f))
                .dismissAnimation = createTranslateAnimation(0f, 1f, 0f, 0f)
            mPopupWindow!!.popupGravity = Gravity.END
        }
        mPopupWindow?.setPopInfo(info) { v ->
            mPopupWindow?.dismiss()
            listener.onClick(v)
        }
        mPopupWindow?.setDrawableLeft(R.mipmap.icon_delete_black)

        val location = IntArray(2)
        rootView.getLocationInWindow(location)
        rootView.getLocationOnScreen(location)

        mPopupWindow!!.setBackground(null)
            .setBlurBackgroundEnable(false)
            .showPopupWindow(
                location[0] - Dp2PxUtils.dip2px(activity, 80),
                location[1] + Dp2PxUtils.dip2px(activity, 1)
            )
    }

    private fun createTranslateAnimation(
        fromX: Float, toX: Float, fromY: Float, toY: Float
    ): Animation? {
        val animation: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF, toX,
            Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY
        )
        animation.duration = 500
        animation.interpolator = DecelerateInterpolator()
        return animation
    }

}
