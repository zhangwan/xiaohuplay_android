package com.tinytiger.titi.adapter.gametools


import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.size
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.UserInfo
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.im.config.preference.Preferences
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
class GameReviewAdapter2 :
    BaseQuickAdapter<CommentAssessBean, BaseViewHolder>(R.layout.item_news_comment, null) {

    var postUser = ""
    var answer_id = -1
    var author_id = "0"
    var lz_id = "0"

    override fun convert(holder: BaseViewHolder, item: CommentAssessBean) {
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
            mOnPostListener?.onLikeClick(item)
            item.like_num = viewLike.like_num
            item.is_like = viewLike.is_like
        }
        val tvDesc = holder.getView<ShowAllTextView>(R.id.tvInfo)
        val ll_name = holder.getView<LinearLayout>(R.id.ll_name)
        tvDesc.clearTxt()
        tvDesc.create(ShowAllTextView.Builder().setText(item.content))
        ll_name.setOnClickListener {
            val user = UserInfo()
            user.avatar = item.avatar
            user.nickname = item.nickname
            user.netease_id = item.netease_id
            user.user_id = item.user_id
            item.replysUserinfo = user
            mOnPostListener?.onReplyClick(item, item)
        }

        val llComment = holder.getView<LinearLayout>(R.id.llComment)
        if (item.replys != null && item.replys.size > 0) {
            llComment.visibility = View.VISIBLE
            setllComment(llComment, item)
        } else {
            llComment.visibility = View.GONE
        }
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return@setOnClickListener
            }
            mOnPostListener?.onItemClick(item)
        }

        val tvReply = holder.getView<TextView>(R.id.tvReply)
        val tvDelete = holder.getView<View>(R.id.tvDelete)
        tvDelete.visibility = View.GONE
        tvReply.visibility = View.GONE
        holder.setGone(R.id.ivReply, true)

        if (item.user_id == SpUtils.getString(R.string.user_id, "000")) {
            holder.setGone(R.id.iv_more, false)
            holder.setImageDrawable(
                R.id.iv_more,
                ContextCompat.getDrawable(context, R.mipmap.icon_delete_black)
            )
        } else {
            holder.setGone(R.id.iv_more, true)
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
            var type = false
            if (item.user_id == SpUtils.getString(R.string.user_id, "000")) {
                type = true
                mOnPostListener?.onMoreClick(item, type, iv_more)
            }
        }

        if (item.user_id != null) {
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
        }
//        holder.setGone(R.id.comment_item_hot, item.has_badge == 1)

