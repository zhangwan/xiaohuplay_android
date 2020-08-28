package com.tinytiger.titi.adapter.circle.post


import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2020/5/6 0006 下午 3:05
 * @Copyright 小虎互联科技
 * @since 3.0.0
 * @doc 二级评论列表
 */
class PostTwoAdapter :
    BaseQuickAdapter<PostData, BaseViewHolder>(R.layout.post_item_comment_two2, null) {

    override fun convert(holder: BaseViewHolder, item: PostData) {
        if (holder.adapterPosition == 0) {
            holder.setGone(R.id.vLine2, false)
            holder.setGone(R.id.vLine, true)
            holder.setGone(R.id.tvRead, false)
            holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))
        } else {
            holder.setGone(R.id.vLine2, false)
            holder.setGone(R.id.tvRead, true)
            holder.setGone(R.id.vLine2, true)
        }


        if (!item.reply_nickname.isNullOrEmpty()) {
            holder.setGone(R.id.tvName1, true)
            holder.setGone(R.id.tvName, false)
            val nickname = StringUtils.stringName(item.nickname)
            val sb = SpannableString("$nickname 回复 @${StringUtils.stringName(item.reply_nickname)}")
            sb.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.grayAA)),
                nickname.length,
                nickname.length + 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.setText(R.id.tvName, sb)
        } else {
            holder.setGone(R.id.tvName1, false)
            holder.setGone(R.id.tvName, true)
            holder.getView<MedalTextView>(R.id.tvName1).setNickname(item.nickname, item.medal_image)
        }

        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar).setTalent(item.master_type)
            .setUserId(item.user_id)

        holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(item.create_time))

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            mOnPostListener?.clickLike(item.id, item.is_like)
            item.like_num = viewLike.like_num
            item.is_like = viewLike.is_like
        }
        val tvDesc = holder.getView<ShowAllTextView>(R.id.tvInfo)
        tvDesc.create(ShowAllTextView.Builder().setText(item.content))

        val tvDelete = holder.getView<View>(R.id.tvDelete)
        if (holder.adapterPosition != 0 && item.user_id == SpUtils.getString(R.string.user_id, "")) {
            tvDelete.visibility = View.VISIBLE
            tvDelete.setOnClickListener {
                mOnPostListener?.setDelete(item)
            }
        } else {
            tvDelete.visibility = View.GONE
        }

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            mOnPostListener?.clickReply(item)
        }
    }

    var mOnPostListener: OnPostListener? = null

    interface OnPostListener {
        fun clickLike(comment_id: String, is_like: Int)

        fun clickReply(item: PostData)
        fun setDelete(item: PostData)
    }

    fun addCommentItem(txt: String, comment_id: Int, post_id: String, reply_nickname: String): PostData {
        val bean = PostData()
        bean.id = "" + comment_id
        bean.post_id = post_id
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.nickname = SpUtils.getString(R.string.nickname, "")
        bean.avatar = SpUtils.getString(R.string.avatar, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.content = txt
        bean.reply_nickname = reply_nickname
        bean.is_like = -1
        bean.master_type = SpUtils.getInt(R.string.master_type, 0)
        bean.medal_image = SpUtils.getString(R.string.medal_image, "")
        addData(1, bean)
        return bean
    }

    fun delComment(comment_id: String) {
        for (i in data) {
            if (comment_id == i.id) {
                remove(i)
                /* data[0].comment_num+=1
                 notifyItemChanged(0)*/
                break
            }
        }
    }


}