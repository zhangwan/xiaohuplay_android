package com.tinytiger.titi.adapter.search


import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.search.SearchUserInfo
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R

import com.tinytiger.common.event.SearchEvent
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.AttentionView
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索用户记录
 */
class SearchUserAdapter :
    BaseQuickAdapter<SearchUserInfo, BaseViewHolder>(R.layout.search_item_user, null) {
    /**
     * 匹配字段
     */
    var searchTxt = ""
    override fun convert(holder: BaseViewHolder, bean: SearchUserInfo) {
        var city="在你心里"
        if (bean.provcn!=null&&bean.provcn.isNotEmpty()){
            if (bean.provcn==bean.districtcn){
                city=bean.provcn
            }else{
                city=bean.provcn+bean.districtcn
            }
        }

        if (bean.fans_num!=0){
            city="$city  粉丝: ${StringUtils.sizeToString(bean.fans_num)}"
        }

        holder.setText(R.id.tvInfo, city)

        val tv_nickname : MedalTextView = holder.getView(R.id.tvName)
        tv_nickname.setNickname(bean.nickname)
        tv_nickname.setMedalIcon(bean.medal_image)

        when(bean.gender){
            1->{
                holder.setImageDrawable(R.id.ivGender,ContextCompat.getDrawable(context,R.mipmap.icon_gender_male))
            }
            2->{
                holder.setImageDrawable(R.id.ivGender,ContextCompat.getDrawable(context,R.mipmap.icon_gender_girl))
            }
            else->{
                holder.setImageDrawable(R.id.ivGender,null)
            }
        }

        val avAttention = holder.getView<AttentionView>(R.id.avAttention)
        avAttention.setMutual(bean.is_mutual)
        avAttention.mListener = object : AttentionView.OnAttentionViewListener {
            override fun onAttentionView(is_mutual: Int) {
                EventBus.getDefault().post(SearchEvent(bean.user_id, holder.adapterPosition,bean.is_mutual))
            }
        }


        holder.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar, bean.master_type)
        holder.getView<RelativeLayout>(R.id.rlItem).setOnClickListener {
            HomepageActivity.actionStart(context,bean.user_id)
            SearchAgentUtils.setSearchRoute(searchTxt,3)
        }

    }

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