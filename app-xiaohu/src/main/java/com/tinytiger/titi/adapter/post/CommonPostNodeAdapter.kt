package com.tinytiger.titi.adapter.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.utils.umeng.CircleAgentUtils
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.titi.R
import com.tinytiger.titi.event.LikeEvent
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.widget.view.Anim.LikeView
import com.tinytiger.titi.widget.view.AttentionView
import kotlinx.android.synthetic.main.collect_item_note2.view.*
import kotlinx.android.synthetic.main.item_common_post_node.view.*
import org.greenrobot.eventbus.EventBus

/*
 * @author zwy
 * create at 2020/6/8 0008
 * description:所有帖子的adapter
 */
class CommonPostNodeAdapter(private val context: Context, var data: ArrayList<PostBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 0-任何类型 1-圈子主页 2-搜索 3-我的发布
     * */
    var type: Int = 0
    /**
     * 搜索模块关键词
     */
    var searchTxt = ""
    /**
     * 使用在我的作品中
     * 是否显示选中状态
     */
    var showDeleteIcon = false

    // 1.动态贴 2,.问答贴
    var isFlagType = 2

    var dp3 = 0

    //1-默认列表 2-我的发布 //3-我的收藏
    var pageIndex = ""
    var moreListener: ((bean: PostBean, view: View) -> Unit)? = null
    var checkListener: ((bean: PostBean) -> Unit)? = null
    var shareListener: ((bean: PostBean) -> Unit)? = null
    var likeListener: ((bean: PostBean) -> Unit)? = null
    var attentionListener: ((bean: PostBean, isMutual: Int) -> Unit)? = null
    var readListener: ((bean: PostBean) -> Unit)? = null
    var itemListener: ((bean: PostBean) -> Unit)? = null


    var mBottomView: View? = null
    var mHeaderView: View? = null
    var mEmptyView: View? = null //单独设置空View

    //true 可以播放视频 false 不可以播放视频
    var isVideoType: Boolean = false


    companion object {
        var TYPE_HEADER_DATA_ITEM = 2000
        val TYPE_BOTTOM_DATA_ITEM = 2001
        var TYPE_EMPTY_DATA_ITEM = 2002
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                var viewHolder = CommonPostNodeViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.collect_item_note2, parent,
                        false))
                with(viewHolder.itemView) {
                    if (pageIndex == ConstantsUtils.NOTE.PAGE_COLLECT) {
                        rl_content.setBackgroundResource(R.color.white)
                        rl_content.setPadding(0,
                            context.resources.getDimensionPixelOffset(R.dimen.size15), 0, 0)
                        var lp = rl_content.layoutParams as FrameLayout.LayoutParams
                        lp.setMargins(context.resources.getDimensionPixelOffset(R.dimen.size15), 0,
                            context.resources.getDimensionPixelOffset(R.dimen.size15), 0)
                        rl_content.layoutParams = lp

                    } else if (pageIndex == ConstantsUtils.NOTE.PAGE_RELEASE) {

                        rl_content.setBackgroundResource(R.drawable.stroke_rectangle_b3b3b3)
                        rl_content.setPadding(
                            context.resources.getDimensionPixelOffset(R.dimen.size10),
                            context.resources.getDimensionPixelOffset(R.dimen.size10),
                            context.resources.getDimensionPixelOffset(R.dimen.size10),
                            context.resources.getDimensionPixelOffset(R.dimen.size10))
                        var lp = rl_content.layoutParams as FrameLayout.LayoutParams
                        lp.setMargins(context.resources.getDimensionPixelOffset(R.dimen.size10),
                            context.resources.getDimensionPixelOffset(R.dimen.size20),
                            context.resources.getDimensionPixelOffset(R.dimen.size10), 0)
                        rl_content.layoutParams = lp
                    }
                }
                return viewHolder
            }
            TYPE_BOTTOM_DATA_ITEM -> {
                return MyEmptyViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.load_end_layout_new, parent,
                        false))
            }
            TYPE_HEADER_DATA_ITEM -> {
                return MyEmptyViewHolder(mHeaderView ?: View(context))
            }
            TYPE_EMPTY_DATA_ITEM -> {
                return MyEmptyViewHolder(mEmptyView ?: View(context))
            }
            else -> {
                return MyEmptyViewHolder(mEmptyView ?: View(context))
            }
        }

    }

    override fun getItemCount(): Int {
        if (mEmptyView != null && data.size == 0) {
            return 1
        }
        var count = getHeaderSize()
        if (mBottomView != null) {
            count += 1
        }
        return count
    }


    override fun getItemViewType(position: Int): Int {
        return if (mEmptyView != null && data.size == 0) {
            TYPE_EMPTY_DATA_ITEM
        } else if (mHeaderView != null && position == 0) {
            TYPE_HEADER_DATA_ITEM
        } else if (mBottomView != null && position == getHeaderSize()) {
            TYPE_BOTTOM_DATA_ITEM
        } else {
            super.getItemViewType(position)
        }
    }

    private fun getHeaderSize(): Int {
        return if (mHeaderView != null) {
            data.size + 1
        } else {
            data.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            var item = if (mHeaderView != null) {
                data[position - 1]
            } else {
                data[position]
            }


            with(holder.itemView) {
                when (pageIndex) {
                    ConstantsUtils.NOTE.PAGE_RELEASE -> {
                        if (isFlagType == 1) {
                            item.answer_id = -1
                        }
                        vView.visibility = View.GONE
                        if (showDeleteIcon) {
                            iv_check.isSelected = item.isSelected
                            rl_content.setBackgroundResource(
                                if (item.isSelected) R.drawable.stroke_rectangle_ffcc03 else R.drawable.stroke_rectangle_b3b3b3)
                            iv_check.visibility = View.VISIBLE
                            vbg.visibility = View.VISIBLE
                        } else {
                            rl_content.setBackgroundResource(R.drawable.stroke_rectangle_b3b3b3)
                            iv_check.visibility = View.GONE
                            vbg.visibility = View.GONE
                        }
                        iv_check.setOnClickListener {
                            item.isSelected = !item.isSelected
                            notifyItemChanged(holder.adapterPosition)
                            checkListener?.invoke(item)
                        }

                        iv_more.visibility = if (type == 3) View.VISIBLE else View.GONE
                        iv_more.setOnClickListener {
                            moreListener?.invoke(item, iv_more)
                        }

                        vbg.setOnClickListener {
                            item.isSelected = !item.isSelected
                            checkListener?.invoke(item)
                            notifyItemChanged(holder.adapterPosition)
                        }
                    }
                    ConstantsUtils.NOTE.PAGE_COLLECT -> {
                        if (item.create_time.isNullOrEmpty()) {
                            tv_time.visibility = View.GONE
                        } else {
                            tv_time.visibility = View.VISIBLE
                            tv_time.text =
                                "收藏于" + TimeZoneUtil.getShortTimeShowString(item.collect_time)
                        }
                        if (dp3 != 0) {
                            //收藏状态
                            val params = rl_content.layoutParams
                            params.width = dp3
                            rl_content.layoutParams = params
                        }
                        if (showDeleteIcon) {
                            vbg.visibility = View.VISIBLE
                            cb_check.visibility = View.VISIBLE
                        } else {
                            vbg.visibility = View.GONE
                            cb_check.visibility = View.GONE
                        }
                        if (showDeleteIcon) {
                            cb_check.isChecked = item.isSelected
                            cb_check.setOnCheckedChangeListener(
                                CompoundButton.OnCheckedChangeListener { _, b ->
                                    item.isSelected = b
                                    checkListener?.invoke(item)
                                })
                        }
                        vbg.setOnClickListener {
                            item.isSelected = !item.isSelected
                            cb_check.isChecked = item.isSelected
                        }
                        item.circle_list = item.circle_post
                        for (i in 0 until item.circle_post.size) {
                            item.circle_list[i].circle_id = item.circle_post[i].id
                        }

                    }
                    ConstantsUtils.NOTE.PAGE_DEFAULT -> {
                        vbg.visibility = View.GONE
                        vView.visibility = View.VISIBLE
                    }
                }
                BasePostNodeDataUtils.setPostNodeView(context, item, holder.itemView, position,
                    isVideoType, shareListener)

                tvAttention.mListener = object : AttentionView.OnAttentionViewListener {
                    override fun onAttentionView(is_mutual: Int) {
                        attentionListener?.invoke(item, is_mutual)
                    }
                }
                viewLike.mListener = LikeView.OnLikeViewListener {
                    likeListener?.invoke(item)
                    item.like_num = viewLike.like_num
                    item.is_like = viewLike.is_like
                    EventBus.getDefault().post(LikeEvent(item.id, item.like_num, item.is_like))
                    if (viewLike.is_like == 1) {
                        CircleAgentUtils.setCirclePostInfo(item.circle_id, item.id, 1)
                    }
                }
                tvShare.setOnClickListener {
                    if (item.state == 0) {
                        ToastUtils.toshort(context!!,
                            context.getString(R.string.post_video_no_share_tip))
                    } else {
                        shareListener?.invoke(item)
                    }
                }
                tvRead.setOnClickListener {
                    readListener?.invoke(item)
                }
                rl_content.setOnClickListener {
                    if (pageIndex == ConstantsUtils.NOTE.PAGE_RELEASE) {
                        if (showDeleteIcon) {
                            item.isSelected = !item.isSelected
                            itemListener?.invoke(item)
                            notifyItemChanged(holder.adapterPosition)
                        } else {
                            startPostActivity(item)
                        }
                    } else if (pageIndex == ConstantsUtils.NOTE.PAGE_COLLECT) {
                        if (showDeleteIcon) {
                            item.isSelected = !item.isSelected
                            cb_check.isChecked = item.isSelected
                        } else {
                            startPostActivity(item)
                        }
                    } else {
                        startPostActivity(item)
                    }

                }
                llAllTextParent.setOnClickListener {
                    startPostActivity(item)
                }


                tvName1.setOnClickListener {
                    if (1 == type) {
                        itemListener?.invoke(item)
                    } else {
                        CirclesDetailsActivity.actionStart(context, item.circle_id, item.modular_id,
                            "")
                    }

                }


            }
        }
    }

    private fun startPostActivity(item: PostBean) {
        PostActivity.actionStart(context, item.id)
        if (type == 2) {
            SearchAgentUtils.setSearchRoute(searchTxt, 1)
        }
    }

    inner class MyEmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class CommonPostNodeViewHolder(view: View) : RecyclerView.ViewHolder(view)
}