package com.tinytiger.titi.adapter.gametools.info



import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.logger.Logger
import com.tinytiger.common.net.data.gametools.GameInfoDetailBean
import com.tinytiger.titi.R
import java.util.ArrayList


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 游戏_标签列表
 */
class GameTagAdapter :
    BaseQuickAdapter<GameInfoDetailBean.GameCate, BaseViewHolder>(R.layout.item_text1, null) {

    override fun convert(holder: BaseViewHolder, item: GameInfoDetailBean.GameCate) {
        val tvTxt = holder.getView<TextView>(R.id.tvTxt)
        tvTxt.background=ContextCompat.getDrawable(context,R.drawable.solid_rectangle_5_ebdab7)
        tvTxt.text=item.cate_name
    }

    fun setNumData(list :ArrayList<GameInfoDetailBean.GameCate>){
        var size=0
        var txt=""
        for (i in 0..list.size-1){
            txt+=list[i].cate_name
            if (txt.length<54){
                size=i
            }
        }
        if (size+1<=list.size){
            size+=1
        }


        setNewInstance(list.subList(0,size))
        //setNewInstance(list)
    }

}
