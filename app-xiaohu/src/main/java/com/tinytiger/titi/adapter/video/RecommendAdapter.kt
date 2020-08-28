package com.tinytiger.titi.adapter.video


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.video.ContentInfoBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.comment.IntroduceBeanMulti
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.ConllectionView
import com.tinytiger.titi.widget.view.Anim.LikeView


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:
*/
class RecommendAdapter(data: ArrayList<IntroduceBeanMulti>) :
    BaseMultiItemQuickAdapter<IntroduceBeanMulti, BaseViewHolder>(data) {


    init {
        //咨询
        addItemType(1, R.layout.video_layout_introduce)
        //推荐
        addItemType(2, R.layout.video_item_news)
        //更多推荐
        addItemType(3, R.layout.comment_item_info)
    }

    private var isExpand = false
    private var tvAttention: TextView? = null

    fun setConllections(is_collect: Int) {
        viewConllection?.setConllection(is_collect, viewConllection!!.conllection_num + is_collect)
    }

    fun setCommentNum(num: Int) {
        tv_comment_num?.text = StringUtils.sizeToString(num)
    }

    fun setLikes(is_like: Int) {
        viewLike?.setLike(is_like, viewLike!!.like_num + is_like)
    }

    var viewConllection: ConllectionView? = null
    var tv_comment_num: TextView? = null
    var viewLike: LikeView? = null
    override fun convert(holder: BaseViewHolder, item: IntroduceBeanMulti) {
        when (holder.itemViewType) {
            1 -> {
                val user_id = SpUtils.getString(R.string.user_id, "")

                val bean = item!!.contentBean
                /*       GlideUtil.loadImg(
                           holder.getView(R.id.iv_avatar),
                           bean.avatar,
                           R.mipmap.icon_person_head
                       )*/
                holder.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar)
                    .setTalent(bean.master_type)
                    .setUserId(bean.user_id)

                val tv_nickname: MedalTextView = holder.getView(R.id.tv_name)
                tv_nickname.setNickname(StringUtils.stringName(bean.nickname))
                tv_nickname.setMedalIcon(bean.medal_image)


                holder.setText(R.id.tv_time, TimeZoneUtil.getShortTimeShowString(bean.create_time))
                holder.setText(R.id.tv_title, bean.title)
                holder.setText(R.id.tv_introduce, bean.introduce)
//                holder.setText(R.id.tv_read_num, StringUtils.sizeToString(bean.view_num) + "次")
                holder.setText(R.id.tv_share_num, StringUtils.sizeToString(bean.share_num))
                holder.setText(R.id.tv_comment_num, StringUtils.sizeToString(bean.comment_num))

//                holder.setVisible(R.id.iv_more, user_id != bean.user_id)

                tvAttention = holder.getView<TextView>(R.id.tvAttention)
                setMutual(bean.is_mutual)

                val iv_expand = holder.getView<ImageView>(R.id.iv_expand)
                iv_expand.setOnClickListener {
                    if (isExpand) {
                        iv_expand.setImageResource(R.mipmap.icon_down_black)
                        holder.setGone(R.id.tv_introduce, true)
                    } else {
                        iv_expand.setImageResource(R.mipmap.icon_up_black)
                        holder.setGone(R.id.tv_introduce, true)
                    }
                    isExpand = !isExpand
                }

                //  holder.addOnClickListener(R.id.iv_avatar)
                //  holder.addOnClickListener(R.id.tv_name)
                //  holder.addOnClickListener(R.id.iv_more)
                //  holder.addOnClickListener(R.id.tvAttention)
                //  holder.addOnClickListener(R.id.tv_share_num)

                tv_comment_num = holder.getView(R.id.tv_comment_num)
                tv_comment_num?.setOnClickListener {
                    listener?.onClickComment(bean)
                }

//                viewConllection = holder.getView(R.id.viewConllection)
//                viewConllection?.setConllection(bean.is_collect, bean.collect_num)
//                viewConllection?.mListener =
//                    ConllectionView.OnLikeViewListener { listener?.onConllection(bean) }

                viewLike = holder.getView(R.id.viewLike)
                viewLike?.setLike(bean.is_like, bean.like_num)
                viewLike?.mListener = LikeView.OnLikeViewListener { listener?.onLike(bean) }
            }
            2 -> {
                val bean = item!!.recommendBean
                holder.setText(R.id.tv_title, bean.title)
                holder.setText(R.id.tv_name, bean.game_name)
                holder.setText(R.id.tv_time, TimeZoneUtil.formatTimemS(bean.video_length * 1000))
                holder.setText(R.id.tv_read_num, StringUtils.sizeToString(bean.comment_count))
                GlideUtil.loadImg(holder.getView(R.id.iv_image), bean.cover)

                // holder.addOnClickListener(R.id.rl_content)
            }
            3 -> {
                holder.setText(R.id.tv_info, item!!.defaulttxt)
            }
        }
    }

    fun setMutual(is_mutual: Int) {
        tvAttention!!.visibility = View.VISIBLE
        when (is_mutual) {
            1 -> {
                tvAttention!!.isSelected = true
                tvAttention!!.text = "互相关注"
                tvAttention!!.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention!!.background =
                    ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
            }
            0 -> {
                tvAttention!!.isSelected = true
                tvAttention!!.text = "已关注"
                tvAttention!!.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention!!.background =
                    ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
            }
            -1 -> {
                tvAttention!!.isSelected = false
                tvAttention!!.text = "+ 关注"
                tvAttention!!.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                tvAttention!!.background =
                    ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_16_ffcc03)
            }
            -2 -> {
                tvAttention!!.visibility = View.GONE
            }
        }
    }

    interface OnRecommendListener {
        fun onLike(mBean: ContentInfoBean.DataBean)
        fun onConllection(mBean: ContentInfoBean.DataBean)
        fun onClickComment(mBean: ContentInfoBean.DataBean)
    }

    var listener: OnRecommendListener? = null

}