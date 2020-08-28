package com.tinytiger.titi.adapter.mine


import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.center.UserMedalBean
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 我的勋章 adapter
*/
class MineMeritListAdapter : BaseQuickAdapter<UserMedalBean, BaseViewHolder>(R.layout.mine_item_merit, null) {

    override fun convert(holder: BaseViewHolder, bean: UserMedalBean) {
        GlideUtil.loadImg(holder.getView(R.id.iv_avatar),bean.image)
        holder.setText(R.id.tv_name, bean.name)
        holder.setText(R.id.tv_desc, bean.explain)

        val card = holder.getView<View>(R.id.ll_content)

        //#是否拥有 0未拥有 1已拥有
        if (bean.is_have == 0) {
            card.background=ContextCompat.getDrawable(context,R.drawable.solid_rectangle_10_eeeeee)
            holder.setText(R.id.tv_action, "未拥有")
            holder.setTextColor(R.id.tv_action, Color.parseColor("#666666"))
            holder.setBackgroundResource(R.id.tv_action, R.drawable.solid_rectangle_15_ffffff)
        } else {
            // #是否佩戴 0 未佩戴 1已佩戴
            if (bean.is_use == 0) {
                card.background=ContextCompat.getDrawable(context,R.drawable.solid_rectangle_10_ffffff)
                holder.setText(R.id.tv_action, "佩戴")
                holder.setTextColor(R.id.tv_action, Color.parseColor("#333333"))
                holder.setBackgroundResource(R.id.tv_action, R.drawable.solid_rectangle_16_ffcc03_linear)
            } else {
                card.background=ContextCompat.getDrawable(context,R.drawable.stroke_rectangle_15_ffcc03_ffffff)
                holder.setText(R.id.tv_action, "卸下")
                holder.setTextColor(R.id.tv_action, Color.parseColor("#ffcc03"))
                holder.setBackgroundResource(R.id.tv_action, R.drawable.stroke_rectangle_16_ffcc03)
            }
        }

        card.setOnClickListener {
            setStartIntent2(bean)
        }
    }

    fun setStartIntent2(mAdBean: UserMedalBean) {
        //#0=无跳转 1=外部 2=功能页面 3=游戏详情 4=百科详情 5=词条 6=文章 7=视频 8=帖子
        when (mAdBean.jump_type) {
            1 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 1, mAdBean.jump_url))
                }
            }
            2 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 0, mAdBean.jump_url))
                }
            }
            3 -> {
                GameDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url,1)
            }
            4 -> {
                CirclesDetailsActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url,
                    ConstantsUtils.Page.PAGE_WIKI)
            }
            5 -> {
                GameWikiDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url)
            }
            6 -> {
                NewsDetailActivity.actionStart(
                    AppManager.getAppManager().currentActivity(),
                    "" + mAdBean.jump_url
                )
            }
            7 -> {
                VideoDetailActivity.actionStart(
                    AppManager.getAppManager().currentActivity(),
                    "" + mAdBean.jump_url,
                    ""
                )
            }
            8 -> {
                PostActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url)
            }
        }
    }
}