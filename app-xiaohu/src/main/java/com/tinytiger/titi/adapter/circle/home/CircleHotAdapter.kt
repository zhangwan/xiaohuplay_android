package com.tinytiger.titi.adapter.circle.home


import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.event.LikeEvent
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.circle.post.SelectFriendActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import com.tinytiger.titi.widget.view.Image9View
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc push 推荐帖子
 */
class CircleHotAdapter :
    BaseQuickAdapter<PostBean, BaseViewHolder>(R.layout.circle_item_hot, null) {
    /**
     * 是否不在圈子主页 -
     * */
    var type = true
    var clickCallback: ((data: PostBean) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: PostBean) {
        holder.getView<View>(R.id.ll_content).setOnClickListener {
            PostActivity.actionStart(context,item.id)
        }
        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar, item.master_type)
            .setUserId(item.user_id)
        val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)
        holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(item.create_time))
        val tvAttention = holder.getView<AttentionView>(R.id.tvAttention)
        tvAttention.setMutual(item.is_mutual, item.user_id)
        tvAttention.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                listener?.onAttention(is_mutual, item)
            }
        }

        val ivView9 = holder.getView<Image9View>(R.id.ivView9)
        if (item.img_url != null && item.img_url.size > 0) {
            ivView9.visibility = View.VISIBLE
            ivView9.setImages(item.img_url)
        } else {
            ivView9.visibility = View.GONE
        }

        if (item.total_hots > 999) {
            holder.setGone(R.id.ivEssence, false)
        } else {
            holder.setGone(R.id.ivEssence, true)
        }


        holder.setGone(R.id.tvInvite, true)
        holder.setGone(R.id.tvName1, false)

        val tabs = ArrayList<String>()
        if (item.circle_list != null && item.circle_list.size > 0) {
            for (i in item.circle_list) {
                tabs.add("#${i.name}#")
            }
        }

        val builder = ShowAllTextView.Builder()
            .setText(item.content)
            .setLabelList(tabs)
            .setOnClickListener { _, index, _ ->
                CirclesDetailsActivity.actionStart(context, item.circle_list[index].circle_id,"","")
            }
        isNodeVisible(item.answer_id, holder)
        var circleName = ""
//        if (type) {
            if (!TextUtils.isEmpty(item.circle_name)) {
                circleName = "${item.circle_name}"
            }

//        }
        circleName += if (!TextUtils.isEmpty(item.modular_name)) {
            if ("综合" == item.modular_name) {
                ""
            } else {
                if (!TextUtils.isEmpty(circleName)) {
                    "-${item.modular_name}"
                }else{
                    "${item.modular_name}"
                }

            }
        } else {
            ""
        }
        holder.setText(R.id.tvName1, circleName)
        holder.setGone(R.id.tvName1,false)
        if(TextUtils.isEmpty(circleName)){
           holder.getView<View>(R.id.tvName1).visibility=View.INVISIBLE
        }

        when (item.answer_id) {
            -1 -> {
                builder.setType(ShowAllTextView.TEXT)
            }
            0 -> {
                holder.getView<View>(R.id.tvInvite).setOnClickListener {
                    SelectFriendActivity.actionStart(context, item.id)
                }

                builder.setImgLabel("待解答")
                    .setType(ShowAllTextView.IMAGE_TEXT)
                    .isResolved(false)

            }
            else -> {
                var participateNum = "${item.participate_num}人参与回答"
                builder.setImgLabel("已解答")
                    .setType(ShowAllTextView.IMAGE_TEXT)
                    .isResolved(true)
                holder.getView<TextView>(R.id.tvAnswerName).text = participateNum

            }
        }

        holder.getView<ShowAllTextView>(R.id.tvDesc).create(builder)
        holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))
        holder.setGone(R.id.vView, false)

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener {
            listener?.onLike(item)
            item.like_num = viewLike.like_num
            item.is_like = viewLike.is_like
            EventBus.getDefault().post(LikeEvent(item.id, item.like_num, item.is_like))
        }

        holder.getView<View>(R.id.ll_content).setOnClickListener {
            PostActivity.actionStart(context, item.id)
        }
        holder.getView<View>(R.id.rl_top).setOnClickListener {
            PostActivity.actionStart(context, item.id)
        }

        holder.getView<View>(R.id.tvShare).setOnClickListener {
            listener?.onShare(item)
        }
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            listener?.onRead(item)
        }
        holder.getView<View>(R.id.tvName1).setOnClickListener {
            if(type){
                CirclesDetailsActivity.actionStart(context, item.circle_id,item.modular_id,"")
            }else{
                clickCallback?.invoke(item)
            }

        }
    }

    /**
     * 是否是问答帖
     * @param isAnswerType int -1综合 0-待解答 else 已解答
     */
    private fun isNodeVisible(isAnswerType: Int, holder: BaseViewHolder) {
        if (-1 == isAnswerType) {
            holder.getView<View>(R.id.tvShare).visibility = View.VISIBLE
            holder.getView<View>(R.id.tvRead).visibility = View.VISIBLE
            holder.getView<View>(R.id.viewLike).visibility = View.VISIBLE
            holder.getView<View>(R.id.tvInvite).visibility = View.GONE
            holder.getView<View>(R.id.tvAnswerName).visibility = View.GONE
        } else {
            holder.getView<View>(R.id.tvShare).visibility = View.GONE
            holder.getView<View>(R.id.tvRead).visibility = View.GONE
            holder.getView<View>(R.id.viewLike).visibility = View.GONE
            if (0 == isAnswerType) {//待解答
                holder.getView<View>(R.id.tvInvite).visibility = View.VISIBLE
                holder.getView<View>(R.id.tvAnswerName).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.tvInvite).visibility = View.GONE
                holder.getView<View>(R.id.tvAnswerName).visibility = View.VISIBLE
            }
        }
    }

    interface OnHomePushListener {
        fun onShare(item: PostBean)
        fun onLike(item: PostBean)
        fun onAttention(is_mutual: Int, item: PostBean)

        fun onRead(item: PostBean)
    }

    var listener: OnHomePushListener? = null

}
