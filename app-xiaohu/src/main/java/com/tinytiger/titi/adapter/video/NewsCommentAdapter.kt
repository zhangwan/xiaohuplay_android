package com.tinytiger.titi.adapter.video


import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.net.data.video.ReplysUserinfoBean
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
class NewsCommentAdapter :
    BaseQuickAdapter<CommentDetailBean, BaseViewHolder>(R.layout.item_news_comment, null) {

    var postUser = ""
    var answer_id = -1
    var author_id = "0"
    var lz_id = "0"

    override fun convert(holder: BaseViewHolder, item: CommentDetailBean) {
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
        tvDesc.clearTxt()
        tvDesc.create(ShowAllTextView.Builder().setText(item.content))
        tvDesc.setOnClickListener {
            val user = ReplysUserinfoBean()
            user.avatar = item.avatar
            user.nickname = item.nickname
            user.netease_id = item.netease_id
            user.user_id = item.user_id
            item.replysUserinfo = user
            mOnPostListener?.setReply(item)
        }

        val llComment = holder.getView<LinearLayout>(R.id.llComment)

        if (item.replys != null && item.replys.size > 0) {
            llComment.visibility = View.VISIBLE
            setllComment(llComment, item)
        } else {
            llComment.visibility = View.GONE
        }
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            mOnPostListener?.setPostRead(item)
        }

        val tvReply = holder.getView<TextView>(R.id.tvReply)
        val tvDelete = holder.getView<TextView>(R.id.tvDelete)
        val iv_delete = holder.getView<ImageView>(R.id.iv_delete)
        iv_delete.visibility = View.GONE
        tvDelete.visibility = View.GONE
        tvReply.visibility = View.GONE

        holder.setGone(R.id.ivReply, true)

        if (item.user_id == SpUtils.getString(R.string.user_id, "")) {
            iv_delete.visibility = View.VISIBLE
            iv_delete.setOnClickListener {
                mOnPostListener?.setDelete(item)
            }
        }

        when (item.user_id) {
            author_id -> {
                holder.setText(R.id.comment_item_author, "作者")
                holder.setGone(R.id.comment_item_author, false)
            }
            lz_id -> {
                holder.setText(R.id.comment_item_author, "楼主")
                holder.setGone(R.id.comment_item_author, false)
            }
            else -> {
                holder.setGone(R.id.comment_item_author, true)
            }
        }
        holder.setGone(R.id.comment_item_hot, item.has_badge == 1)

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            mOnPostListener?.setPostRead(item)
        }
    }

    private fun setllComment(llComment: LinearLayout, bean: CommentDetailBean) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        val item = bean.replys
        //计数最多显示2条回复
        var count = 0
        for (i in item) {
            count++
            if (count > 2) break
            val view = View.inflate(context, R.layout.post_item_comment_two, null)
            val tvName = view.findViewById<TextView>(R.id.tvName)

            var nickname = ""
            if (i.replysUserinfo != null) {
                GlideUtil.loadImg(view.findViewById(R.id.ivIcon), i.replysUserinfo.avatar)
                nickname = StringUtils.stringName(i.replysUserinfo.nickname)
            }
            if (i.commentUserinfo != null && !i.commentUserinfo.nickname.isNullOrEmpty()) {
                val sb =
                    SpannableString("$nickname 回复 @${StringUtils.stringName(i.commentUserinfo.nickname)}")
                sb.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.grayAA)),
                    nickname.length,
                    nickname.length + 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvName.text = sb
            } else {
                tvName.text = nickname
            }

            view.findViewById<TextView>(R.id.tvTime).text =
                TimeZoneUtil.getShortTimeShowString(i.create_time)
            view.findViewById<ShowAllTextView>(R.id.tvInfo)
                .create(ShowAllTextView.Builder().setText(i.content))
            llComment.addView(view)
        }

        if (bean.comment_num > 2 && item.size >= 2) {
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
        fun clickLike(comment_id: Int, is_like: Int)

        fun setPostRead(item: CommentDetailBean)
        fun setDelete(item: CommentDetailBean)
        fun setReply(item: CommentDetailBean)
    }

    fun addCommentItem(txt: String, comment_id: Int, post_id: String) {
        var bean = CommentDetailBean()
        bean.id = comment_id
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.nickname = SpUtils.getString(R.string.nickname, "")
        bean.avatar = SpUtils.getString(R.string.avatar, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.content = txt
        bean.master_type = SpUtils.getInt(R.string.master_type, 0)
        bean.medal_image = SpUtils.getString(R.string.medal_image, "")
        bean.is_like = -1
//        bean.lz_user_id = SpUtils.getString(R.string.user_id, "")
//        bean.like_num = 0

        data.add(0, bean)
    }

    fun delComment(comment_id: String) {
        for (i in data) {
            if (comment_id == i.id.toString()) {
                remove(i)
                break
            }
        }
    }

}