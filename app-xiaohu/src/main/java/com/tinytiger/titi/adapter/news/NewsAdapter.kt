package com.tinytiger.titi.adapter.news

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned

import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil

import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2020/2/28 0028 上午 11:08
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 点评详情
 */
class NewsAdapter : BaseQuickAdapter<CommentDetailBean, BaseViewHolder>(R.layout.news_item_comment, null) {


    var author_id = "0"
    var user_id = ""
    override fun convert(holder: BaseViewHolder, mBean: CommentDetailBean) {

        if (holder.adapterPosition == 0) {
            holder.setGone(R.id.ivLine1, false)
            holder.setGone(R.id.ivLine2, true)
            holder.setGone(R.id.vLine, false)
        } else if (holder.adapterPosition == data.size - 1) {
            holder.setGone(R.id.ivLine1, true)
            holder.setGone(R.id.ivLine2, false)
            holder.setGone(R.id.vLine, true)
        } else {
            holder.setGone(R.id.ivLine1, true)
            holder.setGone(R.id.ivLine2, true)
            holder.setGone(R.id.vLine, false)
        }
        if (data.size == 1) {
            holder.setGone(R.id.ivLine2, false)
        }
        holder.getView<AvatarView>(R.id.avUser).setAvatar(mBean.avatar,mBean.master_type).setUserId(mBean.user_id)
        val tv_nickname = holder.getView<MedalTextView>(R.id.tvName)
        tv_nickname.setNickname(StringUtils.stringName( mBean.nickname))
        tv_nickname.setMedalIcon(mBean.medal_image)

        holder.setText(R.id.tvInfo, mBean.content)
        holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(mBean.create_time))
        holder.setText(R.id.tvRead, StringUtils.sizeToString(mBean.comment_num))

        if (author_id == mBean.user_id) {
            holder.setGone(R.id.comment_item_author, false)
        } else {
            holder.setGone(R.id.comment_item_author, true)
        }
        if (mBean.has_badge == 1) {
            holder.setGone(R.id.comment_item_hot, false)
        } else {
            holder.setGone(R.id.comment_item_hot, true)
        }

        if (mBean.user_id == user_id) {
            holder.setGone(R.id.iv_more, false)
            holder.setImageDrawable(R.id.iv_more, ContextCompat.getDrawable(context, R.mipmap.icon_delete_black))
        } else {
            holder.setGone(R.id.iv_more, true)
          //  holder.setImageDrawable(R.id.iv_more, ContextCompat.getDrawable(context, R.mipmap.icon_more_g))
        }
        val iv_more = holder.getView<View>(R.id.iv_more)
        iv_more.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return@setOnClickListener
            }

            if (mBean.user_id == user_id) {
                mListener?.onMoreClick(mBean, true, iv_more)
            } else {
                mListener?.onMoreClick(mBean, false, iv_more)
            }
        }

        val llComment = holder.getView<LinearLayout>(R.id.llComment)
        if (mBean.replys != null && mBean.replys.size > 0) {
            llComment.visibility = View.VISIBLE
            llComment.removeAllViews()
            var siz = mBean.replys.size - 1
            if (mBean.replys.size > 3) {
                siz = 2
            }

            for (i in 0..siz) {
                val txt = TextView(context)

                var nickname = "该用户已注销"
                if (mBean.replys[i].replysUserinfo != null && mBean.replys[i].replysUserinfo.nickname != null) {
                    nickname = mBean.replys[i].replysUserinfo.nickname
                }
                if (nickname.length > 8) {
                    nickname = nickname.substring(0, 8) + "..."
                }

                nickname = "$nickname: "
                val spannableString = SpannableString(nickname + mBean.replys[i].content)
                if (mBean.replys[i].content != null && mBean.replys[i].content.isNotEmpty()) {
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#333333")),
                        nickname.length,
                        spannableString.length,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
                txt.text = spannableString
                txt.maxLines = 2
                txt.ellipsize = TextUtils.TruncateAt.END
                llComment.addView(txt)
            }
            if (mBean.comment_num > 3) {
                val txt = TextView(context)
                txt.text = "查看全部${mBean.comment_num}条评论"
                txt.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                llComment.addView(txt)
            }

        } else {
            llComment.visibility = View.GONE
        }


        holder.getView<RelativeLayout>(R.id.rl_content).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return@setOnClickListener
            }

            mListener?.onItemClick(mBean)
        }

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(mBean.is_like, mBean.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            mListener?.onLikeClick(mBean)
            mBean.like_num=viewLike.like_num
            mBean.is_like=viewLike.is_like
        }

    }


    var mListener: OnGameReviewListener? = null

    interface OnGameReviewListener {
        fun onLikeClick(mBean: CommentDetailBean)
        fun onItemClick(mBean: CommentDetailBean)

        fun onMoreClick(mBean: CommentDetailBean, type: Boolean, view: View)
    }
}