package com.tinytiger.titi.adapter.gametools.sort


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.*
import com.tinytiger.common.net.data.gametools.category.GameCategoryBean
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏分类列表  右边菜单 adapter
*/
class GameCategoryAdapter :
    BaseQuickAdapter<GameCategoryBean, BaseViewHolder>(R.layout.game_item_type_category, null) {


    override fun convert(holder: BaseViewHolder, item: GameCategoryBean) {
        holder.setText(R.id.tv_title, item.cate_name)

        val recyclerView = holder.getView<RecyclerView>(R.id.recycler_view)

    // "template_id": 4,#模板id 3=上图下文分类模板 4=纯文字标题模板

        if(item.template_id == 3){
            recyclerView.layoutManager =   GridLayoutManager(context,2)
            recyclerView.adapter = GameCategoryImageAdapter(item.childCate)
        }else{
            val layoutManager = object :FlexboxLayoutManager(context,FlexDirection.ROW,FlexWrap.WRAP){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            recyclerView.layoutManager =   layoutManager
            recyclerView.adapter = GameCategoryListAdapter(item.childCate)
        }



    }


}