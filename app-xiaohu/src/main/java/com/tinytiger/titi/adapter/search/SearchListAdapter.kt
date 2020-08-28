package com.tinytiger.titi.adapter.search


import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.search.SearchBeanMulti
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R

import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.adapter.post.BasePostNodeDataUtils
import com.tinytiger.titi.event.LikeEvent
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView

import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/12 0012 上午 10:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索数据展示页
 */
class SearchListAdapter(data: ArrayList<SearchBeanMulti>) :
    BaseMultiItemQuickAdapter<SearchBeanMulti, BaseViewHolder>(data) {
    var shareListener: ((bean: PostBean) -> Unit)? = null
    init {
        //title
        addItemType(0, R.layout.search_item_title)

        //游戏
        addItemType(1, R.layout.search_item_game)
        addItemType(2, R.layout.search_item_game2)

        //圈子
        addItemType(3, R.layout.search_item_circle)

        //帖子
        addItemType(4, R.layout.item_common_post_node)

        //用户
        addItemType(5, R.layout.search_item_user)

        //资讯
        addItemType(6, R.layout.search_item_article)

        //站位
        addItemType(10, R.layout.view_line10)
    }

    var dp5 = 5
    /**
     * 匹配字段
     */
    var searchTxt = ""

    //true 可以播放视频 false 不可以播放视频
    var isVideoType: Boolean = false

    override fun convert(holder: BaseViewHolder, item: SearchBeanMulti) {
        when (holder.itemViewType) {
            0 -> {
                holder.setText(R.id.tvTitle, item.defaulttxt)
//0 全部 1游戏 2帖子 3圈子 4用户 5资讯
                holder.getView<ImageView>(R.id.tvAdvance).setOnClickListener {
                    var page = 1
                    when (item.defaulttxt) {
                        "游戏" -> {
                            page = 1
                        }
                        "圈子" -> {
                            page = 3
                        }
                        "帖子" -> {
                            page = 2
                        }
                        "用户" -> {
                            page = 4
                        }
                        "资讯" -> {
                            page = 5
                        }
                    }
                    EventBus.getDefault().post(SearchEvent(1, page))
                }
            }
            1 -> {
                val mData = item.mSearchGameInfo
                var cate_two = ""
                if (mData.cate_two_level != null && mData.cate_two_level.size > 0) {
                    var size = 0
                    for (i in mData.cate_two_level) {
                        size++
                        if (size < 4) {
                            cate_two = "$cate_two${i.cate_name}/"
                        }
                    }
                    cate_two = cate_two.substring(0, cate_two.length - 1)
                }
                holder.setText(R.id.tvInfo, cate_two)
                holder.setText(R.id.tvName, mData.name)

                GlideUtil.loadImg(holder.getView(R.id.iv_image), mData.logo)
                holder.getView<View>(R.id.rlItem).setOnClickListener {
                    GameDetailActivity.actionStart(context, mData.id,5)
                    SearchAgentUtils.setSearchRoute(searchTxt,0)
                }
            }
            2 -> {
                val mData = item.mSearchGameInfo
                holder.setText(R.id.tvInfo, mData.cate_name)
                holder.getView<View>(R.id.rlItem).setOnClickListener {
                    GameCategoryDetailActivity.actionStart(context, mData.cate_name, mData.id.toInt())
                }
            }
            4 -> {
                val item = item.mPostBean
                BasePostNodeDataUtils.setPostNodeView(context,item,holder.itemView,holder.adapterPosition,isVideoType,shareListener)
                holder.setGone(R.id.vView, false)
                val tvAttention = holder.getView<AttentionView>(R.id.tvAttention)
                tvAttention.mListener = object : AttentionView.OnAttentionViewListener {
                    override fun onAttentionView(is_mutual: Int) {
                        listener?.onAttention(is_mutual, item.user_id)
                    }
                }
                val viewLike = holder.getView<LikeView>(R.id.viewLike)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    listener?.onLike(item)
                    item.like_num = viewLike.like_num
                    item.is_like = viewLike.is_like
                    EventBus.getDefault().post(LikeEvent(item.id, item.like_num, item.is_like))
                }

                holder.getView<View>(R.id.rl_content).setOnClickListener {
                    PostActivity.actionStart(context,item.id)

                    SearchAgentUtils.setSearchRoute(searchTxt,1)
                }
                holder.getView<View>(R.id.llAllTextParent).setOnClickListener {
                    PostActivity.actionStart(context,item.id)
                    SearchAgentUtils.setSearchRoute(searchTxt,1)
                }

                holder.getView<View>(R.id.tvShare).setOnClickListener {
                    shareListener?.invoke(item)
                }
                holder.getView<View>(R.id.tvRead).setOnClickListener {
                    listener?.onRead(item)
                }
                holder.getView<View>(R.id.tvName1).setOnClickListener {

                    CirclesDetailsActivity.actionStart(context, item.circle_id, item.modular_id,"")
                    SearchAgentUtils.setSearchRoute(searchTxt,2)

                }
            }
            3 -> {
                val mData = item.mCircleBean

                GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), mData.background)

                holder.setText(R.id.tvNum, StringUtils.sizeToString(mData.hots))

                holder.setText(R.id.tv_title, "#${mData.name}#")
                holder.getView<View>(R.id.ll_content).setOnClickListener {
                    CirclesDetailsActivity.actionStart(context, mData.id,"","")
                    SearchAgentUtils.setSearchRoute(searchTxt,2)
                }
            }
            5 -> {
                val rlItem = holder.getView<RelativeLayout>(R.id.rlItem)
                val lp = RelativeLayout.LayoutParams(rlItem.layoutParams)
                if (data[holder.adapterPosition - 1].itemType == 0) {
                    lp.setMargins(dp5 * 3, 0, dp5 * 3, 0)
                } else {
                    lp.setMargins(dp5 * 3, dp5 * 2, dp5 * 3, 0)
                }
                rlItem.layoutParams = lp

                val bean = item.mSearchUserInfo
                var city = "在你心里"
                if (bean.provcn != null && bean.provcn.isNotEmpty()) {
                    if (bean.provcn == bean.districtcn) {
                        city = bean.provcn
                    } else {
                        city = bean.provcn + bean.districtcn
                    }
                }
                if (bean.fans_num == 0) {
                    holder.setText(R.id.tvInfo, "$city")
                } else {
                    holder.setText(R.id.tvInfo, "$city  粉丝: ${StringUtils.sizeToString(bean.fans_num)}")
                }


                var nickname = bean.nickname
                if (nickname.length > 8) {
                    nickname = nickname.substring(0, 8) + "..."
                }

                val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
                tv_nickname.setNickname(nickname)
                tv_nickname.setMedalIcon(bean?.medal_image)

                when (bean.gender) {
                    1 -> {
                        holder.setImageDrawable(
                            R.id.ivGender,
                            ContextCompat.getDrawable(context, R.mipmap.icon_gender_male)
                        )
                    }
                    2 -> {
                        holder.setImageDrawable(
                            R.id.ivGender,
                            ContextCompat.getDrawable(context, R.mipmap.icon_gender_girl)
                        )
                    }
                    else -> {
                        holder.setImageDrawable(R.id.ivGender, null)
                    }
                }
                val avAttention = holder.getView<AttentionView>(R.id.avAttention)
                avAttention.setMutual(bean.is_mutual)
                avAttention.mListener = object : AttentionView.OnAttentionViewListener {
                    override fun onAttentionView(is_mutual: Int) {
                        listener?.onAttention(bean.is_mutual, bean.user_id)
                       // EventBus.getDefault().post(SearchEvent(bean.user_id, holder.adapterPosition,bean.is_mutual))
                    }
                }


             /*   val tvAttention = holder.getView<TextView>(R.id.tvAttention)
                //1:互相关注 0:已关注 -1:未关注 -2:自己
                tvAttention.visibility = View.VISIBLE
                when (bean.is_mutual) {
                    1 -> {
                        tvAttention.isSelected = true
                        tvAttention.text = "互相关注"
                        tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                        tvAttention.background =
                            ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
                    }
                    0 -> {
                        tvAttention.isSelected = true
                        tvAttention.text = "已关注"
                        tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                        tvAttention.background =
                            ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_aaaaaa)
                    }
                    -1 -> {
                        tvAttention.isSelected = false
                        tvAttention.text = "关注"
                        tvAttention.setTextColor(ContextCompat.getColor(context, R.color.gray33))
                        tvAttention.background =
                            ContextCompat.getDrawable(context, R.drawable.solid_gradient_15_ffcc03)
                    }
                    -2 -> {
                        tvAttention.visibility = View.GONE
                    }
                }*/

                holder.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar, bean.master_type)
               /* tvAttention.setOnClickListener {
                    if (FastClickUtil.isFastClickTiming()) {
                        return@setOnClickListener
                    }

                    listener?.onAttention(bean.is_mutual, bean.user_id)
                }*/
                holder.getView<RelativeLayout>(R.id.rlItem).setOnClickListener {
                    HomepageActivity.actionStart(context, bean.user_id)
                    SearchAgentUtils.setSearchRoute(searchTxt,3)
                }
            }

            6 -> {

                val rlItem = holder.getView<RelativeLayout>(R.id.rlItem)
                val lp = RelativeLayout.LayoutParams(rlItem.layoutParams)
                if (data[holder.adapterPosition - 1].itemType == 0) {
                    lp.setMargins(0, 0, 0, 0)
                } else {
                    lp.setMargins(0, dp5 * 2, 0, 0)
                }
                rlItem.layoutParams = lp

                val mData = item.mSearchNewsInfo
                holder.setText(R.id.tvName, "#${mData.gamename}")

                holder.setText(R.id.tvTitile, mData.title)
                GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), mData.cover)
                holder.setText(R.id.tvSize, StringUtils.sizeToString(mData.comment_num))


                if (mData.type == 1) {
                    holder.setGone(R.id.ivVideo, true)
                } else {
                    holder.setGone(R.id.ivVideo, false)
                }

                holder.getView<RelativeLayout>(R.id.rlItem).setOnClickListener {

                    if (mData.type == 2) {
                        VideoDetailActivity.actionStart(context, mData.id, mData.video_url)
                    } else {
                        NewsDetailActivity.actionStart(context, mData.id)
                    }
                    SearchAgentUtils.setSearchRoute(searchTxt,4)
                }
            }
        }
    }

    interface OnHomePushListener {
        fun onLike(item: PostBean)
        fun onAttention(is_mutual: Int, userid: String)

        fun onRead(item: PostBean)
    }

    var listener: OnHomePushListener? = null


    /**
     * txt 文字内容
     *  view View
     *  type true约完名字 false 约完id
     */
    private fun setCocorTxt(txt: String, view: TextView) {
        val style = SpannableStringBuilder(txt)
        val ind = txt.indexOf(searchTxt)
        when (ind) {
            -1 -> {
                //无匹配
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.grayAA)),
                    0, txt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            0 -> {
                //开头匹配
                // 一定为用户名
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray33)),
                    0, searchTxt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (txt.length > searchTxt.length) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.grayAA)),
                        searchTxt.length, txt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            else -> {
                //中间匹配

                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.grayAA)),
                    0, ind,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray33)),
                    ind, searchTxt.length + ind,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (txt.length > searchTxt.length) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.grayAA)),
                        searchTxt.length + ind, txt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
        }
        view.text = style
    }

}
