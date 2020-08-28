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
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.StringFormatUtil


/*
* @author Tamas
* create at 2019/12/1 0001
* Email: ljw_163mail@163.com
* description: 
*/
class MyCommentAdapter(data: ArrayList<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {

    companion object {
        val TYPE_LEVEL_0 = 0 //Expand parent
        val TYPE_LEVEL_1 = 1 //Expand child
        val TYPE_LEVEL_2 = 2 //Expand line
        val TYPE_LEVEL_3 = 3 //Expand empty
    }

    var author_id = "0"
    var lz_id = "0"

    var user_id = "0"

    init {
        addItemType(TYPE_LEVEL_0, R.layout.item_comment_group)
        addItemType(TYPE_LEVEL_1, R.layout.item_comment_child)
        addItemType(TYPE_LEVEL_2, R.layout.view_line)
        addItemType(TYPE_LEVEL_3, R.layout.comment_item_empty)

    }


    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (helper.itemViewType) {
            TYPE_LEVEL_0 -> {
                user_id = SpUtils.getString(R.string.user_id,"")

                val bean = item as CommentDetailBean
                GlideUtil.loadImg(helper.getView(R.id.item_logo), bean.avatar, R.mipmap.icon_person_head)


                helper.setText(R.id.item_username, StringUtils.stringName(bean.nickname))
                helper.setGone(R.id.item_hot, bean.has_badge == 1)

                if(bean.create_time == null || bean.create_time.isEmpty()){
                    helper.setText(R.id.item_time,"刚刚")
                }else{
                    helper.setText(R.id.item_time, TimeZoneUtil.getShortTimeShowString(bean.create_time))
                }

                bean.like_num =  if(bean.like_num<0) 0 else bean.like_num
                helper.setText(R.id.item_like_num, bean.like_num.toString())
                helper.setText(R.id.item_comment_num, if (bean.comment_num > 0) bean.comment_num.toString() else "")


                when (bean.user_id) {
                    author_id -> {
                        helper.setText(R.id.item_author, "作者")
                        helper.setGone(R.id.item_author, true)
                    }
                    lz_id -> {
                        helper.setText(R.id.item_author, "楼主")
                        helper.setGone(R.id.item_author, true)
                    }
                    else -> {
                        helper.setGone(R.id.item_author, false)
                    }
                }

                if (bean.content.length < 50) {
                    helper.setText(R.id.item_content, bean.content)
                } else {
                    StringFormatUtil.toggleEllipsize(
                        context, helper.getView(R.id.item_content), 6, bean.content,
                        " [全部] ", R.color.yellow, false
                    )
                }

                helper.getView<TextView>(R.id.item_like_num).isSelected = bean.is_like == 1
                helper.setImageResource(R.id.item_more,if(bean.user_id == user_id)  R.mipmap.icon_delete_black else R.mipmap.icon_more_g)

             //   helper.addOnClickListener(R.id.item_logo)
             //   helper.addOnClickListener(R.id.item_username)
             //   helper.addOnClickListener(R.id.item_like_num)
             ///   helper.addOnClickListener(R.id.item_comment_num)
             //   helper.addOnClickListener(R.id.item_content)
             //   helper.addOnClickListener(R.id.item_more)


                /* helper.getView<ImageView>(R.id.comment_item_logo).setOnClickListener {
                     mOnExpandClickListener?.clickUser(bean.user_id)
                 }
                 helper.getView<TextView>(R.id.comment_item_userName).setOnClickListener {
                     mOnExpandClickListener?.clickUser(bean.user_id)
                 }
                 helper.getView<ImageView>(R.id.comment_item_like).setOnClickListener {
                     mOnExpandClickListener?.clickLike(bean.id,bean.is_like)
                 }

                 helper.getView<ImageView>(R.id.comment_item_more).setOnClickListener {
                     if (bean.user_id == user_id){
                         mOnExpandClickListener?.clickDelete(helper.adapterPosition,bean.id)
                     }else{
                         mOnExpandClickListener?.clickMore(helper.getView<ImageView>(R.id.comment_item_more),bean.id,bean.user_id,bean.netease_id,bean.is_black)
                     }
                 }

                 helper.getView<ImageView>(R.id.comment_item_comment).setOnClickListener {
                     if(bean.nickname!=null){
                         mOnExpandClickListener?.clickReply(bean.id,"",bean.user_id)
                     }

                 }
                 helper.getView<TextView>(R.id.comment_item_content).setOnClickListener {
                     if(bean.nickname!=null) {
                         mOnExpandClickListener?.clickReply(bean.id,"",bean.user_id)
                     }
                 }*/

            }
            TYPE_LEVEL_1 -> {
                val bean = item as ReplyDetailBean

                if (bean.id == 0) {
                    helper.setTextColor(R.id.item_content, Color.parseColor("#FFCC03"))
                    helper.setText(R.id.item_content, bean.content)
                } else {
                    helper.setTextColor(R.id.item_content, Color.parseColor("#282828"))
                    val textSb = SpannableStringBuilder()

                    var nickname = "该用户已注销"
                    if (bean.replysUserinfo != null&&bean.replysUserinfo.nickname != null) {
                        nickname = bean.replysUserinfo.nickname
                    }
                    if (nickname.length > 8) {
                        nickname = nickname.substring(0, 8) + "..."
                    }

                    val replyUserName = SpannableString(nickname)

                    replyUserName.setSpan(ForegroundColorSpan(Color.GRAY), 0, replyUserName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textSb.append(replyUserName)

                    if (bean.commentUserinfo != null) {
                        val lastReply = SpannableString(bean.commentUserinfo.nickname)
                        lastReply.setSpan(ForegroundColorSpan(Color.GRAY), 0, lastReply.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        textSb.append(" 回复 ").append(lastReply)
                    }
                    helper.setText(R.id.item_content, textSb.append("：").append(bean.content))

                }


            }
            TYPE_LEVEL_2 -> {

            }
            TYPE_LEVEL_3 -> {

            }

        }
    }



}