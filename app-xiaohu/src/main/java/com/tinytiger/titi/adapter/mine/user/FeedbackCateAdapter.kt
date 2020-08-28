package com.tinytiger.titi.adapter.mine.user


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.mine.CatListBean
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 用户意见收发室分类 adapter
*/
class FeedbackCateAdapter : BaseQuickAdapter<CatListBean.CateBean, BaseViewHolder>(R.layout.item_cate, null) {

    override fun convert(holder: BaseViewHolder, item: CatListBean.CateBean) {
        holder.setText(R.id.tv_name,item.type_name)
        holder.setBackgroundResource(R.id.tv_name,if(!item.is_selected) R.drawable.solid_rectangle_5_ffffff else R.drawable.solid_rectangle_5_ffcc03)

    }



}