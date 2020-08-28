package com.tinytiger.titi.adapter.mine

import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.mine.FeedbackBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView


/*
* @author Tamas
* create at 2020/1/7 0007
* Email: ljw_163mail@163.com
* description: 
*/
class FeedbackListAdapter : com.chad.library.adapter.base.BaseQuickAdapter<FeedbackBean, BaseViewHolder>(R.layout.mine_item_feedback, null)  {
    override fun convert(holder: BaseViewHolder, item: FeedbackBean) {
        if(item.replay_content == null || item.replay_content.isEmpty()){

            holder.setGone(R.id.tv_reply,true)
        }else{
            holder.setGone(R.id.tv_reply,false)

            val textSb = SpannableStringBuilder()
            val replyUserName = SpannableString("小虎回复")
            replyUserName.setSpan(ForegroundColorSpan(getResourceColor(holder.adapterPosition)), 0, replyUserName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textSb.append(replyUserName).append("：").append(item.replay_content)
            holder.setText(R.id.tv_reply,textSb)
        }

        holder.setBackgroundResource(R.id.ll_content,getResourceImage(holder.adapterPosition))
        holder.setText(R.id.tv_content,item.problem_desc)

        val tv_nickname = holder.getView<MedalTextView>(R.id.tv_name)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)
        if(item.avatar == null){
            holder.setImageResource(R.id.iv_avatar,R.mipmap.icon_person_head)
        }else{
            GlideUtil.loadImg(holder.getView(R.id.iv_avatar),item.avatar)
        }



    }



    private fun getResourceImage(pos:Int):Int{
      return  when(pos%4){
            0-> R.drawable.solid_rectangle_feedback_01
            1-> R.drawable.solid_rectangle_feedback_02
            2-> R.drawable.solid_rectangle_feedback_03
            else-> R.drawable.solid_rectangle_feedback_04
        }
    }




    private fun getResourceColor(pos:Int):Int{
        return  when(pos%4){
            0-> Color.parseColor("#FEB27A")
            1-> Color.parseColor("#FF9B9B")
            2->Color.parseColor("#F9C974")
            else-> Color.parseColor("#FD8757")
        }
    }

}