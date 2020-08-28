package com.tinytiger.titi.adapter.circle.post


import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.logger.Logger
import com.tinytiger.common.net.data.circle.post.PostData
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
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
 * @doc 一级评论列表
 */
class PostAdapter :
    BaseQuickAdapter<PostData, BaseViewHolder>(R.layout.post_item_comment, null) {

    var postUser = ""
    var answer_id = -1
    override fun convert(holder: BaseViewHolder, item: PostData) {
        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar).setTalent(item.master_type)
            .setUserId(item.user_id)
        val tv_nickname = holder.getView<MedalTextView>(R.id.tvName)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)
        holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(item.create_time))
        holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))
        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            mOnPostListener?.clickLike(item.id, item.is_like)
            item.like_num = viewLike.like_num
            item.is_like = viewLike.is_like
        }
        val tvDesc = holder.getView<ShowAllTextView>(R.id.tvInfo)
        tvDesc.create(ShowAllTextView.Builder().setText(item.content))
        val llComment = holder.getView<LinearLayout>(R.id.llComment)

        if (item.reply_list != null && item.reply_list.size > 0) {
            llComment.visibility = View.VISIBLE
            setllComment(llComment, item)
        } else {
            llComment.visibility = View.GONE
        }
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            mOnPostListener?.setPostRead(item)
        }

        val tvReply = holder.getView<TextView>(R.id.tvReply)
        val tvDelete = holder.getView<View>(R.id.tvDelete)
        tvDelete.visibility = View.GONE
        tvReply.visibility = View.GONE

        holder.setGone(R.id.ivReply, true)

        if (item.user_id == SpUtils.getString(R.string.user_id, "")) {
            tvDelete.visibility = View.VISIBLE
            tvDelete.setOnClickListener {
                mOnPostListener?.setDelete(item)
            }
        } else if (answer_id == 0) {
            //题主采纳回答按钮
            if (postUser == SpUtils.getString(R.string.user_id, "")) {
                tvReply.visibility = View.VISIBLE
                tvReply.setOnClickListener {
                    mOnPostListener?.setReply(item)
                }
            }
        } else if ("" + answer_id == item.id) {
            //采纳的回答
            holder.setGone(R.id.ivReply, false)
        }
        holder.getView<View>(R.id.rl_content).setOnClickListener {
            mOnPostListener?.setPostRead(item)
        }
    }

    private fun setllComment(llComment: LinearLayout, bean: PostData) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        val item = bean.reply_list
        for (i in item) {
            val view = View.inflate(context, R.layout.post_item_comment_two, null)
            GlideUtil.loadImg(view.findViewById(R.id.ivIcon), i.avatar)
            val tvName = view.findViewById<TextView>(R.id.tvName)

            if (!i.reply_nickname.isNullOrEmpty()) {
                val nickname=StringUtils.stringName(i.nickname)
                val sb = SpannableString("$nickname 回复 @${StringUtils.stringName(i.reply_nickname)}")
                sb.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.grayAA)),
                    nickname.length,
                    nickname.length + 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvName.text = sb
            } else {
                tvName.text =i.nickname
            }
            view.findViewById<TextView>(R.id.tvTime).text= TimeZoneUtil.getShortTimeShowString(i.create_time)
            view.findViewById<ShowAllTextView>(R.id.tvInfo).create(ShowAllTextView.Builder().setText(i.content))
            llComment.addView(view)
        }

        if (bean.comment_num > 2&&item.size>=2) {
            val llMore = llComment[1].findViewById<View>(R.id.llMore)
            llMore.visibility = View.VISIBLE
            llMore.setOnClickListener {
                mOnPostListener?.setPostRead(bean)
            }
            llComment[1].findViewById<TextView>(R.id.tvMore).text = "共${bean.comment_num}条回复 >"
        }

    }

    var mOnPostListener: OnPostListener? = null

    interface OnPostListener {
        fun clickLike(comment_id: String, is_like: Int)

        fun setPostRead(item: PostData)
        fun setDelete(item: PostData)
        fun setReply(item: PostData)
    }

    fun addCommentItem(txt: String, comment_id: Int, post_id: String) {
        val bean = PostData()
        bean.id = "" + comment_id
        bean.post_id = post_id
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.nickname = SpUtils.getString(R.string.nickname, "")
        bean.avatar = SpUtils.getString(R.string.avatar, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.content = txt
        bean.master_type=SpUtils.getInt(R.string.master_type,0)
        bean.medal_image=SpUtils.getString(R.string.medal_image,"")
        bean.is_like = -1

        addData(0, bean)

    }

    fun delComment(comment_id: String) {
        for (i in data) {
            if (comment_id == i.id) {
                remove(i)
                break
            }
        }
    }

}