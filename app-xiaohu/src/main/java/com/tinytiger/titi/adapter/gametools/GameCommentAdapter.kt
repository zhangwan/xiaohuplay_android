package com.tinytiger.titi.adapter.gametools


import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.logger.Logger
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.gametools.GameCommentBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.umeng.GameAgentUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import com.tinytiger.titi.widget.view.Image9View
import org.greenrobot.eventbus.EventBus


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏详情 评论 adapter
*/
class GameCommentAdapter : BaseQuickAdapter<GameCommentBean, BaseViewHolder>(R.layout.game_item_comment, null) {

    var isRefresh = true
    var user_id = SpUtils.getString(R.string.user_id, "0")
    var game_id = ""
    override fun convert(holder: BaseViewHolder, item: GameCommentBean) {
        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar).setTalent(item.master_type)
            .setUserId(item.user_id)
        val tv_nickname = holder.getView<MedalTextView>(R.id.tv_name)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)

        if (item.title.isNotEmpty()){
            holder.setGone(R.id.tvTitle,false)
            holder.setText(R.id.tvTitle, item.title)
        }else{
            holder.setGone(R.id.tvTitle,true)
        }


        holder.setText(R.id.tv_time, TimeZoneUtil.getShortTimeShowString(item.create_time))
        holder.getView<AppCompatRatingBar>(R.id.rating_bar).rating = item.score.toFloat()

        holder.setText(R.id.tv_comment_num, item.comment_num.toString())
        holder.setText(R.id.tv_share_num, item.share_num.toString())


        holder.setText(R.id.tv_desc, StringUtils.toPlainText(item.content))
        val ivView9 = holder.getView<Image9View>(R.id.ivView9)
        val imagesHtml = StringUtils.getImageUrlsFromHtml(item.content)
        if (imagesHtml != null) {
            val images = ArrayList<String>()
            for (index in 0..imagesHtml.size - 1) {
                if (index < 3) {
                    images.add(imagesHtml[index])
                }
            }

            if (images.size > 0) {
                ivView9.visibility = View.VISIBLE
                ivView9.setImages_9(images)
            } else {
                ivView9.visibility = View.GONE
            }
        } else {
            ivView9.visibility = View.GONE
        }


        val avAttention = holder.getView<AttentionView>(R.id.avAttention)
        avAttention.setGameMutual(item.user_follow_status)
        if(item.user_id==SpUtils.getString(R.string.user_id, "")){
          avAttention.visibility=View.GONE
        }else{
            avAttention.visibility=View.VISIBLE
        }
        avAttention.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                listener?.onAttention(holder.adapterPosition, item)
            }
        }

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            listener?.onLike(item)
            if (viewLike.is_like == 1) {
                GameAgentUtils.setGameDetailInfo(game_id, item.id, 1,0)
            }
        }

        val llComment = holder.getView<LinearLayout>(R.id.llReply)
        if (item.comment_list != null && item.comment_list.size > 0) {
            setLlComment(llComment, item.comment_list)
        } else {
            llComment.visibility = View.GONE
        }

        holder.getView<View>(R.id.tv_share_num).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (MyUserData.isEmptyToken()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            listener?.onShare(item)
            GameAgentUtils.setGameDetailInfo(game_id,item.id,3,0)
        }

        holder.getView<View>(R.id.ll_content).setOnClickListener {
            isRefresh = true
            GameReviewsActivity.actionStart(
                context,
                game_id,
                item.id
            )
        }
    }


    interface OnGameCommentListener {
        fun onShare(mBean: GameCommentBean)
        fun onLike(mBean: GameCommentBean)
        fun onAttention(position: Int, mBean: GameCommentBean)
    }

    var listener: OnGameCommentListener? = null


    private fun setLlComment(llComment: LinearLayout, comment_list: java.util.ArrayList<CommentAssessBean>) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        var siz = comment_list.size - 1
        if (comment_list.size > 3) {
            siz = 2
        }

        for (i in 0..siz) {
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
            txt.maxLines = 1
            txt.ellipsize = TextUtils.TruncateAt.END

            llComment.addView(txt)
        }
    }
}