//        if (item.user_id == SpUtils.getString(R.string.user_id, "")) {
//            tvDelete.visibility = View.VISIBLE
//            tvDelete.setOnClickListener {
//                mOnPostListener?.onMoreClick(item)
//            }
//        }
//        else if (answer_id == 0) {
//            //题主采纳回答按钮
//            if (postUser == SpUtils.getString(R.string.user_id, "")) {
//                tvReply.visibility = View.VISIBLE
//                tvReply.setOnClickListener {
//                    val user = UserInfo()
//                    user.avatar = item.avatar
//                    user.nickname = item.nickname
//                    user.netease_id = item.netease_id
//                    user.user_id = item.user_id
//                    item.replysUserinfo = user
//                    mOnPostListener?.onReplyClick(item, item)
//                }
//            }
//        } else if ("" + answer_id == item.id.toString()) {
//            //采纳的回答
//            holder.setGone(R.id.ivReply, false)
//        }
        holder.getView<View>(R.id.rl_content).setOnClickListener {
            mOnPostListener?.onItemClick(item)
        }
    }

    private fun setllComment(llComment: LinearLayout, bean: CommentAssessBean) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        val item = bean.replys
        var index = 0
        for (i in item) {
            index++
            //最多显示2条回复
            if (index > 2) break
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
            var tvInfo = view.findViewById<ShowAllTextView>(R.id.tvInfo)
            tvInfo.clearTxt()
            tvInfo.create(ShowAllTextView.Builder().setText(i.content))
            llComment.addView(view)
        }

        if (bean.comment_num > 2 && llComment.size > 1) {
            val llMore = llComment[1].findViewById<View>(R.id.llMore)
            llMore.visibility = View.VISIBLE
            llMore.setOnClickListener {
                mOnPostListener?.onItemClick(bean)
            }
            llComment[1].findViewById<TextView>(R.id.tvMore).text = "共${bean.comment_num}条回复 >"
        }

    }

    var mOnPostListener: OnPostListener? = null

    interface OnPostListener {
        fun onLikeClick(mBean: CommentAssessBean)
        fun onItemClick(mBean: CommentAssessBean)
        fun onMoreClick(mBean: CommentAssessBean, type: Boolean, view: View)
        fun onReplyClick(mBean: CommentAssessBean, replys: CommentAssessBean)
    }

    /**
     * 怎么就评论
     * 一级评论
     */
    fun addBean(txt: String, comment_id: Int) {
        val bean = CommentAssessBean()
        bean.id = comment_id
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.nickname = SpUtils.getString(R.string.nickname, "")
        bean.avatar = SpUtils.getString(R.string.avatar, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.content = txt
        bean.master_type = SpUtils.getInt(R.string.master_type, 0)
        bean.medal_image = SpUtils.getString(R.string.medal_image, "")
        bean.is_like = -1
        bean.lz_user_id = SpUtils.getString(R.string.user_id, "")
        bean.like_num = 0

        val user = UserInfo()
        user.user_id = SpUtils.getString(R.string.user_id, "")
        user.nickname = SpUtils.getString(R.string.nickname, "")
        user.netease_id = Preferences.getUserAccount()
        user.avatar = SpUtils.getString(R.string.avatar, "")
        bean.replysUserinfo = user
        user.medal_image = SpUtils.getString(R.string.medal_image, "")
        user.master_type = SpUtils.getInt(R.string.master_type, 0)
        data.add(0, bean)

        notifyDataSetChanged()
    }

    /**
     * 二级评论
     */
    fun addBeanTwo(txt: String, comment_id: Int, mTopCommentId: Int, commentUserinfo: UserInfo?) {
        val bean = CommentAssessBean()
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.content = txt
        bean.lz_user_id = SpUtils.getString(R.string.user_id, "")
        bean.id = comment_id
        bean.comment_num += 1
        bean.medal_image = SpUtils.getString(R.string.medal_image, "")
        bean.master_type = SpUtils.getInt(R.string.master_type, 0)
        val user = UserInfo()
        user.user_id = SpUtils.getString(R.string.user_id, "")
        user.nickname = SpUtils.getString(R.string.nickname, "")
        user.netease_id = Preferences.getUserAccount()
        user.avatar = SpUtils.getString(R.string.avatar, "")
        user.medal_image = SpUtils.getString(R.string.medal_image, "")
        user.master_type = SpUtils.getInt(R.string.master_type, 0)
        bean.replysUserinfo = user
        //去除自己
        if (commentUserinfo != null && commentUserinfo.user_id != user.user_id) {
            bean.commentUserinfo = commentUserinfo
        }

        for (i in 0..data.size - 1) {
            if (data[i].id == mTopCommentId) {
                if (data[i].replys == null) {
                    data[i].replys = ArrayList()
                }

                data[i].replys.add(0, bean)
                data[i].comment_num++
                notifyItemChanged(i)
            }
        }
    }

    /**
     * 删除bean
     */
    fun setRemove(comment_id: Int) {
        for (i in 0..(data.size - 1)) {
            if (data[i].id == comment_id) {
                remove(i)
                break
            }
        }
    }

    fun delComment(comment_id: String) {
        for (i in data) {
            if (comment_id == i.id.toString()) {
                remove(i)
                break
            }
        }
    }

    /**
     * 更新点赞
     */
    fun setLike(comment_id: Int, islike: Int) {
        for (i in 0..(data.size - 1)) {
            if (data[i].id == comment_id) {
                data[i].is_like = islike
                data[i].like_num += islike
                notifyItemChanged(i)
                break
            }
        }
    }

    /**
     * 更新二级评论
     */
    fun setReplys(comment_id: Int, list: ArrayList<CommentAssessBean>) {
        for (i in 0..(data.size - 1)) {
            if (data[i].id == comment_id) {
                data[i].replys = list
                notifyItemChanged(i)
                break
            }
        }
    }

}