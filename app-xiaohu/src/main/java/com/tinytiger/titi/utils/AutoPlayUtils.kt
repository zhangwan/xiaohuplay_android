package com.tinytiger.titi.utils

import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.Jzvd
import com.j256.ormlite.stmt.query.In
import com.tinytiger.common.net.data.circle.PostBean
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.video.MyJzVideoView

/**
 * @author zwy
 * create at 2020/6/13 0013
 * description: 列表自动播放的工具类
 */
object AutoPlayUtils {

    var index = 0
    //key是位置，value是进度
    var playerMap = HashMap<Int, Int>()
    var homePlayerMap = HashMap<Int, Int>()
    //代表的页面1 推荐主页关注 2推荐主页圈子 3 圈子详情页
    var pageType = 1

    /**
     *
     * @param recyclerView RecyclerView?
     * @param dataList ArrayList<PostBean>
     * @param looperFlag Int 1等于上滑 2下滑
     * @param count Int 最后有前面的item
     */
    fun onScrollPlayVideo(recyclerView: RecyclerView?, dataList: ArrayList<PostBean>, looperFlag: Int, count: Int) {
        val layoutManager = recyclerView?.layoutManager as LinearLayoutManager?
        var firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
        var lastVisiblePosition = layoutManager!!.findLastVisibleItemPosition()
        if (looperFlag == 1) { //上滑
            for (i in lastVisiblePosition downTo firstVisiblePosition) {

                val parentView = layoutManager!!.findViewByPosition(i)
                try {
                    var view = parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)
                    var indexPosition = if (pageType == 1 || pageType == 2) { //圈子主页
                        if (homePlayerMap[index] != null) homePlayerMap[index]!! else -1
                    } else {
                        if (playerMap[index] != null) playerMap[index]!! else -1
                    }
                    var lastParentView = layoutManager!!.findViewByPosition(indexPosition)
                    var lastView = View(recyclerView?.context)
                    if (lastParentView != null) {
                        lastView = lastParentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)
                        if (getViewVisiblePercent(lastView) < 0.5) {
                            Jzvd.releaseAllVideos()
                        }
                    }

                    if (!TextUtils.isEmpty(dataList[i - count].video_url) && getViewVisiblePercent(
                            view) == 1f && (getViewVisiblePercent(
                            lastView) < 0.5 || Jzvd.CURRENT_JZVD == null || indexPosition == -1)
                    ) {

                        if (pageType == 1 || pageType == 2) { //圈子主页
                            homePlayerMap[index] = i
                            view.startVideo()
                        } else {
                            playerMap[index] = i
                            view.startVideo()
                        }
                        break
                    }
                } catch (e: Exception) {

                }
            }
        } else {
            for (i in firstVisiblePosition..lastVisiblePosition) {

                try {
                    val parentView = layoutManager!!.findViewByPosition(i)
                    var view = parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)
                    var indexPosition = if (pageType == 1 || pageType == 2) { //圈子主页
                        if (homePlayerMap[index] != null) homePlayerMap[index]!! else -1
                    } else {
                        if (playerMap[index] != null) playerMap[index]!! else -1
                    }
                    var lastParentView = layoutManager!!.findViewByPosition(indexPosition)
                    var lastView = View(recyclerView?.context)
                    if (lastParentView != null) {
                        lastView = lastParentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)
                    }
                    var postion = i
                    if (pageType == 2) {
                        if (i - count > 0) {
                            postion = i - count
                        }
                    }

                    if (!TextUtils.isEmpty(dataList[postion].video_url) && getViewVisiblePercent(
                            view) == 1f && (getViewVisiblePercent(lastView) < 0.5 || Jzvd.CURRENT_JZVD == null)
                    ) {
                        if (pageType == 1 || pageType == 2) { //圈子主页
                            homePlayerMap[index] = i
                            view.startButton.performClick()
                        } else {
                            playerMap[index] = i
                            view.startButton.performClick()
                        }
                        break
                    }
                } catch (e: Exception) {

                }
            }
        }

    }

    /**
     *
     *
     * @param layoutManager recyclerView LinearLayoutManger
     * @param percent Float 被遮挡的比例
     */
    fun onScrollReleaseAllVideos(layoutManager: LinearLayoutManager?, percent: Float) {
        if (Jzvd.CURRENT_JZVD == null) return
        var positionInList = if (pageType == 1 || pageType == 2) { //圈子主页
            if (homePlayerMap[index] != null) homePlayerMap[index]!! else 0
        } else {
            if (playerMap[index] != null) playerMap[index]!! else 0
        }
        if (positionInList >= 0) {
            if (layoutManager == null) {
                Jzvd.releaseAllVideos()
                return
            }
            var parentView: View? = layoutManager!!.findViewByPosition(positionInList)
            if (parentView == null) {
                Jzvd.releaseAllVideos()
                return
            }
            var view = parentView!!.findViewById<MyJzVideoView>(R.id.jzVideoView)

            if (pageType == 1 || pageType == 2) { //圈子主页
                if (getViewVisiblePercent(view) <= percent) {
                    Jzvd.releaseAllVideos()
                }
            } else {

                if (getViewGlobalPercent(view) <= percent) {
                    Jzvd.releaseAllVideos()
                }
            }

        }


    }

    /**
     * @param view
     * @return 当前视图可见比列
     */
    fun getViewVisiblePercent(view: View?): Float {
        if (view == null) {
            return 0f
        }
        val height = view.height.toFloat()
        val rect = Rect()
        if (!view.getLocalVisibleRect(rect)) {
            return 0f
        }
        val visibleHeight = rect.bottom - rect.top.toFloat()
        return visibleHeight / height
    }

    /**
     * 播放暂停
     */
    fun getViewGlobalPercent(view: View?): Float {
        if (view == null) {
            return 0f
        }
        val height = view.height.toFloat()
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        if (rect.bottom - height < 0 || rect.bottom - height < 30) {
            return 0.5f
        }

        return 1f
    }

}