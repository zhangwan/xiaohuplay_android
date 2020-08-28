package com.tinytiger.titi.adapter.mine.user


import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.WikiBean
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.OnItemCheckListener
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 我的作品 安利文 adapter
*/
class CollectWikiListAdapter(private val listener: OnItemCheckListener) : BaseQuickAdapter<WikiBean, BaseViewHolder>(R.layout.collect_item_wiki, null) {

    var isShowEditStatus = false


    override fun convert(holder: BaseViewHolder, item: WikiBean) {
        val content = holder.getView<View>(R.id.ll_content)
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tv_game, item.game_name)

        val params = content.layoutParams
        params.width = ScreenUtil.getScreenWidth()
        content.layoutParams = params

        holder.setGone(R.id.cb_check, !isShowEditStatus)
        val cb_check = holder.getView<CheckBox>(R.id.cb_check)
        if (isShowEditStatus) {
            cb_check.isChecked = item.isSelected
            cb_check.setOnCheckedChangeListener { _, b ->
                item.isSelected = b
                listener.onCheck(b)
            }
        }

        holder.getView<View>(R.id.ll_content).setOnClickListener {
            if (isShowEditStatus) {
                item.isSelected = !cb_check.isSelected
                cb_check.isChecked = item.isSelected
            } else {
                GameWikiDetailActivity.actionStart(context, item.content_id)
            }
        }

    }



}