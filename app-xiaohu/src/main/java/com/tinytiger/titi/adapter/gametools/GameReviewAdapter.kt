package com.tinytiger.titi.adapter.gametools

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import kotlin.collections.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2020/2/28 0028 上午 11:08
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc 点评详情
 */
class GameReviewAdapter : BaseQuickAdapter<CommentAssessBean, BaseViewHolder>(R.layout.gametools_item_comment, null) {

    var user_id = ""
    override fun convert(holder: BaseViewHolder, mBean: CommentAssessBean) {
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
        holder.getView<AvatarView>(R.id.avUser).setAvatar(mBean.avatar).setTalent(mBean.master_type)
            .setUserId(mBean.user_id)

        holder.getView<MedalTextView>(R.id.tvName).setNickname(mBean.nickname).setMedalIcon(mBean.medal_image)

        val tvDesc = holder.getView<ShowAllTextView>(R.id.tvInfo)
        tvDesc.create(ShowAllTextView.Builder().setText(mBean.content))
        tvDesc.setOnClickListener {
            val user = UserInfo()
            user.avatar = mBean.avatar
            user.nickname = mBean.nickname
            user.netease_id = mBean.netease_id
            user.user_id = mBean.user_id
            mBean.replysUserinfo = user
            mListener?.onReplyClick(mBean, mBean)
        }

        holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(mBean.create_time))
        // holder.setText(R.id.tvRead, StringUtils.sizeToString(mBean.comment_num))
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            if (!MyNetworkUtil.isNetworkAvailable(context)) {
                ToastUtils.show(context, "当前网络不可用")
                return@setOnClickListener
            }
            mListener?.onItemClick(mBean)
        }


        if (mBean.user_id == user_id) {
            holder.setGone(R.id.iv_more, false)
            holder.setImageDrawable(R.id.iv_more, ContextCompat.getDrawable(context, R.mipmap.icon_delete_black))
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
            if (mBean.user_id == user_id) {
                type = true
                mListener?.onMoreClick(mBean, type, iv_more)
            }
        }

        val llComment = holder.getView<LinearLayout>(R.id.llComment)
        if (mBean.replys != null && mBean.replys.size > 0) {
            llComment.visibility = View.VISIBLE
            setllComment(llComment, mBean, 2)
        } else {
            llComment.visibility = View.GONE
        }

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(mBean.is_like, mBean.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            mListener?.onLikeClick(mBean)
            mBean.like_num = viewLike.like_num
            mBean.is_like = viewLike.is_like
        }
    }


    var mListener: OnGameReviewListener? = null

    interface OnGameReviewListener {
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
        bean.user_id = SpUtils.getString(R.string.user_id, "")
        bean.create_time = TimeZoneUtil.formatTime2(System.currentTimeMillis())
        bean.avatar = SpUtils.getString(R.string.avatar, "")
        bean.content = txt
        bean.lz_user_id = SpUtils.getString(R.string.user_id, "")
        bean.like_num=0
        bean.id = comment_id
        bean.is_like=-1
        bean.medal_image=SpUtils.getString(R.string.medal_image,"")
        bean.master_type=SpUtils.getInt(R.string.master_type,0)
        val user = UserInfo()
        user.user_id = SpUtils.getString(R.string.user_id, "")
        user.nickname = SpUtils.getString(R.string.nickname, "")
        user.netease_id = Preferences.getUserAccount()
        user.avatar = SpUtils.getString(R.string.avatar, "")
        bean.replysUserinfo = user
        user.medal_image=SpUtils.getString(R.string.medal_image,"")
        user.master_type=SpUtils.getInt(R.string.master_type,0)
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
        bean.medal_image=SpUtils.getString(R.string.medal_image,"")
        bean.master_type=SpUtils.getInt(R.string.master_type,0)
        val user = UserInfo()
        user.user_id = SpUtils.getString(R.string.user_id, "")
        user.nickname = SpUtils.getString(R.string.nickname, "")
        user.netease_id = Preferences.getUserAccount()
        user.avatar = SpUtils.getString(R.string.avatar, "")
        user.medal_image=SpUtils.getString(R.string.medal_image,"")
        user.master_type=SpUtils.getInt(R.string.master_type,0)
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


    private fun setllComment(llComment: LinearLayout, item: CommentAssessBean, size: Int) {
        val list = item.replys
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()

        var siz = list.size - 1
        if (item.comment_num > 3) {
            siz = 1
        }
        if (size < 0) {
            siz = list.size - 1
        }

        for (i in 0..siz) {
            val view = View.inflate(context, R.layout.gametools_item_comment_two, null)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            if (list[i].replysUserinfo!=null&&list[i].replysUserinfo.nickname!=null){
                GlideUtil.loadImg(view.findViewById(R.id.ivIcon), list[i].replysUserinfo.avatar)
                tvName.text = StringUtils.stringName(list[i].replysUserinfo.nickname)
                tvName.setOnClickListener {
                    HomepageActivity.actionStart(context, list[i].replysUserinfo.user_id)
                }

            }else{
                tvName.text = "该用户已注销"
            }


            view.findViewById<View>(R.id.ivIcon).setOnClickListener {
                HomepageActivity.actionStart(context, list[i].replysUserinfo.user_id)
            }
            val tvDesc = view.findViewById<ShowAllTextView>(R.id.tvDesc)
            if (list[i].commentUserinfo != null) {
                val nickname = StringUtils.stringName(list[i].commentUserinfo.nickname)
                val sb = SpannableString("回复${nickname} :${list[i].content}")
                sb.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray33)),
                    3,
                    nickname.length + 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvDesc.create(ShowAllTextView.Builder().setText(sb))
            } else {
                tvDesc.create(ShowAllTextView.Builder().setText(list[i].content))
            }
            tvDesc.setOnClickListener {
                mListener?.onReplyClick(item, list[i])
            }
            val vLine = view.findViewById<View>(R.id.vLine)
            val tvMore = view.findViewById<TextView>(R.id.tvMore)
            vLine.visibility = View.VISIBLE
            if (siz == 1 && i == 1 && item.comment_num > 2) {
                tvMore.visibility = View.VISIBLE
                tvMore.text = "剩余${item.comment_num - 2}条回复 >"
                tvMore.setOnClickListener {
                    if (item.comment_num > 5) {
                        if (FastClickUtil.isFastClickTiming()) {
                            return@setOnClickListener
                        }
                        if (!MyNetworkUtil.isNetworkAvailable(context)) {
                            ToastUtils.show(context, "当前网络不可用")
                            return@setOnClickListener
                        }
                        mListener?.onItemClick(item)
                    } else {
                        setllComment(llComment, item, -1)
                    }
                }
            } else {
                tvMore.visibility = View.GONE
                if (siz == i) {
                    vLine.visibility = View.GONE
                }
            }

            llComment.addView(view)
        }

    }


}