package com.tinytiger.titi.adapter.msg


import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.msg.MsgCommentBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil

import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2019/12/30 0030 上午 10:12
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 评论 回复页详情
 */
class MsgCommentAdapter :
    BaseQuickAdapter<MsgCommentBean, BaseViewHolder>(R.layout.comment_item_layout3, null) {

    private var user_id = "0"
    /**
     * 标记,回复高亮数据
     */
    var top_comment_id = 0
    //作者id
    var author_user_id = ""

    var typeShow = 1

    init {
        user_id = SpUtils.getString(R.string.user_id, "0")
    }

    override fun convert(holder: BaseViewHolder, bean: MsgCommentBean) {
        if (top_comment_id == bean.id) {
            holder.setBackgroundResource(R.id.ll_content, R.color.color_fff8d9)
        } else {
            holder.setBackgroundResource(R.id.ll_content, 0)
        }
        var nickname = "该用户已注销"
        if (bean.replysUserinfo != null && bean.replysUserinfo.nickname != null) {
            nickname = bean.replysUserinfo.nickname
            holder.getView<AvatarView>(R.id.avUser)
                .setAvatar(bean.replysUserinfo.avatar, bean.replysUserinfo.master_type)
                .setUserId(bean.replysUserinfo.user_id)
            if (nickname.length > 8) {
                nickname = nickname.substring(0, 8) + "..."
            }
            holder.getView<MedalTextView>(R.id.comment_item_userName)
                .setNickname(StringUtils.stringName(nickname), bean.replysUserinfo.medal_image)
        } else {
            holder.getView<MedalTextView>(R.id.comment_item_userName)
                .setNickname(StringUtils.stringName(nickname))
        }

        holder.setText(
            R.id.comment_item_time,
            TimeZoneUtil.getShortTimeShowString(bean.create_time)
        )

        when (bean.replysUserinfo.user_id) {
            author_user_id -> {
                holder.setText(R.id.comment_item_author, "作者")
                holder.setGone(R.id.comment_item_author, false)
            }
            bean.top_parent_user_id -> {
                holder.setText(R.id.comment_item_author, "楼主")
                holder.setGone(R.id.comment_item_author, false)
            }
            else -> {
                holder.setGone(R.id.comment_item_author, true)
            }
        }

        val sb = SpannableStringBuilder()
        if (bean.commentUserinfo != null) {
            var lz = ""
            when (bean.commentUserinfo.user_id) {
                author_user_id -> {
                    lz = "(作者)"
                }
                bean.top_parent_user_id -> {
                    lz = "(楼主)"
                }
            }

            var nickname = "该用户已注销"
            if (bean.commentUserinfo.nickname != null) {
                nickname = bean.commentUserinfo.nickname
            }
            if (nickname.length > 8) {
                nickname = nickname.substring(0, 8) + "..."
            }

            val commentUser = SpannableString(nickname + lz)
            commentUser.setSpan(
                ForegroundColorSpan(Color.GRAY),
                0,
                commentUser.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            sb.append("回复 ").append(commentUser).append("：")
        }
        sb.append(bean.content)

        holder.setText(R.id.comment_item_content, sb)


        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(bean.is_like, bean.like_num)
        viewLike.mListener =
            LikeView.OnLikeViewListener { mOnExpandClickListener?.clickLike(bean.id, bean.is_like) }
        holder.setGone(R.id.comment_item_more, bean.replysUserinfo.user_id != SpUtils.getString(R.string.user_id, "0"))
        /*if (bean.replysUserinfo.user_id == SpUtils.getString(R.string.user_id, "0")) {
            holder.setGone(R.id.comment_item_more, bean.replysUserinfo.user_id != SpUtils.getString(R.string.user_id, "0"))
        } else {
            holder.setGone(R.id.comment_item_more, true)
        }*/

        holder.getView<View>(R.id.comment_item_userName).setOnClickListener {
            HomepageActivity.actionStart(context, bean.replysUserinfo.user_id)
        }

        holder.getView<View>(R.id.comment_item_more).setOnClickListener {
            if (bean.replysUserinfo.user_id == user_id) {
                mOnExpandClickListener?.clickDelete(bean.id)
            }
        }

        holder.getView<View>(R.id.comment_item_content).setOnClickListener {
            mOnExpandClickListener?.clickReply(bean.id, bean.replysUserinfo.nickname)
        }
    }


    private var mOnExpandClickListener: OnExpandClickListener? = null

    fun setOnExpandClickListener(listener: OnExpandClickListener) {
        this.mOnExpandClickListener = listener
    }

    interface OnExpandClickListener {

        //点赞
        fun clickLike(comment_id: Int, is_like: Int)

        //删除
        fun clickDelete(comment_id: Int)

        //回复
        fun clickReply(comment_id: Int, reply_name: String)
    }


}