package com.tinytiger.titi.adapter.news


import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.news.NewsBeanMulti
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.common.view.web.X5TxtWebView
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.widget.text.MedalTextView

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_推荐视频列表
 */
class NewsListAdapter(data: ArrayList<NewsBeanMulti>) :
    BaseMultiItemQuickAdapter<NewsBeanMulti, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.news_item_info)
        addItemType(2, R.layout.video_item_news)
        addItemType(3, R.layout.comment_item_info_black)
    }

    var tvAttention: TextView? = null
    private var tvLike: TextView? = null
    private var tvComment: TextView? = null

    //只加载一次WebView
    private var isLoadWebView = false

    fun setLikeSize(like_num: Int) {
        tvLike?.text = "${StringUtils.sizeToString(like_num)}条点赞"
    }

    fun setCommentNum(comment_num: Int) {
        tvComment?.text = "${StringUtils.sizeToString(comment_num)}条评论"
    }

    override fun convert(holder: BaseViewHolder, item: NewsBeanMulti) {
        when (holder.itemViewType) {
            1 -> {
                if (item.mNewsInfoBean == null) {
                    return
                }

                val mBean = item.mNewsInfoBean

                val tv_nickname = holder.getView<MedalTextView>(R.id.tvName)
                tv_nickname.setNickname(mBean.nickname)
                tv_nickname.setMedalIcon(mBean.medal_image)

                holder.setText(
                    R.id.tvTime,
                    TimeZoneUtil.getShortTimeShowString(mBean.create_time) + "发布"
                )
                holder.setText(R.id.tvTitle, mBean.title)

                tvLike = holder.getView(R.id.tvLike)
                tvComment = holder.getView(R.id.tvComment)

                holder.setText(R.id.tvLike, "${StringUtils.sizeToString(mBean.like_num)}条点赞")
                holder.setText(R.id.tvComment, "${StringUtils.sizeToString(mBean.comment_num)}条评论")
                holder.setGone(R.id.tvViewNum, false)
                holder.setText(R.id.tvViewNum, "${StringUtils.sizeToString(mBean.view_num)}次浏览")

                holder.getView<AvatarView>(R.id.avUser).setAvatar(mBean.avatar)
                    .setTalent(mBean.master_type)
                    .setUserId(mBean.user_id)
                holder.getView<RelativeLayout>(R.id.rl_user).setOnClickListener {

                    HomepageActivity.actionStart(context, mBean.user_id)
                }

                tvAttention = holder.getView(R.id.tvAttention)
                tvAttention?.visibility = View.VISIBLE

                //解决RecycleView和WebView滑动空白的问题
                val dp15 = ScreenUtil.dp2px(context, 15f)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(dp15, 0, dp15, 0)
                val tvHtml = holder.getView<X5TxtWebView>(R.id.x5webView)
                if (!isLoadWebView) {
                    isLoadWebView = true
                    tvHtml.imageClick { imageUrls, position ->
                        mOnImageClickListener?.onClick(imageUrls, position)
                    }.loadDataWithBaseURL(mBean.introduce)
                }
                tvHtml.layoutParams = lp
                setfollow(mBean.is_mutual)
            }
            2 -> {
                if (item.mRecommendBean == null) {
                    return
                }
                holder.setGone(R.id.tv_time, true)
                GlideUtil.loadImg(holder.getView(R.id.iv_image), item.mRecommendBean.cover)
                holder.setText(R.id.tv_title, item.mRecommendBean.title)
                holder.setText(
                    R.id.tv_time,
                    TimeZoneUtil.formatTimemS(item.mRecommendBean.video_length * 1000)
                )

                holder.setText(R.id.tv_name, item.mRecommendBean.game_name)
//                holder.setImageResource(
//                    R.id.iv_read_num,
//                    if (isVideo) R.mipmap.ic_news_video_num else R.mipmap.icon_read_g
//                )
//                holder.setText(
//                    R.id.tv_read_num,
//                    StringUtils.sizeToString(item.mRecommendBean.comment_count)
//                )

                holder.getView<LinearLayout>(R.id.ll_content).setOnClickListener {
                    NewsDetailActivity.actionStart(context!!, item.mRecommendBean.id)
                    (context as NewsDetailActivity).finish()
                }
            }

            3 -> {
                holder.setText(R.id.tv_info, "更多推荐")
            }
        }
    }

    private var mOnImageClickListener: OnImageClickListener? = null

    fun setOnImageClickListener(onclick: OnImageClickListener) {
        mOnImageClickListener = onclick
    }

    interface OnImageClickListener {
        fun onClick(urls: List<String>, position: Int)
    }

    fun setfollow(is_mutual: Int) {
        //1:互相关注 0:已关注 -1:未关注 -2:自己
        tvAttention?.visibility = View.VISIBLE
        when (is_mutual) {
            1 -> {
                tvAttention?.isSelected = true
                tvAttention?.text = "互相关注"
                tvAttention?.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention?.background =
                    ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
            }
            0 -> {
                tvAttention?.isSelected = true
                tvAttention?.text = "已关注"
                tvAttention?.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention?.background =
                    ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
            }
            -1 -> {
                tvAttention?.isSelected = false
                tvAttention?.text = "+ 关注"
                tvAttention?.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                tvAttention?.background =
                    ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_16_ffcc03)
            }
            -2 -> {
                tvAttention?.visibility = View.GONE
            }
        }
    }


}
