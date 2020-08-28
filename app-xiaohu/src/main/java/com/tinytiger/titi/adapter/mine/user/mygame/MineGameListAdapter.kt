package com.tinytiger.titi.adapter.mine.user.mygame



import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.CheckUtils


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 我的游戏 adapter
*/
class MineGameListAdapter : BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.mine_item_game_list, null) {

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        GlideUtil.loadImg( holder.getView(R.id.iv_avatar),item.logo)
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tvDesc, item.cate_name)

        if(item.package_name_android!=null && item.package_name_android.isNotEmpty()&& CheckUtils.isApplicationAvilible(context,item.package_name_android)){
            holder.setText(R.id.tv_action, "打开")
            item.has_application = true
        }else{
            item.has_application = false
            holder.setText(R.id.tv_action, "查看")
        }
    }

    fun removeBean(game_id:String){
        for (i in data) {
            if (game_id == i.id) {
                remove(i)
                break
            }
        }

    }

}