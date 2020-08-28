package com.tinytiger.titi.adapter.mine.user.mygame


import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.utils.CheckUtils
import kotlinx.android.synthetic.main.collect_item_note2.view.*
import kotlinx.android.synthetic.main.item_common_post_node.view.*


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 我的游戏 adapter
*/
class MineCollectGameAdapter :
    BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.mine_collect_item_game_list, null) {
    var dp3 = 0
    /**
     * 使用在我的收藏中
     * 是否显示选中状态
     */
    var showDeleteIcon = false
    var checkListener: ((bean: MineGameBean) -> Unit)? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        GlideUtil.loadImg(holder.getView(R.id.iv_avatar), item.logo)
        holder.setText(R.id.tv_name, item.name)
        if(!TextUtils.isEmpty(item.cate_name)){
            var index=item.cate_name.indexOf("/")
            if(index!=-1){
                var cateName=item.cate_name.split("/")
                var cateNameText=StringBuffer()
                for(i in cateName.indices) {
                    if (i > 3) {
                        break
                    }
                    cateNameText.append(cateName[i] + "/")
                    if (i == 1) {
                        cateNameText.deleteCharAt(cateNameText.length-1)
                        cateNameText.append("\n")
                    }

                }
                cateNameText.deleteCharAt(cateNameText.length-1)
                holder.setText(R.id.tvDesc, cateNameText)
            }else{
                holder.setText(R.id.tvDesc, item.cate_name)
            }
        }


        var cbCheck = holder.getView<CheckBox>(R.id.cb_check)
        var tvTime = holder.getView<TextView>(R.id.tv_time)
        var vbg = holder.getView<View>(R.id.vbg)
        if (item.create_time.isNullOrEmpty()) {
            tvTime.visibility = View.GONE
        } else {
            tvTime.visibility = View.VISIBLE
            tvTime.text = context.getString(R.string.collect_time,
                TimeZoneUtil.getShortTimeShowString(item.create_time))
        }

        if (showDeleteIcon) {
            vbg.visibility = View.VISIBLE
            cbCheck.visibility = View.VISIBLE
        } else {
            vbg.visibility = View.GONE
            cbCheck.visibility = View.GONE
        }
        if (showDeleteIcon) {
            cbCheck.isChecked = item.isSelected
            cbCheck.setOnCheckedChangeListener { _, b ->
                item.isSelected = b
                checkListener?.invoke(item)
            }
        }
        holder.getView<View>(R.id.rl_content).setOnClickListener {
            GameDetailActivity.actionStart(context!!, item.id, 7)
        }

        vbg.setOnClickListener {
            item.isSelected = !item.isSelected
            checkListener?.invoke(item)
            notifyItemChanged(holder.adapterPosition)
        }
    }

}