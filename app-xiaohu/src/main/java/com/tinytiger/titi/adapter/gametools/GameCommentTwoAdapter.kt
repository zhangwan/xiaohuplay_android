package com.tinytiger.titi.adapter.gametools


import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil

import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2020/3/3 0003 下午 5:49
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 评论2级展示
 */
class GameCommentTwoAdapter : BaseQuickAdapter<CommentAssessBean, BaseViewHolder>(R.layout.comment_item_layout2, null) {


    private var user_id = "0"
    //作者id
    var author_user_id = ""

    init {
        user_id = SpUtils.getString(R.string.user_id, "0")
    }

    override fun convert(helper: BaseViewHolder, item: CommentAssessBean) {
        if (helper.adapterPosition == 0) {
            helper.setGone(R.id.tvLoad, false)
        } else {
            helper.setGone(R.id.tvLoad, true)
        }

        helper.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar).setTalent(item.master_type)
            .setUserId(item.user_id)
        helper.getView<MedalTextView>(R.id.tvName).setNickname(StringUtils.stringName(item.nickname), item.medal_image)

        helper.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(item.create_time))

        val viewLike = helper.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            mOnExpandClickListener?.clickLike(item.id, item.is_like)
            item.like_num=viewLike.like_num
            item.is_like=viewLike.is_like
        }

        if (helper.adapterPosition == 0) {
            helper.setGone(R.id.tvRead, false)
            helper.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))
        } else {
            helper.setGone(R.id.tvRead, true)
        }

        when (item.user_id) {
            author_user_id -> {
                helper.setText(R.id.comment_item_author, "作者")
                helper.setGone(R.id.comment_item_author, false)
            }
            item.lz_user_id -> {
                helper.setText(R.id.comment_item_author, "楼主")
                helper.setGone(R.id.comment_item_author, false)
            }
            else -> {
                helper.setGone(R.id.comment_item_author, true)
            }
        }

        val sb = SpannableStringBuilder()
        if (item.commentUserinfo != null) {
            var lz = ""
            when (item.commentUserinfo.user_id) {
                author_user_id -> {
                    lz = "(作者)"
                }
                item.lz_user_id -> {
                    lz = "(楼主)"
                }
            }
            var nickname = "该用户已注销"
            if (item.commentUserinfo != null && item.commentUserinfo.nickname != null) {
                nickname = item.commentUserinfo.nickname
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
        sb.append(item.content)

        helper.setText(R.id.tvInfo, sb)


        if (item.user_id == user_id) {
            helper.setGone(R.id.comment_item_more, false)
            helper.setImageResource(R.id.comment_item_more, R.mipmap.icon_delete_black)
        } else {
            helper.setGone(R.id.comment_item_more, true)
           // helper.setImageResource(R.id.comment_item_more, R.mipmap.icon_more_g)
        }


        helper.getView<ImageView>(R.id.comment_item_more).setOnClickListener {
            if (item.user_id == user_id) {
                mOnExpandClickListener?.clickDelete(item.id)
            } else {
                mOnExpandClickListener?.clickMore(
                    helper.getView<ImageView>(R.id.comment_item_more),
                    item
                )
            }
        }

        helper.getView<TextView>(R.id.tvInfo).setOnClickListener {
            mOnExpandClickListener?.clickReply(item)
        }
    }

    private var mOnExpandClickListener: OnExpandClickListener? = null

    fun setOnExpandClickListener(listener: OnExpandClickListener) {
        this.mOnExpandClickListener = listener
    }

    interface OnExpandClickListener {
        //更多
        fun clickMore(view: View, item: CommentAssessBean)

        //点赞
        fun clickLike(comment_id: Int, is_like: Int)

        //删除
        fun clickDelete(comment_id: Int)

        //回复
        fun clickReply(item: CommentAssessBean)
    }


}