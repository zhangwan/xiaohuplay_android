package com.tinytiger.titi.adapter.home2


import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_安利文列表页
 */
class HomePushAdapter :
    BaseQuickAdapter<AmwayBean, BaseViewHolder>(R.layout.home_item_amway, null) {

    override fun convert(holder: BaseViewHolder, item: AmwayBean) {
        holder.getView<LinearLayout>(R.id.rl_content).setOnClickListener {
            GameReviewsActivity.actionStart(context, item.game_id, item.id)
        }
        holder.getView<RelativeLayout>(R.id.rlGameInfo).setOnClickListener {
            GameDetailActivity.actionStart(context, item.game_id,0)
        }

        val recyclerView = holder.getView<RecyclerView>(R.id.recycler_tab)
        if (item.viewpoint_list != null && item.viewpoint_list.size > 0) {
            recyclerView.visibility = View.VISIBLE
            val manager = FlexboxLayoutManager(context)
            manager.flexDirection = FlexDirection.ROW
            manager.flexWrap = FlexWrap.WRAP
            manager.alignItems = AlignItems.STRETCH
            recyclerView.clipToPadding = false
            recyclerView.layoutManager = manager
            val adapterFelxbox = HomeFelxboxAdapter()
            recyclerView.adapter = adapterFelxbox
            adapterFelxbox.setNewInstance(item.viewpoint_list)
        } else {
            recyclerView.visibility = View.GONE
        }


        holder.setGone(R.id.rl_top, false)

        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar,item.master_type).setUserId(item.user_id)
        val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)

        holder.getView<AppCompatRatingBar>(R.id.ratingBar).rating = Math.round(item.score).toFloat()

        val avAttention = holder.getView<AttentionView>(R.id.avAttention)
        avAttention.setMutual(item.follow)
        avAttention.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                listener?.onAttention(item)
            }
        }

        GlideUtil.loadImg(holder.getView(R.id.ivIcon1), item.thumbnail)
        holder.setText(R.id.tvGameName, item.game_name)
        holder.setText(R.id.tvGameSize, StringUtils.sizeToString(item.amway_assess_num) + "条安利")
        if (item.title.isNotEmpty()) {
            holder.setGone(R.id.tvTitle, false)
            holder.setText(R.id.tvTitle, item.title)
        } else {
            holder.setGone(R.id.tvTitle, true)
        }


        holder.getView<ShowAllTextView>(R.id.tvDesc)
            .create(ShowAllTextView.Builder().setText(StringUtils.toPlainText(item.content)))
        val llComment = holder.getView<LinearLayout>(R.id.llReply)
        if (item.comment_list != null && item.comment_list.size > 0) {
            setLlComment(llComment, item.comment_list)
        } else {
            llComment.visibility = View.GONE
        }

        holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener { listener?.onLike(item) }

        llComment.setOnClickListener {
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            GameReviewsActivity.actionStart(context, item.game_id, item.id, 1,1)
        }
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            GameReviewsActivity.actionStart(context, item.game_id, item.id, 1,1)
        }


        val tvShare = holder.getView<TextView>(R.id.tvShare)
        tvShare.text = StringUtils.sizeToString(item.share_num)
        tvShare.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            listener?.onShare(item)
        }
    }

/*
    private fun setMutual(is_mutual: Int, tvAttention: TextView) {
        //1:互相关注 0:已关注 -1:未关注 -2:自己
        tvAttention.visibility = View.VISIBLE
        when (is_mutual) {
            1 -> {
                tvAttention.isSelected = true
                tvAttention.text = "互相关注"
                tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
            }
            0 -> {
                tvAttention.isSelected = true
                tvAttention.text = "已关注"
                tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
            }
            -1 -> {
                tvAttention.isSelected = false
                tvAttention.text = "+ 关注"
                tvAttention.setTextColor(ContextCompat.getColor(context, R.color.gray33))
                tvAttention.background = ContextCompat.getDrawable(context, R.drawable.solid_gradient_15_ffcc03)
            }
            -2 -> {
                tvAttention.visibility = View.GONE
            }
            2 -> {
                tvAttention.visibility = View.GONE
            }
        }
    }
*/

    interface OnHomePushListener {
        fun onShare(item: AmwayBean)
        fun onLike(item: AmwayBean)
        fun onAttention(item: AmwayBean)
        fun onChecked(is_check: Boolean)
    }

    var listener: OnHomePushListener? = null


    private fun setLlComment(llComment: LinearLayout, comment_list: java.util.ArrayList<CommentAssessBean>) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        for (i in 0..comment_list.size - 1) {
            val txt = TextView(context)

            var nickname = "该用户已注销"
            if (comment_list[i].nickname != null) {
                nickname = comment_list[i].nickname
            }
            if (nickname.length > 8) {
                nickname = nickname.substring(0, 8) + "..."
            }

            txt.setTextColor(ContextCompat.getColor(context, R.color.gray66))
            txt.text = "$nickname: ${comment_list[i].content}"
            txt.textSize = 14.toFloat()
            txt.maxLines = 1
            txt.ellipsize = TextUtils.TruncateAt.END

            llComment.addView(txt)
        }
    }
}
