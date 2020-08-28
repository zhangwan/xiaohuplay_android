package com.tinytiger.titi.adapter.gametools.info

import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView

import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.gametools.wiki.WikiChildBean
import com.tinytiger.common.utils.Dp2PxUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.indicator.WikiNavigatorView
import com.tinytiger.titi.R
import com.tinytiger.titi.data.MyUserData
import com.tinytiger.titi.data.game.MultiMyGameWikiBean


import com.tinytiger.titi.ui.search.GameWikiSearchActivity
import com.tinytiger.titi.utils.BannerStartUtils
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.titi.widget.view.Anim.AnimFollowGameView
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/3/26 0026 下午 4:34
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 游戏详情_百科
 */
class MultiGameWikiAdapter :
    BaseMultiItemQuickAdapter<MultiMyGameWikiBean, BaseViewHolder>(ArrayList()) {

    //1=横图banner模板 2=图标banner模板 3=上图下文分类模板 4=纯文字标题模板 5=左图右文分类模板 6=申请管理员模板
    init {
        //首屏
        addItemType(0, R.layout.game_wiki_item_search)

        //单图banner
        addItemType(7, R.layout.game_wiki_item_img)
        //多图banner
        addItemType(1, R.layout.game_wiki_item_banner1)

        //多图小bannner
        addItemType(2, R.layout.game_wiki_item_banner)

        //分类数据
        addItemType(3, R.layout.game_wiki_item_introduction)
        addItemType(4, R.layout.game_wiki_item_introduction)
        addItemType(5, R.layout.game_wiki_item_introduction)

        //管理员
        addItemType(6, R.layout.game_wiki_item_add)
        //空白占位
        addItemType(10, R.layout.bg_item_10)

        addItemType(9, R.layout.view_empty)
    }

    var game_id = ""
    var logo = ""
    var name=""
    var afgvFollow: AnimFollowGameView? = null
    override fun convert(holder: BaseViewHolder, data: MultiMyGameWikiBean) {
        when (data.itemType) {
            0 -> {
                var ivAvatar = holder.getView<RoundedImageView>(R.id.iv_avatar)
                if (!TextUtils.isEmpty(logo)) {
                    GlideUtil.loadImg(ivAvatar, logo)
                }
                holder.setText(R.id.tv_name,name)
            }
            7 -> {
                val bean = data.mWikiModular
                GlideUtil.loadImg(holder.getView(R.id.ivIcon), bean.child_data[0].img_url)
                holder.getView<View>(R.id.ivIcon).setOnClickListener {
                    setbanner(bean.child_data[0])
                }
            }
            1 -> {
                val recycler_view = holder.getView<RecyclerView>(R.id.recycler_view)
                recycler_view.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val adapter = WikiBannerAdapter()
                recycler_view.adapter = adapter
                val bean = data.mWikiModular
                adapter.setNewInstance(bean.child_data)

                adapter.mListener = object : WikiBannerAdapter.OnBannerListener {
                    override fun setBannerId(item: WikiChildBean) {
                        setbanner(item)
                    }
                }
            }
            2 -> {
                val recycler_view = holder.getView<RecyclerView>(R.id.recycler_view)
                recycler_view.layoutManager = GridLayoutManager(context, 5)
                val adapter = WikiBannerMiniAdapter()
                recycler_view.adapter = adapter
                val bean = data.mWikiModular
                adapter.setNewInstance(bean.child_data)

                adapter.mListener = object : WikiBannerMiniAdapter.OnBannerListener {
                    override fun setBannerId(bean: WikiChildBean) {
                        setbanner(bean)
                    }
                }
            }
            3 -> {
                val bean = data.mWikiCategoryBean
                holder.setText(R.id.tvTitle, bean.cate_name)

                val viewNavigator = holder.getView<WikiNavigatorView>(R.id.viewNavigator)
                val recycler_view = holder.getView<RecyclerView>(R.id.recycler_view)
                recycler_view.layoutManager = GridLayoutManager(context, 4)
                recycler_view.minimumHeight = Dp2PxUtils.dip2px(context, 210)
                val adapter = WikiType1Adapter()
                adapter.game_id = game_id
                recycler_view.adapter = adapter
                viewNavigator.mListener = WikiNavigatorView.OnNavigatorListener {
                    bean.position = it
                    if (bean.son_category[it] != null && bean.son_category[it].content_list != null) {
                        adapter.setNewInstance(bean.son_category[it].content_list)
                    } else {
                        adapter.setNewInstance(ArrayList())
                    }
                }

                viewNavigator.postDelayed({
                    viewNavigator.setTitles(bean.son_category, bean.position)
                }, 100)
            }
            4 -> {
                val bean = data.mWikiCategoryBean
                holder.setText(R.id.tvTitle, bean.cate_name)
                val viewNavigator = holder.getView<WikiNavigatorView>(R.id.viewNavigator)
                val recycler_view = holder.getView<RecyclerView>(R.id.recycler_view)
                recycler_view.minimumHeight = Dp2PxUtils.dip2px(context, 90)
                recycler_view.layoutManager = GridLayoutManager(context, 4)
                val adapter = WikiType2Adapter()
                adapter.game_id = game_id
                recycler_view.adapter = adapter

                viewNavigator.mListener = WikiNavigatorView.OnNavigatorListener {
                    bean.position = it
                    if (bean.son_category[it] != null && bean.son_category[it].content_list != null) {
                        adapter.setNewInstance(bean.son_category[it].content_list)
                    } else {
                        adapter.setNewInstance(ArrayList())
                    }
                }

                viewNavigator.postDelayed({
                    viewNavigator.setTitles(bean.son_category, bean.position)
                }, 200)
            }
            5 -> {
                val bean = data.mWikiCategoryBean
                holder.setText(R.id.tvTitle, bean.cate_name)

                val viewNavigator = holder.getView<WikiNavigatorView>(R.id.viewNavigator)
                val recycler_view = holder.getView<RecyclerView>(R.id.recycler_view)
                recycler_view.layoutManager = GridLayoutManager(context, 4)
                recycler_view.minimumHeight = Dp2PxUtils.dip2px(context, 90)
                val adapter = WikiType3Adapter()
                adapter.game_id = game_id
                recycler_view.adapter = adapter

                viewNavigator.mListener = WikiNavigatorView.OnNavigatorListener {
                    bean.position = it
                    if (bean.son_category[it] != null && bean.son_category[it].content_list != null) {
                        adapter.setNewInstance(bean.son_category[it].content_list)
                    } else {
                        adapter.setNewInstance(ArrayList())
                    }
                }

                viewNavigator.postDelayed({
                    viewNavigator.setTitles(bean.son_category, bean.position)
                }, 200)

            }
            6 -> {
                holder.getView<View>(R.id.tvTitle).setOnClickListener {
                    mOnGameWikiItemListener?.setWikiApply()
                }
            }
            9 -> {
                holder.setBackgroundColor(R.id.empty_view,
                    ContextCompat.getColor(context, R.color.white))

            }
            else -> {

            }
        }
    }


    var mOnGameWikiItemListener: OnGameWikiItemListener? = null

    interface OnGameWikiItemListener {

        fun setGameFollow(follow_status: Int)
        fun setWikiApply()

        fun setBannerId(id: String)
    }


    /**
     * bannner点击处理
     */
    private fun setbanner(bean: WikiChildBean) {
        if (!MyNetworkUtil.isNetworkAvailable(context)) {
            ToastUtils.show(context, "当前网络不可用")
            return
        }
        if (MyUserData.isEmptyToken()) {
            EventBus.getDefault().post(ClassEvent("LoginActivity"))
            return
        }
      //  BannerAgentUtils.setBannerWikiDetailClick(bean.id)
        mOnGameWikiItemListener?.setBannerId(bean.id)

        BannerStartUtils.setStartIntent2(bean)
    }
}