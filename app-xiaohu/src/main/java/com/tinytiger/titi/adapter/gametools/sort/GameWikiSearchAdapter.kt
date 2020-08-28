package com.tinytiger.titi.adapter.gametools.sort


import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.wiki.WikiSearchBean
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏百科搜索   adapter
*/
class GameWikiSearchAdapter:
    BaseQuickAdapter<WikiSearchBean, BaseViewHolder>(R.layout.game_item_wiki_search, null) {

   var searchTxt=""
    override fun convert(holder: BaseViewHolder, item: WikiSearchBean) {
        holder.setText(R.id.tv_title, item.name)
        val  tv_title=holder.getView<TextView>(R.id.tv_title)
        setCocorTxt(item.name,tv_title)

        //# 1百科  2词条
        holder.setText(R.id.tv_info,if(item.is_type ==1) "百科" else "词条")

        holder.getView<View>(R.id.ll_content).setOnClickListener {
            if(item.is_type ==1){
                GameDetailActivity.actionStart(context,item.game_id,1)
            }else{
                GameWikiDetailActivity.actionStart(context,item.id)
            }
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
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray33)),
                    0, txt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            0 -> {
                //开头匹配
                // 一定为用户名
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray99)),
                    0, searchTxt.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (txt.length > searchTxt.length) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray33)),
                        searchTxt.length, txt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            else -> {
                //中间匹配

                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray33)),
                    0, ind,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                style.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray99)),
                    ind, searchTxt.length + ind,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                if (txt.length > searchTxt.length) {
                    style.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.gray33)),
                        searchTxt.length + ind, txt.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
        }
        view.text = style
    }


}