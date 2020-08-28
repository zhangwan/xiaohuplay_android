package com.tinytiger.titi.adapter.home2


import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.tinytiger.common.net.data.gametools.comment.CommentAssessBean
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 我的作品  安利文
 */
class MineAmwayAdapter :
    BaseQuickAdapter<AmwayBean, BaseViewHolder>(R.layout.mine_item_amway, null) {

    /**
     * 使用在我的作品中
     * 是否显示选中状态
     */
    var showDeleteIcon = false

    override fun convert(holder: BaseViewHolder, item: AmwayBean) {

        val rl_content = holder.getView<RelativeLayout>(R.id.rl_content)
        val cb_check = holder.getView<CheckBox>(R.id.cb_check)

        GlideUtil.loadImg(holder.getView(R.id.ivIcon1), item.thumbnail)
        holder.setText(R.id.tvGameName, item.game_name)
        holder.setText(R.id.tvGameSize, StringUtils.sizeToString(item.amway_assess_num) + "条安利")

        if (item.title.isNotEmpty()) {
            holder.setGone(R.id.tvTitle, false)
            holder.setText(R.id.tvTitle, item.title)
        } else {
            holder.setGone(R.id.tvTitle, true)
        }
        holder.setVisible(R.id.cb_check, showDeleteIcon)
        holder.getView<ShowAllTextView>(R.id.tvDesc)
            .create(ShowAllTextView.Builder().setText(StringUtils.toPlainText(item.content)))
        if (showDeleteIcon) {
            cb_check.isChecked = item.isSelected
        }

        rl_content.setBackgroundResource(if (item.isSelected) R.mipmap.shadow_ffcc03 else R.mipmap.shadow_white)
        cb_check.setOnCheckedChangeListener { _, isChecked ->
            item.isSelected = isChecked
            rl_content.setBackgroundResource(if (item.isSelected) R.mipmap.shadow_ffcc03 else R.mipmap.shadow_white)
            listener?.onChecked(isChecked)
        }


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


        val llReply = holder.getView<LinearLayout>(R.id.llReply)
        if (item.comment_list != null && item.comment_list.size > 0) {
            setLlComment(llReply, item.comment_list)
        } else {
            llReply.visibility = View.GONE
        }

        holder.setText(R.id.tvRead, StringUtils.sizeToString(item.comment_num))

        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        viewLike.setLike(item.is_like, item.like_num)
        viewLike.mListener = LikeView.OnLikeViewListener { listener?.onLike(item) }
        holder.setGone(R.id.vViewBg,!showDeleteIcon)

        holder.getView<View>(R.id.vViewBg).setOnClickListener {
            item.isSelected = !item.isSelected
            cb_check.isChecked = item.isSelected
        }

        val tvShare = holder.getView<TextView>(R.id.tvShare)
        tvShare.text = StringUtils.sizeToString(item.share_num)


    }


    interface OnHomePushListener {
        fun onLike(item: AmwayBean)
        fun onChecked(is_check: Boolean)
    }

    var listener: OnHomePushListener? = null


    private fun setLlComment(llComment: LinearLayout, comment_list: java.util.ArrayList<CommentAssessBean>) {
        llComment.visibility = View.VISIBLE
        llComment.removeAllViews()
        for (i in 0..comment_list.size - 1) {
            val txt = TextView(context)

            var nickname = "该用户已注销"
            if (comment_list[i].nickname != null) {
                nickname = comment_list[i].nickname
            }
            if (nickname.length > 8) {
                nickname = nickname.substring(0, 8) + "..."
            }

            txt.setTextColor(ContextCompat.getColor(context, R.color.gray66))
            txt.text = "$nickname: ${comment_list[i].content}"
            txt.textSize = 14.toFloat()
            txt.maxLines = 1
            txt.ellipsize = TextUtils.TruncateAt.END

            llComment.addView(txt)
        }
    }
}
