package com.tinytiger.titi.adapter.home2.push


import android.text.TextUtils
import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.tinytiger.common.net.data.home2.HomeRecommendBean
import com.tinytiger.common.net.data.home2.PushBeanMultiRevise
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.image.GlideUtil

import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity
import com.tinytiger.titi.ui.home2.AmwayActivity
import com.xwdz.download.core.DownloadEntry
import kotlinx.android.synthetic.main.home_item_topic.view.*
import kotlinx.android.synthetic.main.item_game_av.view.*


/**
 *
 * @author zhw_luke
 * @date 2019/11/19 0019 上午 11:26
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页资讯列表
 */
class HomeAdapter(data: ArrayList<PushBeanMultiRevise>) :
    BaseMultiItemQuickAdapter<PushBeanMultiRevise, BaseViewHolder>(data) {

    init {
        //bottom数据流
        addItemType(1, R.layout.item_game_av)
        //专题
        addItemType(2, R.layout.home_item_topic)
        //安利墙
        addItemType(3, R.layout.home_item_list)
    }

    val mAdapter1 by lazy { HomeGame1dapter() }
    val mAdapter2 by lazy { HomeGame2dapter() }
    var bannerItemClickListener: ((bean: HomeRecommendBean) -> Unit)? = null
    var gameItemClickListener: ((bean: HomeRecommendBean) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: PushBeanMultiRevise) {
        when (holder.itemViewType) {
            1 -> {
                with(holder.itemView) {
                    var homeBean = item.homeBeans
                    tvRecommendTitle.visibility=View.GONE
                    if ("1" == homeBean.recommend_type) {
                        layoutGame.visibility = View.VISIBLE
                        tvType.visibility = View.GONE
                        if (homeBean?.game_info!!.thumbnail != null) {
                            GlideUtil.loadImg(ivIconBg,homeBean?.game_info!!.thumbnail,6f)
                        }
                        if (homeBean?.game_info?.logo != null) {
                            GlideUtil.loadImg(ivLogo, homeBean?.game_info?.logo)
                        }

                        tvGameName.text = homeBean?.game_info?.name
                        tvGameDesc.text = "来自"+homeBean?.game_info?.game_from

                        if ("1" == homeBean?.game_info?.cloud_game) {
                            ivSecondPlay.visibility = View.VISIBLE
                        } else {
                            ivSecondPlay.visibility = View.GONE
                        }
                        layout.setOnClickListener {
                            gameItemClickListener?.invoke(homeBean)
                        }
                        if(!TextUtils.isEmpty(homeBean?.game_info?.recommend_title)){
                            tvRecommendTitle.visibility=View.VISIBLE
                            tvRecommendTitle.text=homeBean?.game_info?.recommend_title
                        }

                    } else {
                        layoutGame.visibility = View.GONE
                        tvType.visibility = View.VISIBLE

                        if (homeBean?.content?.advertisement_img != null) {
                            GlideUtil.loadImg(ivIconBg, homeBean?.content?.advertisement_img,6f)
                        }
                        tvType.text = "来自广告"
                        layout.setOnClickListener {
                            bannerItemClickListener?.invoke(homeBean)
                        }
                    }

                }

            }
            2 -> {
                var homeBean = item.homeBeans
                with(holder.itemView) {
                    tvTitle.text = homeBean.cate_name
                    if (homeBean.game_list != null) {
                        for (i in 0 until homeBean.game_list!!.size) {
                            when (i) {
                                0 -> {
                                    GlideUtil.loadImg(ivIcon3, homeBean?.game_list!![i].logo)
                                }
                                1 -> {
                                    GlideUtil.loadImg(ivIcon4, homeBean?.game_list!![i].logo)
                                }
                                2 -> {
                                    GlideUtil.loadImg(ivIcon6, homeBean?.game_list!![i].logo)
                                }
                                3 -> {
                                    GlideUtil.loadImg(ivIcon2, homeBean?.game_list!![i].logo)
                                }
                                4 -> {
                                    GlideUtil.loadImg(ivIcon7, homeBean?.game_list!![i].logo)
                                }
                                5 -> {
                                    GlideUtil.loadImg(ivIcon1, homeBean?.game_list!![i].logo)
                                }
                                6 -> {
                                    GlideUtil.loadImg(ivIcon5, homeBean?.game_list!![i].logo)
                                }
                            }
                        }
                        if(homeBean.game_list!!.size>=2){
                            rlGameInfo.visibility=View.VISIBLE
                        }else{
                            rlGameInfo.visibility=View.GONE
                        }
                    }

                    ivIcon1.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                    ivIcon2.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                    ivIcon3.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                    ivIcon4.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                    ivIcon5.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                    ivIcon6.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                    ivIcon7.setOnClickListener {
                        startGameCategoryDetailActivity(homeBean.cate_name ?: "",
                            homeBean.cate_id ?: 0)
                    }
                }
            }
            3 -> {
                with(holder.itemView) {
                    view_line.visibility = View.VISIBLE
                }
                holder.setText(R.id.tvGameName, "安利墙")
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
                recyclerList.adapter = adapter
                if (item.mAmwayWallListBean != null) {
                    adapter.setNewInstance(item.mAmwayWallListBean.data)
                }
            }


        }
    }

    fun startGameCategoryDetailActivity(cateName: String, cateId: Int) {
        GameCategoryDetailActivity.actionStart(context, cateName, cateId)
    }


    fun setResolveInfo() {
        mAdapter1.installApk()
        mAdapter2.installApk()
    }

    fun notifyUpdate(item: DownloadEntry) {
        mAdapter1.notifyUpdate(item)
        mAdapter2.notifyUpdate(item)
    }

}
