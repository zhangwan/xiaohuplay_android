package com.tinytiger.titi.adapter.circle.post


import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.circle.postsend.MultiFriendBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.common.view.title.AvatarView

import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView


/**
 *
 * @author zhw_luke
 * @date 2020/4/28 0028 上午 10:11
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 邀请回答
 */
class MultiFriendAdapter(data: ArrayList<MultiFriendBean>) :
    BaseMultiItemQuickAdapter<MultiFriendBean, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.circle_item_friend_text)
        addItemType(2, R.layout.circle_item_friend_info)
    }


    var tvNum: TextView? = null
    override fun convert(holder: BaseViewHolder, item: MultiFriendBean) {
        when (holder.itemViewType) {
            1 -> {
                holder.setText(R.id.tvName, item.defaulttxt)
            }
            2 -> {
                val bean = item.mBean

                val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
                tv_nickname.setNickname(bean.nickname)
                tv_nickname.setMedalIcon(bean.medal_image)

                holder.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar,bean.master_type)
                    .setUserId(""+bean.user_id)
                if (bean.old){
                    holder.setImageResource(R.id.ivCheck, R.mipmap.icon_check2_sel_old)
                }else if (bean.select) {
                    holder.setImageResource(R.id.ivCheck, R.mipmap.icon_check2_sel)
                } else {
                    holder.setImageResource(R.id.ivCheck, R.mipmap.icon_check2_nor)
                }
                holder.getView<RelativeLayout>(R.id.rl_top).setOnClickListener {
                    if (bean.old){
                        return@setOnClickListener
                    }
                    if (!bean.select && num >= 5) {
                        ToastUtils.show(context, "最多选择5个")
                        return@setOnClickListener
                    }

                    bean.select = !bean.select
                    notifyItemChanged(holder.adapterPosition)
                    getNumber()
                }
            }
        }
    }

    var num = 0
    fun getNumber() {
        num = 0
        for (i in data) {
            if (i.mBean != null && i.mBean.select) {
                num++
            }
        }

        tvNum?.text = "(${num}/5)"
    }

    fun setlist(list: ArrayList<MultiFriendBean>) {
        setNewInstance(list)
        getNumber()
    }

    fun setSearchlist(list: ArrayList<MultiFriendBean>) {
        for (i in list) {
            if (i.mBean != null) {
                i.mBean.select = false
            }
        }

        setlist(list)
    }
}