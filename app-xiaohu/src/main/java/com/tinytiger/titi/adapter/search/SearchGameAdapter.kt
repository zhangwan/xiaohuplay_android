package com.tinytiger.titi.adapter.search



import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.search.SearchGameInfo
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索
 */
class SearchGameAdapter :
    BaseQuickAdapter<SearchGameInfo, BaseViewHolder>(R.layout.search_item_game, null) {
    /**
     * 匹配字段
     */
    var searchTxt = ""
    override fun convert(helper: BaseViewHolder, mData: SearchGameInfo) {
        helper.setText(R.id.tvInfo, mData.one_introduce)

        helper.setText(R.id.tvName, mData.name)
        GlideUtil.loadImg(helper.getView(R.id.iv_image), mData.logo)
        helper.getView<View>(R.id.rlItem).setOnClickListener {
            GameDetailActivity.actionStart(context,mData.id,5)
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
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray28)),
                    0, txt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            0 -> {
                //开头匹配
                // 一定为用户名
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.color_FF2D2D)),
                    0, searchTxt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (txt.length > searchTxt.length) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray28)),
                        searchTxt.length, txt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            else -> {
                //中间匹配

                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray28)),
                    0, ind,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.color_FF2D2D)),
                    ind, searchTxt.length + ind,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (txt.length > searchTxt.length) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray28)),
                        searchTxt.length + ind, txt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
        }
        view.text = style
    }
}