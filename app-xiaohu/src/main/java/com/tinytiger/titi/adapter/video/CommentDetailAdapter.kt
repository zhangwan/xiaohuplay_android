package com.tinytiger.titi.adapter.video

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity

import com.tinytiger.common.net.data.msg.CommentBeanMulti
import com.tinytiger.common.net.data.video.CommentDetailBean
import com.tinytiger.common.net.data.video.ReplyDetailBean
import com.tinytiger.common.utils.StringUtils

import com.tinytiger.common.utils.TimeZoneUtil

import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/*
* @author Tamas
* create at 2019/12/1 0001
* Email: ljw_163mail@163.com
* description: 
*/
class CommentDetailAdapter(data: ArrayList<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    companion object {
        val TYPE_LEVEL_0 = 0 //Expand parent
        val TYPE_LEVEL_1 = 1 //Expand child
        val TYPE_LEVEL_2 = 2 //Expand info
        val TYPE_LEVEL_3 = 3 //Expand empty
    }

    var author_id = "0"
    var lz_id = "0"
    var top_comment_id = 0


    init {
        addItemType(TYPE_LEVEL_0, R.layout.comment_item_layout1)
        addItemType(TYPE_LEVEL_1, R.layout.comment_item_layout1)
        addItemType(TYPE_LEVEL_2, R.layout.comment_item_info)
        addItemType(TYPE_LEVEL_3, R.layout.comment_item_empty)

    }


    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            TYPE_LEVEL_0 -> {
                val bean = item as CommentDetailBean

                helper.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar,bean.master_type).setUserId(bean.user_id)
                val user_id = SpUtils.getString(R.string.user_id, "0")

                helper.getView<MedalTextView>(R.id.comment_item_userName).setNickname(StringUtils.stringName(bean.nickname),bean.medal_image).setUserId(bean.user_id)


                helper.setText(R.id.comment_item_content, bean.content)
                helper.setText(R.id.comment_item_time, TimeZoneUtil.getShortTimeShowString(bean.create_time))

                helper.setGone(R.id.comment_item_hot, bean.has_badge == 1)
                helper.setGone(R.id.divider, true)

                if (bean.comment_num > 0) {
                    helper.setText(R.id.comment_item_comment_num, bean.comment_num.toString())
                } else {
                    helper.setText(R.id.comment_item_comment_num, "")
                }

                val viewLike = helper.getView<LikeView>(R.id.viewLike)
                viewLike.setLike(bean.is_like, bean.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    mOnExpandClickListener?.clickLike(bean.id, bean.is_like)

                    bean.like_num = viewLike.like_num
                    bean.is_like = viewLike.is_like
                }

                if (bean.user_id == user_id) {
                    helper.setGone(R.id.comment_item_more, false)
                    helper.setImageResource(R.id.comment_item_more, R.mipmap.icon_delete_black)
                } else {
                    helper.setGone(R.id.comment_item_more, true)
                  //  helper.setImageResource(R.id.comment_item_more, R.mipmap.icon_more_g)
                }

                when (bean.user_id) {
                    author_id -> {
                        helper.setText(R.id.comment_item_author, "作者")
                        helper.setGone(R.id.comment_item_author, false)
                    }
                    lz_id -> {
                        helper.setText(R.id.comment_item_author, "楼主")
                        helper.setGone(R.id.comment_item_author, false)
                    }
                    else -> {
                        helper.setGone(R.id.comment_item_author, true)
                    }
                }


                helper.getView<ImageView>(R.id.comment_item_more).setOnClickListener {
                    if (bean.user_id == user_id) {
                        mOnExpandClickListener?.clickDelete(helper.adapterPosition, bean.id)
                    } else {
                        mOnExpandClickListener?.clickMore(
                            helper.getView<ImageView>(R.id.comment_item_more),
                            bean.id,
                            bean.user_id,
                            bean.netease_id,
                            bean.is_black
                        )
                    }
                }

                helper.getView<ImageView>(R.id.comment_item_comment).setOnClickListener {
                    if (bean.nickname != null) {
                        mOnExpandClickListener?.clickReply(bean.id, "", bean.user_id)
                    }

                }
                helper.getView<TextView>(R.id.comment_item_content).setOnClickListener {
                    if (bean.nickname != null) {
                        mOnExpandClickListener?.clickReply(bean.id, "", bean.user_id)
                    }
                }

            }
            TYPE_LEVEL_1 -> {
                val user_id = SpUtils.getString(R.string.user_id, "0")
                val bean = item as ReplyDetailBean

                if (top_comment_id > 0) {
                    top_comment_id = 0
                    helper.setBackgroundResource(R.id.ll_content, R.drawable.solid_rectangle_2_f3f3f3)
                } else {
                    helper.setBackgroundResource(R.id.ll_content, 0)
                }

                helper.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar,bean.master_type).setUserId(bean.user_id)
                helper.getView<MedalTextView>(R.id.comment_item_userName).setNickname(StringUtils.stringName(bean.nickname),bean.medal_image).setUserId(bean.user_id)


                if (bean.create_time == null || bean.create_time.isEmpty()) {
                    helper.setText(R.id.comment_item_time, "刚刚")
                } else {
                    helper.setText(R.id.comment_item_time, TimeZoneUtil.getShortTimeShowString(bean.create_time))
                }


                helper.setGone(R.id.comment_item_comment_num, true)
                helper.setGone(R.id.comment_item_comment, true)
                helper.setGone(R.id.divider, false)

                when (bean.user_id) {
                    author_id -> {
                        helper.setText(R.id.comment_item_author, "作者")
                        helper.setGone(R.id.comment_item_author, false)
                    }
                    lz_id -> {
                        helper.setText(R.id.comment_item_author, "楼主")
                        helper.setGone(R.id.comment_item_author, false)
                    }
                    else -> {
                        helper.setGone(R.id.comment_item_author, true)
                    }
                }


                val sb = SpannableStringBuilder()
                if (bean.commentUserinfo != null) {
                    val text = when (bean.commentUserinfo.user_id) {
                        author_id -> "(作者)"
                        lz_id -> "(楼主)"
                        else -> ""
                    }

                    val commentUser = SpannableString(bean.commentUserinfo.nickname + text)
                    commentUser.setSpan(
                        ForegroundColorSpan(Color.GRAY),
                        0,
                        commentUser.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    sb.append("回复 ").append(commentUser).append("：")

                }
                sb.append(bean.content)

                helper.setText(R.id.comment_item_content, sb)

                val viewLike = helper.getView<LikeView>(R.id.viewLike)
                viewLike.setLike(bean.is_like, bean.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    mOnExpandClickListener?.clickLike(bean.id, bean.is_like)
                    bean.like_num = viewLike.like_num
                    bean.is_like = viewLike.is_like
                }


                if (bean.user_id == user_id) {
                    helper.setGone(R.id.comment_item_more, false)
                    helper.setImageResource(R.id.comment_item_more, R.mipmap.icon_delete_black)
                } else {
                    helper.setGone(R.id.comment_item_more, true)
                    //  helper.setImageResource(R.id.comment_item_more, R.mipmap.icon_more_g)
                }

                helper.getView<ImageView>(R.id.comment_item_more).setOnClickListener {
                    if (bean.user_id == user_id) {
                        mOnExpandClickListener?.clickDelete(helper.adapterPosition, bean.id)
                    } else {
                        mOnExpandClickListener?.clickMore(
                            helper.getView<ImageView>(R.id.comment_item_more),
                            bean.id,
                            bean.user_id,
                            bean.netease_id,
                            bean.is_black
                        )
                    }
                }

                helper.getView<TextView>(R.id.comment_item_content).setOnClickListener {
                    mOnExpandClickListener?.clickReply(bean.id, bean.nickname, bean.user_id)
                }

            }
            TYPE_LEVEL_2 -> {

                val bean = item as CommentBeanMulti
                helper.setText(R.id.tv_info, bean.defaulttxt)
            }
            TYPE_LEVEL_3 -> {

                val bean = item as CommentBeanMulti
                helper.setText(R.id.tv_info, bean.defaulttxt)
            }

        }
    }


    private var mOnExpandClickListener: OnExpandClickListener? = null

    fun setOnExpandClickListener(listener: OnExpandClickListener) {
        this.mOnExpandClickListener = listener
    }

    interface OnExpandClickListener {

        fun clickUser(user_id: String)
        fun clickMore(view: View, comment_id: Int, user_id: String, netease_id: String?, is_black: Int)
        fun clickLike(comment_id: Int, is_like: Int)
        fun clickDelete(position: Int, comment_id: Int)
        fun clickReply(comment_id: Int, reply_name: String, reply_user_id: String)
    }


}