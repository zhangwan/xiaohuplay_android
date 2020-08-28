package com.tinytiger.titi.adapter.gametools.info


import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.web.X5WebView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.gametools.GameIntroAdapter
import com.tinytiger.titi.adapter.gametools.GameVideoAdapter
import com.tinytiger.titi.data.game.MultiGameDetailBean
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.video.VideoActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import com.tinytiger.titi.utils.Scroll.FlexboxNoLayoutManager


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏 详情 detail adapter
*/
class MultiGameDetailAdapter(data: ArrayList<MultiGameDetailBean>) :
    BaseMultiItemQuickAdapter<MultiGameDetailBean, BaseViewHolder>(data) {


    init {
        //标题
        addItemType(1, R.layout.game_item_title)
        //词云
        addItemType(2, R.layout.my_game_item_word_cloud)
        //游戏简介
        addItemType(3, R.layout.my_game_item_introduct)
        //视频
        addItemType(4, R.layout.my_game_item_video)
        //评分
        addItemType(5, R.layout.my_game_item_rating)
        //雷达图
        addItemType(6, R.layout.game_item_radar)
        //圈子介绍
        addItemType(7, R.layout.game_item_circleifo)
        //游戏信息
        addItemType(8, R.layout.game_item_gameinfo)
    }

    //宽度
    var width = 0

    override fun convert(holder: BaseViewHolder, item: MultiGameDetailBean) {
        when (holder.itemViewType) {
            1 -> {
                val bean = item.mGameBean
                GlideUtil.loadImg(holder.getView(R.id.iv_avatar), bean.game_info.logo)

                holder.setText(R.id.tv_name, bean.game_info.name)


                if (bean.circle_info != null && !bean.circle_info.id.isNullOrEmpty()) {
                    holder.setGone(R.id.tvCircle, false)
                    holder.getView<View>(R.id.tvCircle).setOnClickListener {
                        CirclesDetailsActivity.actionStart(context, bean.circle_info.id, "", "")
                    }
                } else {
                    holder.setGone(R.id.tvCircle, true)
                }


                if (bean.game_cate != null && bean.game_cate.size > 0) {
                    holder.setGone(R.id.rvTag, false)
                    val rvTag = holder.getView<RecyclerView>(R.id.rvTag)
                    val manager = FlexboxNoLayoutManager(context)
                    manager.setScrollEnabled(false)
                    manager.flexDirection = FlexDirection.ROW
                    manager.flexWrap = FlexWrap.WRAP
                    manager.alignItems = AlignItems.STRETCH
                    rvTag.clipToPadding = false
                    rvTag.layoutManager = manager
                    val adapter = GameTagAdapter()
                    rvTag.adapter = adapter
                    adapter.setNumData(bean.game_cate)

                    var size = 0
                    for (i in bean.game_cate) {
                        size += i.cate_name.length
                    }
                    if (size > 40) {
                        val lp = rvTag.layoutParams
                        lp.height = Dp2PxUtils.dip2px(context, 60)
                        rvTag.layoutParams = lp
                    }
                } else {
                    holder.setGone(R.id.rvTag, true)
                }

            }
            2 -> {
                val word_view = holder.getView<X5WebView>(R.id.word_view)
                word_view.setLayerType()
                word_view.loadUrl(item.tag_cloud_url)

            }
            3 -> {
                val bean = item.mGameInfoBean

                val rv_introduction = holder.getView<RecyclerView>(R.id.rv_introduction)
                val mIntroAdapter = GameIntroAdapter()
                rv_introduction.adapter = mIntroAdapter
                rv_introduction.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                mIntroAdapter.isHorizontal = bean.images_introduce_type == 1
                //游戏简介
                if (bean.video_introduce != null) {
                    mIntroAdapter.video_url = bean.video_introduce
                }
                mIntroAdapter.setNewInstance(bean.images_introduce)
                mIntroAdapter.setOnItemClickListener { _, _, position ->
                    if (bean.video_introduce != null && bean.video_introduce.isNotEmpty() && position == 0) {
                        VideoActivity.actionStart(context, mIntroAdapter.video_url)
                    } else {
                        mOnGameDetailItemListener?.showIntroItem(position)
                    }
                }

                holder.setText(R.id.tvIntroduction, bean.one_introduce)
                holder.getView<ShowAllTextView>(R.id.tvDesc)
                    .create(
                        ShowAllTextView.Builder().setText(
                            StringUtils.toPlainText(bean.detail_introduce)
                        )
                    )
            }
            4 -> {

                val bean = item.recommended_video

                val rv_video = holder.getView<RecyclerView>(R.id.rv_video)
                val mVideoAdapter = GameVideoAdapter()
                //视频介绍
                rv_video.adapter = mVideoAdapter
                rv_video.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                mVideoAdapter.setOnItemClickListener { _, _, position ->
                    VideoDetailActivity.actionStart(
                        context, bean[position].id,
                        bean[position].video_url
                    )
                }

                mVideoAdapter.setNewInstance(bean)
            }

            5 -> {
                val bean = item.assessInfo
                if (bean.show_score == 0) {
                    holder.setText(R.id.tv_score_num, "")
                    holder.setText(R.id.tv_score_players, "暂无评分")
                    holder.setGone(R.id.tv_take_score, true)
                } else {
                    holder.setText(R.id.tv_score_num, bean.average)
                    holder.setText(R.id.tv_score_players, "${bean.total}人评价")
                    holder.setGone(R.id.tv_take_score, false)
                }

                if (bean.is_assess == 1) {
                    holder.setGone(R.id.tv_take_score, true)
                }

                holder.getView<AppCompatRatingBar>(R.id.rating_bar).rating = bean.average.toFloat()
                holder.getView<View>(R.id.tv_take_score).setOnClickListener {
                    mOnGameDetailItemListener?.showTakeScore()
                }

            }
            6 -> {

                if (width > 0) {
                    val rl_container = holder.getView<View>(R.id.rl_container)
                    val lp = rl_container.layoutParams
                    width = width - Dp2PxUtils.dip2px(context, 30)
                    lp.height = (width * 0.66).toInt()
                    rl_container.layoutParams = lp
                }
                holder.getView<X5WebView>(R.id.word_view).loadUrl(item.tag_cloud_url)
            }
            7 -> {
                val bean = item.circleInfo
                GlideUtil.loadImg(holder.getView(R.id.iv_avatar), bean.logo)
                holder.setText(R.id.tv_name, bean.name)
                holder.setText(R.id.tvCircleSize, "" + bean.join_people)
                holder.setText(R.id.tvCircleNum, "" + bean.post_num)
                holder.getView<View>(R.id.rl_content).setOnClickListener {
                    CirclesDetailsActivity.actionStart(context, bean.id, "", "")
                }
            }
            8 -> {
                val bean = item.mGameInfoBean
                holder.setText(R.id.tv_version, bean.version)
                    .setText(
                        R.id.tv_game_size, StringUtils.formatMemorySize(bean.package_size_android)
                    )
                    .setText(
                        R.id.tv_update_time, TimeZoneUtil.getDateToString(
                            TimeZoneUtil.getToDate(bean.package_update_time), "yyyy-MM-dd"
                        )
                    )
                //发行商、开发商有一个为空时往前面挪
                holder.setGone(R.id.ll_bottom, false)
                if (!bean.publisher.isNullOrBlank()) {
                    holder.getView<LinearLayout>(R.id.ll_developer).visibility =
                        if (bean.developer.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
                    holder.setText(R.id.tv_publisher, StringUtils.limitText(bean.publisher, 16))
                        .setText(R.id.tv_developer, StringUtils.limitText(bean.developer, 16))
                        .setText(R.id.title_publisher, "发行商")

                } else if (!bean.developer.isNullOrBlank()) {
                    holder.getView<LinearLayout>(R.id.ll_developer).visibility = View.INVISIBLE
                    holder.setText(R.id.tv_publisher, StringUtils.limitText(bean.developer, 16))
                        .setText(R.id.tv_developer, "")
                        .setText(R.id.title_publisher, "开发商")

                } else {
                    holder.setGone(R.id.ll_bottom, true)
                }
            }
            else -> {

            }
        }
    }


    var mOnGameDetailItemListener: OnGameDetailItemListener? = null

    interface OnGameDetailItemListener {
        fun showIntroItem(position: Int)

        fun showTakeScore()

        fun setGameFollow(follow_status: Int)
    }
}