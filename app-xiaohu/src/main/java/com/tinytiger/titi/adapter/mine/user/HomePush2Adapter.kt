package com.tinytiger.titi.adapter.mine.user


import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.HomeFelxboxAdapter
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.game.GameReviewsActivity
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/4/21 0021 上午 10:33
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 我的收藏_安利文
 */
class HomePush2Adapter :
    BaseQuickAdapter<AmwayBean, BaseViewHolder>(R.layout.home_item_amway2, null) {

    var dp3 = 0

    /**
     * 使用在我的收藏中
     * 是否显示选中状态
     */
    var isShowEditStatus = false



    var mListener: OnItemCheckListener?=null

    override fun convert(holder: BaseViewHolder, item: AmwayBean) {
        if(dp3 !=0) {
            //收藏状态
            val content = holder.getView<View>(R.id.rl_content)
            val params = content.layoutParams
            params.width = dp3
            content.layoutParams = params


            if(!isShowEditStatus){
                item.isSelected = false
            }

            if(item.create_time.isNullOrEmpty()){
                holder.setGone(R.id.tv_time,true)
            }else{
                holder.setGone(R.id.tv_time,false)
                holder.setText(R.id.tv_time, "收藏于"+TimeZoneUtil.getShortTimeShowString(item.create_time) )
            }
        }

        holder.setGone(R.id.cb_check, !isShowEditStatus)


        val cb_check = holder.getView<CheckBox>(R.id.cb_check)
        if (isShowEditStatus) {
            cb_check.isChecked = item.isSelected
            cb_check.setOnCheckedChangeListener { _, b ->
                item.isSelected = b
                mListener?.onCheck(b)
            }
        }

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            if (isShowEditStatus) {
                item.isSelected = !item.isSelected
                cb_check.isChecked = item.isSelected
            } else {
                listener?.onRefresh()
                GameReviewsActivity.actionStart(
                    context,
                    item.game_id,
                    item.id
                )
            }

        }
        holder.getView<View>(R.id.rl_center).setOnClickListener {
            if (isShowEditStatus) {
                item.isSelected = !item.isSelected
                cb_check.isChecked = item.isSelected
            } else {
                listener?.onRefresh()
                GameReviewsActivity.actionStart(
                    context,
                    item.game_id,
                    item.id
                )
            }
        }
        holder.getView<RelativeLayout>(R.id.rlGameInfo).setOnClickListener {
            if (isShowEditStatus) {
                item.isSelected = !item.isSelected
                cb_check.isChecked = item.isSelected
            } else {
                listener?.onRefresh()
                GameDetailActivity.actionStart(context, item.game_id,0)
            }
        }

        val tab_view = holder.getView<View>(R.id.tab_view)
        val recyclerView = holder.getView<RecyclerView>(R.id.recycler_tab)
        if (item.viewpoint_list != null && item.viewpoint_list.size > 0) {
            recyclerView.visibility = View.VISIBLE
            val manager = FlexboxLayoutManager(context)
            manager.flexDirection = FlexDirection.ROW
            manager.flexWrap = FlexWrap.WRAP
            manager.alignItems = AlignItems.STRETCH
            recyclerView.clipToPadding = false
            recyclerView.layoutManager = manager
            val adapterFelxbox = HomeFelxboxAdapter()
            recyclerView.adapter = adapterFelxbox
            adapterFelxbox.setNewInstance(item.viewpoint_list)

        } else {
            recyclerView.visibility = View.GONE
        }
        tab_view.setOnClickListener {
            listener?.onRefresh()
            GameReviewsActivity.actionStart(context, item.game_id, item.id)
        }

        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar,item.master_type).setUserId(item.user_id)
        val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)


        holder.getView<AppCompatRatingBar>(R.id.ratingBar).rating = Math.round(item.score).toFloat()


        val avAttention = holder.getView<AttentionView>(R.id.avAttention)
        avAttention.setMutual(item.follow)
        avAttention.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                listener?.onAttention(item)
            }
        }


        GlideUtil.loadImg(holder.getView(R.id.ivIcon1), item.thumbnail)
        holder.setText(R.id.tvGameName, item.game_name)
        holder.setText(R.id.tvGameSize, StringUtils.sizeToString(item.amway_assess_num) + "条安利")
       if (item.title.isNotEmpty()){
            holder.setGone(R.id.tvTitle,false)
            holder.setText(R.id.tvTitle, item.title)
        }else{
            holder.setGone(R.id.tvTitle,true)
        }

        holder.getView<ShowAllTextView>(R.id.tvDesc)
            .create(ShowAllTextView.Builder().setText(StringUtils.toPlainText(item.content)))

        val llComment = holder.getView<LinearLayout>(R.id.llReply)
        if (item.comment_list != null && item.comment_list.size > 0) {
            llComment.visibility = View.VISIBLE
            llComment.removeAllViews()
            var siz = item.comment_list.size - 1
            if (item.comment_list.size > 3) {
                siz = 2
            }

            for (i in 0..siz) {
                val txt = TextView(context)

                var nickname = "该用户已注销"
                if (item.comment_list[i].nickname != null) {
                    nickname = item.comment_list[i].nickname
                }
                if (nickname.length > 8) {
                    nickname = nickname.substring(0, 8) + "..."
                }

                val spannableString =
                    SpannableString("$nickname: ${item.comment_list[i].content}")
                if (item.comment_list[i].content != null && item.comment_list[i].content.isNotEmpty()) {
                    val colorSpan = ForegroundColorSpan(Color.parseColor("#333333"))
                    spannableString.setSpan(
                        colorSpan,
                        nickname.length + 1,
                        spannableString.length,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                    )
                }
                txt.text = spannableString
                txt.maxLines = 2
                txt.ellipsize = TextUtils.TruncateAt.END
                llComment.addView(txt)
            }
        } else {
            llComment.visibility = View.GONE
        }

        holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))
        llComment.setOnClickListener {
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            listener?.onRefresh()
            GameReviewsActivity.actionStart(context, item.game_id, item.id,1)
        }
        holder.getView<View>(R.id.tvRead).setOnClickListener {
            if (SpUtils.getString(R.string.token, "").isEmpty()) {
                EventBus.getDefault().post(ClassEvent("LoginActivity"))
                return@setOnClickListener
            }
            listener?.onRefresh()
            GameReviewsActivity.actionStart(context, item.game_id, item.id,1)
        }


        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like,item.like_num)
        viewLike.mListener= LikeView.OnLikeViewListener { listener?.onLike(item) }
        holder.setGone(R.id.vViewBg,!isShowEditStatus)

        holder.getView<View>(R.id.vViewBg).setOnClickListener {
            item.isSelected = !item.isSelected
            cb_check.isChecked = item.isSelected
        }
        val tvShare = holder.getView<TextView>(R.id.tvShare)
        tvShare.text = StringUtils.sizeToString(item.share_num)
        tvShare.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            listener?.onShare(item)
        }
    }


    interface OnHomePushListener {
        fun onShare(item: AmwayBean)
        fun onLike(item: AmwayBean)
        fun onAttention(item: AmwayBean)
        fun onRefresh()

    }

    var listener: OnHomePushListener? = null
}
