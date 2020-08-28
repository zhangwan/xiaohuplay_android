package com.tinytiger.titi.adapter.home2


import android.text.TextUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.home2.NewsBeanMulti
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import com.tinytiger.titi.widget.view.Anim.AnimFollowGameView


/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 上午 11:26
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页资讯列表
 */
class HomeGameAdapter(data: ArrayList<NewsBeanMulti>) :
    BaseMultiItemQuickAdapter<NewsBeanMulti, BaseViewHolder>(data) {

    init {
        //文章
        addItemType(1, R.layout.home_item_news_text)
        //视频
        addItemType(2, R.layout.home_item_news_video)

        //游戏详情顶部
        addItemType(3, R.layout.my_game_item_title)

        addItemType(4, R.layout.view_empty)
    }

    var logo: String = ""
    var name: String = ""
    var bgType = false
    var afgvFollow: AnimFollowGameView? = null
    override fun convert(helper: BaseViewHolder, item: NewsBeanMulti) {
        when (helper.itemViewType) {
            1 -> {
                val bean = item.mGameBean

                helper.setText(R.id.tvTitle, bean.title)
                helper.setText(R.id.tvWatchSize, StringUtils.sizeToString(bean.comment_count))
                if (!bean.game_name.isNullOrEmpty()) {
                    helper.setText(R.id.tvName, "#${bean.game_name}")
                } else {
                    helper.setText(R.id.tvName, "#${bean.name}")
                }

                GlideUtil.loadImg(helper.getView(R.id.ivIcon), bean.cover)
                val rlInfo = helper.getView<RelativeLayout>(R.id.rlInfo)
                if (bgType) {
                    rlInfo.background = ContextCompat.getDrawable(context, R.color.white)
                }
                helper.getView<RelativeLayout>(R.id.rlInfo).setOnClickListener {
                    NewsDetailActivity.actionStart(context, bean.id)
                }
            }
            2 -> {
                val bean = item.mGameBean

                helper.setText(R.id.tvTitle, bean.title)
                helper.setText(R.id.tvWatchSize, StringUtils.sizeToString(bean.comment_count))

                if (!bean.game_name.isNullOrEmpty()) {
                    helper.setText(R.id.tvName, "#${bean.game_name}")
                } else {
                    helper.setText(R.id.tvName, "#${bean.name}")
                }

                helper.setText(R.id.tvTime, TimeZoneUtil.formatTimemS(bean.video_length * 1000))
                val ivIcon = helper.getView<ImageView>(R.id.ivIcon)

                GlideUtil.loadImg(ivIcon, bean.cover)

                val rlInfo = helper.getView<RelativeLayout>(R.id.rlInfo)

                if (bgType) {
                    rlInfo.background = ContextCompat.getDrawable(context, R.color.white)
                }

                helper.getView<RelativeLayout>(R.id.rlInfo).setOnClickListener {
                    if (bean.video_url != null) {
                        VideoDetailActivity.actionStart(context, bean.id, bean.video_url)
                    }
                }
            }

            3 -> {
                val bean = item.gameInfoBean
                bgType = true
                if(!TextUtils.isEmpty(logo)){
                    GlideUtil.loadImg(helper.getView(R.id.iv_avatar), logo)
                }
                helper.setText(R.id.tv_name, name)
                helper.setText(R.id.tv_desc, bean.one_introduce)

                bean.follow_status = bean.is_follow
                afgvFollow = helper.getView(R.id.afgvFollow)
                afgvFollow?.post { afgvFollow?.setFollowGame(bean.follow_status) }
                afgvFollow?.setOnFollowGameListener {
                    mOnGameItemListener?.setGameFollow(it)
                }
            }
            4 -> {
                helper.setBackgroundColor(R.id.empty_view,
                    ContextCompat.getColor(context, R.color.color_bg))
            }
        }
    }

    var mOnGameItemListener: OnGameItemListener? = null

    interface OnGameItemListener {
        fun setGameFollow(follow_status: Int)
    }
}
