package com.tinytiger.titi.adapter.mine.user.home

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import cn.jzvd.JZUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.user.UserDynamicBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.circle.post.SelectFriendActivity
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.widget.view.Anim.LikeView

import com.tinytiger.titi.widget.view.Image9View


/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:11
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 用户主页-动态
 */
class MultiDyamicAdapter(data: ArrayList<UserDynamicBean>) :
    BaseMultiItemQuickAdapter<UserDynamicBean, BaseViewHolder>(data) {

    init {
        //#类型:1=评价 2=帖子
        addItemType(1, R.layout.item_homepage_dynamic)
        addItemType(2, R.layout.homepage_item_hot)
    }


    override fun convert(holder: BaseViewHolder, item: UserDynamicBean) {
        when (holder.itemViewType) {
            1 -> {
                GlideUtil.loadLogo(holder.getView(R.id.iv_avatar), item.logo)

                holder.setText(R.id.tv_game_name, item.game_name)
                holder.setText(R.id.tv_title, item.title)
                holder.setText(R.id.tv_desc, StringUtils.Html2Text(item.content))

                holder.setText(R.id.tv_time, TimeZoneUtil.getShortTimeShowString(item.create_time))
                holder.getView<AppCompatRatingBar>(R.id.rating_bar).rating = item.score.toFloat()
                holder.getView<View>(R.id.game_layout).setOnClickListener {
                    GameDetailActivity.actionStart(context, item.game_id, 0)
                }
                holder.getView<View>(R.id.cl_content).setOnClickListener {
                    GameReviewsActivity.actionStart(context, item.game_id, item.id)
                }
            }
            2 -> {
                holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(item.create_time))
                val ivView9 = holder.getView<Image9View>(R.id.ivView9)
                if (item.img_url != null && item.img_url.size > 0) {
                    ivView9.visibility = View.VISIBLE
                    ivView9.setImages(item.img_url)
                } else {
                    ivView9.visibility = View.GONE
                }
                var ivVideoCover = holder.getView<ImageView>(R.id.ivVideoCover)
                var tvTitle = holder.getView<TextView>(R.id.tvTitle)
                if (!TextUtils.isEmpty(item.video_url)) {
                    if (item.video_length == 0L) {
                        holder.setGone(R.id.tv_total, true)
                    } else {
                        holder.setGone(R.id.tv_total, false)
                        holder.getView<TextView>(R.id.tv_total).text = JZUtils.stringForTime(item.video_length * 1000)
                    }

                    holder.setGone(R.id.rl_video, false)
                    if (!TextUtils.isEmpty(item.cover_url)) {
                        GlideUtil.loadImg(ivVideoCover, item.cover_url)
                    } else {
                        ivVideoCover.setBackgroundResource(R.mipmap.icon_error_down3)
                    }
                } else {
                    holder.setGone(R.id.rl_video, true)
                }
                if (!TextUtils.isEmpty(item.title)) {
                    tvTitle.text = item.title
                    holder.setGone(R.id.tvTitle, false)
                } else {
                    holder.setGone(R.id.tvTitle, true)
                }

                holder.setGone(R.id.tvInvite, true)
                holder.setGone(R.id.tvName1, false)
                val tabs = ArrayList<String>()
                if (item.circle_list != null && item.circle_list.size > 0) {
                    for (i in item.circle_list) {
                        if (item.circle_id != i.circle_id) {
                            tabs.add("#${i.name}")
                        }
                    }
                }
                val builder = ShowAllTextView.Builder().setText(item.content).setLabelList(tabs)
                    .setOnClickListener { _, index, _ ->
                        CirclesDetailsActivity.actionStart(context, item.circle_list[index].circle_id,"","")
                    }

                isNodeVisible(item.answer_id, holder)
                var circleName = ""

                if (!TextUtils.isEmpty(item.circle_name)) {
                    circleName = "${item.circle_name}"
                }

                circleName += if (!TextUtils.isEmpty(item.modular_name)) {
                    if ("综合" == item.modular_name) {
                        ""
                    } else {
                        if (!TextUtils.isEmpty(circleName)) {
                            "-${item.modular_name}"
                        } else {
                            "${item.modular_name}"
                        }
                    }
                } else {
                    ""
                }
                holder.setText(R.id.tvName1, circleName)
                holder.setGone(R.id.tvName1, false)
                if (TextUtils.isEmpty(circleName) && TextUtils.isEmpty(item.modular_name)) {
                    holder.getView<View>(R.id.tvName1).visibility = View.INVISIBLE
                }
                when (item.answer_id) {
                    -1 -> {
                        builder.setType(ShowAllTextView.TEXT)
                        holder.setText(R.id.tvName, "发布了一条动态帖")
                    }
                    0 -> {
                        holder.setText(R.id.tvName, "发布了一条问答帖")
                        holder.getView<View>(R.id.tvInvite).setOnClickListener {
                            SelectFriendActivity.actionStart(context, item.id)
                        }
                        builder.setImgLabel("待解答").setType(ShowAllTextView.IMAGE_TEXT).isResolved(false)
                    }
                    else -> {
                        var participateNum = "${item.participate_num}人参与回答"
                        holder.setText(R.id.tvName, "发布了一条问答帖")
                        builder.setImgLabel("已解答").setType(ShowAllTextView.IMAGE_TEXT).isResolved(true)
                        holder.getView<TextView>(R.id.tvAnswerName).text = participateNum
                    }
                }

                holder.getView<ShowAllTextView>(R.id.tvDesc).create(builder)
                holder.getView<View>(R.id.ll_content).setOnClickListener {
                    PostActivity.actionStart(context, item.id)
                }

                holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))
                holder.setGone(R.id.vView, false)

                val viewLike = holder.getView<LikeView>(R.id.viewLike)
                viewLike.setLike(item.is_like, item.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    listener?.onLike(item)
                    item.like_num = viewLike.like_num
                    item.is_like = viewLike.is_like
                }
                ivVideoCover.setOnClickListener {
                    listener?.onViewVideo(item)
                }
                holder.getView<View>(R.id.ll_content).setOnClickListener {
                    PostActivity.actionStart(context, item.id)
                }

                holder.getView<View>(R.id.tvShare).setOnClickListener {
                    listener?.onShare(item)
                }
                holder.getView<View>(R.id.tvRead).setOnClickListener {
                    listener?.onRead(item)
                }
                holder.getView<View>(R.id.tvName1).setOnClickListener {
                    if (!TextUtils.isEmpty(item.circle_id)) {
                        CirclesDetailsActivity.actionStart(context, item.circle_id, item.modular_id,"")
                    }

                }
            }
        }
    }

    /**
     * 是否是问答帖
     * @param isAnswerType int -1综合 0-待解答 else 已解答
     */
    private fun isNodeVisible(isAnswerType: Int, holder: BaseViewHolder) {
        if (-1 == isAnswerType) {
            holder.getView<View>(R.id.tvShare).visibility = View.VISIBLE
            holder.getView<View>(R.id.tvRead).visibility = View.VISIBLE
            holder.getView<View>(R.id.viewLike).visibility = View.VISIBLE
            holder.getView<View>(R.id.tvInvite).visibility = View.GONE
            holder.getView<View>(R.id.tvAnswerName).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.tvShare).visibility = View.GONE
            holder.getView<View>(R.id.tvRead).visibility = View.GONE
            holder.getView<View>(R.id.viewLike).visibility = View.GONE
            if (0 == isAnswerType) { //待解答
                holder.getView<View>(R.id.tvInvite).visibility = View.VISIBLE
                holder.getView<View>(R.id.tvAnswerName).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.tvInvite).visibility = View.GONE
                holder.getView<View>(R.id.tvAnswerName).visibility = View.VISIBLE
            }
        }
    }

    interface OnHomePushListener {
        fun onShare(item: UserDynamicBean)
        fun onLike(item: UserDynamicBean)
        fun onRead(item: UserDynamicBean)
        fun onViewVideo(item:UserDynamicBean)
    }

    var listener: OnHomePushListener? = null

}