package com.tinytiger.titi.adapter.circle.home


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.net.data.circle.home.AttentionMutli
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.BasePostNodeDataUtils
import com.tinytiger.titi.event.AttentionEvent
import com.tinytiger.titi.event.LikeEvent
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:11
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 圈子首页关注列表
 */
class MultiAttentionAdapter(data: ArrayList<AttentionMutli>) :
    BaseMultiItemQuickAdapter<AttentionMutli, BaseViewHolder>(data) {
    //true 可以播放视频 false 不可以播放视频
    var isVideoType: Boolean = false
    var shareListener: ((bean: PostBean) -> Unit)? = null
    init {
        addItemType(1, R.layout.circle_item_attention_1)
        addItemType(2, R.layout.circle_item_attention_2)

        addItemType(3, R.layout.item_common_post_node)
    }

    var tvMutual: TextView? = null
    override fun convert(holder: BaseViewHolder, item: AttentionMutli) {
        when (holder.itemViewType) {
            1 -> {
                tvMutual = holder.getView(R.id.tvMutual)
                holder.getView<View>(R.id.tvNumber).setOnClickListener {
                    setNewFriend()
                }
            }
            2 -> {
                val bean = item.mUserBean
                GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), bean.avatar)

                holder.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar, bean.master_type).setUserId(bean.user_id)
                val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
                tv_nickname.setNickname(bean.nickname)
                tv_nickname.setMedalIcon(bean.medal_image)

                GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), bean.avatar)
                holder.setText(R.id.tvNum, "${bean.post_num}条动态 ${bean.fans_num}个粉丝")

                val tvAttention = holder.getView<AttentionView>(R.id.tvAttention)
                tvAttention.setMutual(bean.is_mutual)
                tvAttention.mListener = object : AttentionView.OnAttentionViewListener {
                    override fun onAttentionView(is_mutual: Int) {
                        listener?.onAttention(is_mutual, bean.user_id)
                    }
                }
            }
            3 -> {
                val item = item.mBean
                holder.setGone(R.id.vView,false)
                holder.setGone(R.id.tvAttention, true)
                BasePostNodeDataUtils.setPostNodeView(context,item,holder.itemView,holder.adapterPosition,isVideoType,shareListener)
                val viewLike = holder.getView<LikeView>(R.id.viewLike)
                viewLike.setLike(item.is_like, item.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    listener?.onLike(item)
                    item.like_num = viewLike.like_num
                    item.is_like = viewLike.is_like
                    EventBus.getDefault().post(LikeEvent(item.id, item.like_num, item.is_like))
                }

                holder.getView<View>(R.id.llAllTextParent).setOnClickListener {
                    PostActivity.actionStart(context, item.id)
                }
                holder.getView<View>(R.id.rl_content).setOnClickListener {
                    PostActivity.actionStart(context, item.id)
                }

                holder.getView<View>(R.id.tvShare).setOnClickListener {
                    shareListener?.invoke(item)
                }
                holder.getView<View>(R.id.tvRead).setOnClickListener {
                    listener?.onRead(item)
                }
                holder.getView<View>(R.id.tvName1).setOnClickListener {
                    CirclesDetailsActivity.actionStart(context, item.circle_id,item.modular_id,"")
                }
            }
        }
    }



    val listFriend = java.util.ArrayList<AttentionMutli>()
    var friendStart = 0
    var friendEnd = 5
    fun setDataFriend(list: java.util.ArrayList<AttentionMutli>) {
        listFriend.addAll(list)

        setNewFriend()
    }

    fun setNewFriend() {


        friendStart = friendEnd
        if (friendStart >= listFriend.size) {
            friendStart = 0
        }

        friendEnd = friendStart + 5
        if (friendEnd >= listFriend.size) {
            friendEnd = listFriend.size
        }

        val lists = java.util.ArrayList<AttentionMutli>()
        lists.add(AttentionMutli(1))
        setNewInstance(lists)
        addData(listFriend.subList(friendStart, friendEnd))
    }

    fun setAttention(event: AttentionEvent) {
        for (i in listFriend) {
            if (event.userId == i.mUserBean.user_id) {
                i.mUserBean.is_mutual = event.isMutual
            }
        }
    }


    interface OnHomePushListener {
        fun onAttention(is_mutual: Int, userid: String)

        fun onLike(item: PostBean)
        fun onRead(item: PostBean)
    }

    var listener: OnHomePushListener? = null

}