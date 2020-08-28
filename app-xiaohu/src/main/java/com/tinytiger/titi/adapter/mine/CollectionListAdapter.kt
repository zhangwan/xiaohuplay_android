package com.tinytiger.titi.adapter.mine


import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.mine.ContentBean
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.mine.user.OnItemCheckListener
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity


/*
* @author Tamas
* create at 2019/11/16 0016
* Email: ljw_163mail@163.com
* description:
*/
class CollectionListAdapter(private val listener: OnItemCheckListener) : BaseQuickAdapter<ContentBean, BaseViewHolder>(R.layout.swipe_item_news, null) {

    var isShowEditStatus = false
    override fun convert(holder: BaseViewHolder, item: ContentBean) {
        var mRlItem = holder.getView<View>(R.id.rlItem)
        val screenWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val screenHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        mRlItem.measure(screenWidth, screenHeight)
        val height: Int = mRlItem.measuredHeight // 获取高度

        var mViewBg = holder.getView<View>(R.id.vViewBg)
        var layoutParams = mViewBg.layoutParams
        layoutParams.height = height
        mViewBg.layoutParams = layoutParams

        holder.setText(R.id.tvName, "#${item.game_name}")

        holder.setText(R.id.tvTitile, item.title)
        GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), item.cover)
        holder.setText(R.id.tvSize, StringUtils.sizeToString(item.comment_num))

        if (item.type==1){
            holder.setGone(R.id.ivVideo,true)
        }else{
            holder.setGone(R.id.ivVideo,false)
        }

        val content = holder.getView<View>(R.id.rlItem)
        val params = content.layoutParams
        params.width = ScreenUtil.getScreenWidth()
        content.layoutParams = params

        holder.setGone(R.id.cb_check, !isShowEditStatus)
        holder.setGone(R.id.vViewBg, !isShowEditStatus)

        val cb_check = holder.getView<CheckBox>(R.id.cb_check)
        if (isShowEditStatus){
            cb_check.isChecked=item.isSelected
            cb_check.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, b ->
                item.isSelected=b
                listener.onCheck(b)
            })
        }
        holder.getView<View>(R.id.vViewBg).setOnClickListener {
            item.isSelected = !item.isSelected
            cb_check.isChecked = item.isSelected
        }

        content.setOnClickListener {
            if (isShowEditStatus) {
                item.isSelected = !cb_check.isSelected
                cb_check.isChecked = item.isSelected
            } else {

                if (item.type==1){
                    NewsDetailActivity.actionStart(context, ""+item.id)
                }else{
                    VideoDetailActivity.actionStart(context, ""+item.id, item.video_url)
                }

            }
        }
    }

}