package com.tinytiger.titi.adapter.msg


import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.msg.ReplyBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil

import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索用户记录
 */
class ReplyUserAdapter :
    BaseQuickAdapter<ReplyBean, BaseViewHolder>(R.layout.msg_item_comment, null) {

    override fun convert(holder: BaseViewHolder, mBean: ReplyBean) {
        val llIcons = holder.getView<LinearLayout>(R.id.llIcons)
        llIcons.removeAllViews()

        var parent = if (mBean.comment_id == 0) {
            "评论了我的作品"
        } else {
            "回复了我的评论"
        }

        val tvInfo = holder.getView<TextView>(R.id.tvInfo)
        tvInfo.text = " \""+mBean.content
        holder.setText(R.id.tvInfo1, "\"")
        GlideUtil.loadImg(holder.getView(R.id.ivDateBg), mBean.cover)

        holder.getView<AvatarView>(R.id.avUser).setAvatar(mBean.avatar, mBean.master_type)
            .setUserId(mBean.user_id)        
        val tv_nickname : MedalTextView = holder.getView(R.id.tvName)
        tv_nickname.setNickname(StringUtils.stringName(mBean.nickname))
        tv_nickname.setMedalIcon(mBean.medal_image)


        holder.setText(R.id.tvDesc, parent)

        holder.setText(R.id.tvTime, TimeZoneUtil.getShortTimeShowString(mBean.create_time))
        if (mBean.is_read == 0) {
            holder.setGone(R.id.ivPoint, false)
        } else {
            holder.setGone(R.id.ivPoint, true)
        }
        var video=true
        holder.setGone(R.id.tvDesc2,true)
        when(mBean.type){
            1->{

            }
            2->{
                video=false
            }
            3->{

            }
            4->{
                if (mBean.video_url.isNotEmpty()){
                    video=false
                    if(mBean.cover.isEmpty()){
                        GlideUtil.loadVideoScreenshot(
                            mBean.video_url,
                            holder.getView(R.id.ivDateBg),
                            0
                        )
                    }
                }else if(mBean.cover.isEmpty()){
                    holder.setGone(R.id.tvDesc2,false)
                    holder .setText(R.id.tvDesc2,mBean.cover_content)
                }
            }
            5->{

                if (mBean.video_url.isNotEmpty()){
                    video=false
                    if(mBean.cover.isEmpty()){
                        GlideUtil.loadVideoScreenshot(
                            mBean.video_url,
                            holder.getView(R.id.ivDateBg),
                            0
                        )
                    }
                }else if(mBean.cover.isEmpty()){
                    holder.setGone(R.id.tvDesc2,false)
                    holder .setText(R.id.tvDesc2,mBean.cover_content)
                }
                if(mBean.cover.isEmpty()){
                    holder.setGone(R.id.tvDesc2,false)
                    holder .setText(R.id.tvDesc2,mBean.cover_content)
                }
            }
        }
        holder.setGone(R.id.ivVideo,video)
    }
}