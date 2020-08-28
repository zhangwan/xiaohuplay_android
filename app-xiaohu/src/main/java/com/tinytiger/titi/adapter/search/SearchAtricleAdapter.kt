package com.tinytiger.titi.adapter.search



import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.search.SearchNewsInfo
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 文章搜索
 */
class SearchAtricleAdapter :
    BaseQuickAdapter<SearchNewsInfo, BaseViewHolder>(R.layout.search_item_article, null) {
    /**
     * 匹配字段
     */
    var searchTxt = ""
    override fun convert(helper: BaseViewHolder, mData: SearchNewsInfo) {
        helper.setText(R.id.tvName, "#${mData.gamename}")
        helper.setText(R.id.tvTitile, mData.title)
       // setCocorTxt(mData.title, helper.getView(R.id.tvTitile))
        GlideUtil.loadImg(helper.getView<ImageView>(R.id.ivIcon), mData.cover)
        helper.setText(R.id.tvSize, StringUtils.sizeToString(mData.comment_num))
        if (mData.type==1){
            helper.setGone(R.id.ivVideo,true)
        }else{
            helper.setGone(R.id.ivVideo,false)
        }

        helper.getView<RelativeLayout>(R.id.rlItem).setOnClickListener {

            if (mData.type==2){
                VideoDetailActivity.actionStart(context,mData.id,mData.video_url)
            }else{
                NewsDetailActivity.actionStart(context,mData.id)
            }
            SearchAgentUtils.setSearchRoute(searchTxt,4)
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