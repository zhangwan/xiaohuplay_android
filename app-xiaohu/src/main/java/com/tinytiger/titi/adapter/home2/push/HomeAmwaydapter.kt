package com.tinytiger.titi.adapter.home2.push

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatRatingBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.ui.yungame.YunGameActivity
import com.tinytiger.titi.widget.text.MedalTextView

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_安利文列表
 */
class HomeAmwaydapter :
    BaseQuickAdapter<AmwayBean, BaseViewHolder>(R.layout.home_push_item_amway, null) {
    var open_from=0
    override fun convert(holder: BaseViewHolder, item: AmwayBean) {
        GlideUtil.loadImg(holder.getView(R.id.ivGame), item.thumbnail)
       // holder.setText(R.id.tvGameName, item.game_name)

        GlideUtil.loadImg(holder.getView(R.id.ivLogo), item.avatar)
        holder.getView<MedalTextView>(R.id.mtvName)
            .setNickname(StringUtils.stringName(item.nickname), item.medal_image)
        holder.setText(R.id.tvTitle, item.title)
        holder.getView<AppCompatRatingBar>(R.id.ratingBar).rating = Math.round(item.score).toFloat()

        holder.setText(R.id.tvDesc, StringUtils.toPlainText1(item.content))

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            GameReviewsActivity.actionStart(context, item.game_id, item.id,open_from)
        }
        holder.getView<View>(R.id.ivLogo).setOnClickListener {
            HomepageActivity.actionStart(context,item.user_id)
        }
        holder.getView<View>(R.id.mtvName).setOnClickListener {
            HomepageActivity.actionStart(context,item.user_id)
        }
        holder.getView<View>(R.id.ivGame).setOnClickListener {
            GameDetailActivity.actionStart(context,item.game_id,0)
        }

    }
}
