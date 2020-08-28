package com.tinytiger.titi.adapter.post

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import cn.jzvd.JZUtils
import cn.jzvd.Jzvd
import com.tinytiger.common.event.ShareEvent
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.circle.post.SelectFriendActivity
import com.tinytiger.titi.ui.video.VideoActivity
import com.tinytiger.titi.utils.AutoPlayUtils
import com.tinytiger.titi.widget.video.MyJzVideoView
import kotlinx.android.synthetic.main.item_common_post_node.view.*


/**
 * @author zwy
 * create at 2020/6/9 0009
 * description:
 */
object BasePostNodeDataUtils {
    //控制是否开启播放
    var isLooper = true


    fun setPostNodeView(context: Context, item: PostBean, view: View, position: Int, isVideoType: Boolean,
        shareListener: ((bean: PostBean) -> Unit)?) {
        var position = position
        with(view) {
            if (!TextUtils.isEmpty(item.video_url)) {
                ivView9.visibility = View.GONE
                rl_video.visibility = View.VISIBLE
                var jzStdRv: MyJzVideoView? = null

                if (item.status == "0") {
                    rl_video.visibility=View.VISIBLE
                    ivVideoCover.visibility=View.VISIBLE
                    ivVideoCover.setImageResource(R.color.black)
                    flContainer.visibility = View.GONE
                    ivStart.visibility = View.GONE
                    tvStatusHint.visibility = View.VISIBLE
                    tv_total.visibility=View.GONE
                    tvStatusHint.text = context.getString(R.string.post_video_audit_tip)
                } else {

                    if (isVideoType) {

                        flContainer.visibility = View.VISIBLE
                        ivVideoCover.visibility = View.GONE
                        ivStart.visibility = View.GONE
                        tvStatusHint.visibility = View.GONE
                        tv_total.visibility=View.GONE
                        var index = if (AutoPlayUtils.pageType == 1 || AutoPlayUtils.pageType == 2) {
                            if (AutoPlayUtils.playerMap[AutoPlayUtils.index] != null) AutoPlayUtils.playerMap[AutoPlayUtils.index]!!
                            else -1
                        } else {
                            if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null) AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!!
                            else -1
                        }
                        if (Jzvd.CURRENT_JZVD != null && index == position) {
                            if (MyJzVideoView.CURRENT_JZVD == null) {
                                return
                            }
                            val parent: ViewParent = MyJzVideoView.CURRENT_JZVD.parent
                            if (parent != null) {
                                (parent as ViewGroup).removeView(MyJzVideoView.CURRENT_JZVD)
                            }
                            flContainer?.removeAllViews()
                            flContainer?.addView(MyJzVideoView.CURRENT_JZVD,
                                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT))
                            jzStdRv = MyJzVideoView.CURRENT_JZVD as MyJzVideoView?
                        } else {
                            if (flContainer.childCount == 0) {
                                jzStdRv = MyJzVideoView(context)
                                flContainer.addView(jzStdRv,
                                    FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT))
                            } else {
                                jzStdRv = flContainer.getChildAt(0) as MyJzVideoView?
                            }
                            Jzvd.SAVE_PROGRESS = true
                            jzStdRv?.setUp(item.video_url, "", 0)
                            if (!TextUtils.isEmpty(item.cover_url)) {
                                GlideUtil.loadImg(jzStdRv?.posterImageView, item.cover_url)
                            } else {
                                if (jzStdRv?.posterImageView != null) {
                                    GlideUtil.loadVideoScreenshot(item.video_url, jzStdRv.posterImageView, 0)
                                }
                            }


                            if (AutoPlayUtils.pageType == 1 || AutoPlayUtils.pageType == 2) {
                                if (AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] != null) {
                                    index = AutoPlayUtils.homePlayerMap[AutoPlayUtils.index]!!
                                }

                            } else {
                                if (AutoPlayUtils.playerMap[AutoPlayUtils.index] != null) {
                                    index = AutoPlayUtils.playerMap[AutoPlayUtils.index]!!
                                }
                            }

                            if ((index == -1 || index == position) && isLooper) {
                                isLooper = !isLooper
                                Thread(Runnable {
                                    Thread.sleep(30)
                                    jzStdRv?.post {
                                        jzStdRv.startButton.performClick()
                                    }
                                }).start()
                            }
                        }




                        jzStdRv?.id = R.id.jzVideoView
                        jzStdRv?.mTvTotal?.text = JZUtils.stringForTime(item.video_length.toLong()*1000)
                        jzStdRv?.mTvTotal?.visibility=View.VISIBLE
                        jzStdRv?.setClickUi(object : MyJzVideoView.ClickUi {
                            override fun onClickUiToggle() {
                                startVideoActivity(context,item)
                                jzStdRv.setClickUi(null)
                            }

                            override fun onClickStart() {

                                if (AutoPlayUtils.pageType == 1 || AutoPlayUtils.pageType == 2) {
                                    AutoPlayUtils.homePlayerMap[AutoPlayUtils.index] = position
                                } else {
                                    AutoPlayUtils.playerMap[AutoPlayUtils.index] = position
                                }

                            }

                            override fun onShareClick() {
                                shareListener?.invoke(item)
                            }

                        })

                    } else {
                        flContainer.visibility = View.GONE
                        ivVideoCover.visibility = View.VISIBLE
                        tvStatusHint.visibility = View.GONE
                        ivStart.visibility = View.VISIBLE
                        if(item.video_length==0L){
                            tv_total.visibility=View.GONE
                        }else{
                            tv_total.visibility=View.VISIBLE
                            tv_total.text= JZUtils.stringForTime(item.video_length.toLong()*1000)

                        }

                        ivVideoCover.setOnClickListener {
                            startVideoActivity(context,item)
                        }

                        if (!TextUtils.isEmpty(item.cover_url)) {
                            GlideUtil.loadImg(ivVideoCover, item.cover_url)
                        } else {
                            GlideUtil.loadVideoScreenshot(item.video_url, ivVideoCover, 0)
                        }
                    }

                }

            } else {
                rl_video.visibility = View.GONE
                ivView9.visibility = View.VISIBLE
            }
            if (!TextUtils.isEmpty(item.title)) {
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = item.title
            } else {
                tvTitle.visibility = View.GONE
            }
            if (!TextUtils.isEmpty(item.user_id)) {
                avUser.setAvatar(item.avatar, item.master_type).setUserId(item.user_id)
                tvAttention.setMutual(item.is_mutual, item.user_id)
            }
            tvName.setNickname(item.nickname)
            tvName.setMedalIcon(item.medal_image)
            tvTime.text = TimeZoneUtil.getShortTimeShowString(item.create_time)
            tvRead.text = StringUtils.sizeToString(item.comment_num)
            viewLike.setLike(item.is_like, item.like_num)
            tvInvite.visibility = View.GONE
            tvName1.visibility = View.VISIBLE
            isNodeVisible(item.answer_id, view)
            if (item.img_url != null && item.img_url.size > 0) {
                ivView9.visibility = View.VISIBLE
                ivView9.setImages(item.img_url)
            } else {
                ivView9.visibility = View.GONE
            }
            if (item.total_hots > 999) {
                ivEssence.visibility = View.VISIBLE
            } else {
                ivEssence.visibility = View.GONE
            }
            val tabs = ArrayList<String>()
            if (item.circle_list != null && item.circle_list.size > 0) {
                for (i in item.circle_list) {
                    if(item.circle_id!=i.circle_id){
                        tabs.add("#${i.name}")
                    }

                }
            }

            var circleName = ""
            if (!TextUtils.isEmpty(item.circle_name)) {
                circleName = "${item.circle_name}"
            }
            circleName += if (!TextUtils.isEmpty(item.modular_name)) {
                if ("综合" == item.modular_name) {
                    ""
                } else {
                    if (!TextUtils.isEmpty(circleName)) {
                        "-${item.modular_name}"
                    } else {
                        "${item.modular_name}"
                    }

                }
            } else {
                ""
            }
            tvName1.text = circleName
            if (TextUtils.isEmpty(circleName)) {
                tvName1.visibility = View.INVISIBLE
            }

            val builder =
                ShowAllTextView.Builder().setText(item.content).setLabelList(tabs).setOnClickListener { _, index, _ ->
                    CirclesDetailsActivity.actionStart(context, item.circle_list[index].circle_id,"","")
                }
            when (item.answer_id) {
                -1 -> {
                    builder.setType(ShowAllTextView.TEXT)
                }
                0 -> {
                    tvInvite.setOnClickListener {
                        SelectFriendActivity.actionStart(context, item.id)
                    }
                    builder.setImgLabel("待解答").setType(ShowAllTextView.IMAGE_TEXT).isResolved(false)
                }
                else -> {
                    var participateNum = "${item.participate_num}人参与回答"
                    builder.setImgLabel("已解答").setType(ShowAllTextView.IMAGE_TEXT).isResolved(true)
                    tvAnswerName.text = participateNum

                }
            }
            tvDesc.create(builder)

        }
    }

    fun startVideoActivity(context: Context,item:PostBean){
        var share_url = "${item.share_url}?post_id=${item.id}"
        var share_title = "来自「${item.nickname}」"
        var share_desc = item.content
        var share_image=""
        if (item.img_url != null && item.img_url.size > 0) {
            share_image = item.img_url[0]
        }
        var shareEvent= ShareEvent(share_url,share_title,share_desc,share_image)
        VideoActivity.actionStart(context!!, item.video_url, 1,shareEvent)
    }

    /**
     * 是否是问答帖
     * @param isAnswerType int -1综合 0-待解答 else 已解答
     */
    private fun isNodeVisible(isAnswerType: Int, view: View) {
        with(view) {
            if (-1 == isAnswerType) {
                tvShare.visibility = View.VISIBLE
                tvRead.visibility = View.VISIBLE
                viewLike.visibility = View.VISIBLE
                tvInvite.visibility = View.GONE
                tvAnswerName.visibility = View.GONE
            } else {
                tvShare.visibility = View.GONE
                tvRead.visibility = View.GONE
                viewLike.visibility = View.GONE
                if (0 == isAnswerType) { //待解答
                    tvInvite.visibility = View.VISIBLE
                    tvAnswerName.visibility = View.GONE
                } else {
                    tvInvite.visibility = View.GONE
                    tvAnswerName.visibility = View.VISIBLE
                }
            }
        }

    }
}