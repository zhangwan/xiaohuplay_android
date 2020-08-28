package com.tinytiger.titi.adapter.home2.find


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.home2.FindBeanMulti
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.home2.push.HomeAmwaydapter
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity
import com.tinytiger.titi.ui.home2.AmwayActivity

/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 上午 11:26
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页发现列表
 */
class FindAdapter(data: ArrayList<FindBeanMulti>) :
    BaseMultiItemQuickAdapter<FindBeanMulti, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.home_item_list)
        //安利墙
        addItemType(2, R.layout.home_item_list)
        //新奇游戏
        addItemType(3, R.layout.home_item_list)
    }

    override fun convert(holder: BaseViewHolder, item: FindBeanMulti) {
        when (holder.itemViewType) {
            1 -> {

            }
            2 -> {
                holder.setText(R.id.tvGameName, "安利墙")
                holder.setGone(R.id.ivMore, true)
                val tvMore = holder.getView<TextView>(R.id.tvMore)
                tvMore.visibility = View.VISIBLE
                tvMore.text = "更多"
                tvMore.setOnClickListener {
                    AmwayActivity().actionStart(context)
                }

                val recyclerList = holder.getView<RecyclerView>(R.id.recycler_list)
                recyclerList.setPadding(Dp2PxUtils.dip2px(context, 13), 0, 0, 0)
                recyclerList.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val adapter = HomeAmwaydapter()
                adapter.open_from=1
                recyclerList.adapter = adapter
                adapter.setNewInstance(item.mAmwayWallListBean.data)
            }
            3 -> {
                holder.setText(R.id.tvGameName, item.defaulttxt)
                holder.setGone(R.id.tvMore, true)
                holder.setGone(R.id.ivMore, false)

                val recyclerList = holder.getView<RecyclerView>(R.id.recycler_list)
                recyclerList.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerList.adapter = GameSortAdapter(item.listBean.child)

                val ivMore = holder.getView<ImageView>(R.id.ivMore)
                ivMore.setOnClickListener {
                    GameCategoryDetailActivity.actionStart(context, item.defaulttxt, item.id)
                }
            }
        }
    }


}
