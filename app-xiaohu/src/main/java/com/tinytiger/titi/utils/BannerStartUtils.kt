package com.tinytiger.titi.utils



import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.net.data.gametools.wiki.WikiChildBean
import com.tinytiger.common.net.data.home2.BannerAdBean
import com.tinytiger.common.net.data.home2.SkipChangeInterface
import com.tinytiger.common.utils.AppManager
import com.tinytiger.common.utils.ConstantsUtils
import com.tinytiger.common.utils.umeng.BannerAgentUtils
import com.tinytiger.titi.ui.circle.PostActivity
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity
import com.tinytiger.titi.ui.news.NewsDetailActivity
import com.tinytiger.titi.ui.video.VideoDetailActivity
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2020/4/17 0017 下午 12:06
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc banner点击跳转
 */
object BannerStartUtils {

    /**
     * 新版banner跳转
     */
    fun setStartIntent(mAdBean: SkipChangeInterface,tag:Int) {
        //  TCAgent.onEvent(AppManager.getAppManager().currentActivity(), "banner")
        // #事件类型:0无事件1外部url跳转 2内部url跳转 3跳转文章/视频 4跳转游戏 5跳转百科 6 跳转词条  7跳转帖子
        when (mAdBean.getJumpTypes()) {
            1 -> {
                if (mAdBean.getJumpUrl() != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 1, mAdBean.getJumpUrl()))
                }
            }
            2 -> {
                if (mAdBean.getJumpUrl() != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 0, mAdBean.getJumpUrl()))
                }
            }
            3 -> {
                when (mAdBean.getContentType()) {
                    "1" -> {
                        NewsDetailActivity.actionStart(
                            AppManager.getAppManager().currentActivity(),
                            "" + mAdBean.getJumpView()
                        )
                    }
                    "2" -> {
                        VideoDetailActivity.actionStart(
                            AppManager.getAppManager().currentActivity(),
                            "" + mAdBean.getJumpView(),
                            ""
                        )
                    }
                }
            }
            4 -> {
                GameDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.getJumpView(),1)
            }
            5 -> {
                CirclesDetailsActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.getJumpView(),
                    ConstantsUtils.Page.PAGE_WIKI)
            }
            6 -> {
                GameWikiDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.getJumpView())
            }
            7 -> {
                PostActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.getJumpView())
            }
        }
        if (tag==0){
            BannerAgentUtils.setBannerHomeClick(mAdBean.getIds()?:"")
        }else{
            // BannerAgentUtils.setBannerWikiClick(mAdBean.id)
        }
    }

    fun setStartIntent(mAdBean: AdBean,tag:Int) {
      //  TCAgent.onEvent(AppManager.getAppManager().currentActivity(), "banner")
       // #事件类型:0无事件1外部url跳转 2内部url跳转 3跳转文章/视频 4跳转游戏 5跳转百科 6 跳转词条  7跳转帖子
        when (mAdBean.jump_type) {
            1 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 1, mAdBean.jump_url))
                }
            }
            2 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 0, mAdBean.jump_url))
                }
            }
            3 -> {
                when (mAdBean.content_type) {
                    "1" -> {
                        var jumpView=mAdBean.jump_view
                        NewsDetailActivity.actionStart(
                            AppManager.getAppManager().currentActivity(),
                            jumpView
                        )
                    }
                    "2" -> {
                        var jumpView=mAdBean.jump_view
                        VideoDetailActivity.actionStart(
                            AppManager.getAppManager().currentActivity(),
                            "" + mAdBean.jump_view,
                            ""
                        )
                    }
                }
            }
            4 -> {
                GameDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_view,1)
            }
            5 -> {
                CirclesDetailsActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_view,
                    ConstantsUtils.Page.PAGE_WIKI)
            }
            6 -> {
                GameWikiDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_view)
            }
            7 -> {
                PostActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_view)
            }
        }
        if (tag==0){
            BannerAgentUtils.setBannerHomeClick(mAdBean.id)
        }else if (tag==3){
            BannerAgentUtils.setBannerFindClick(mAdBean.id)
        }else{
           // BannerAgentUtils.setBannerWikiClick(mAdBean.id)
        }
    }

    //"jump_type": 0, #跳转方式  0=无跳转 1=外部跳转 2=功能页面跳转 3=游戏跳转 4=游戏百科跳转 5=游戏词条 6=文章 7=视频
    //"jump_url": "" #跳转目标 jump_type=1时为外部链接,=2时为功能页面标识,=3|4时为游戏id,=5时为词条id,=6时为文章id,=7时为视频id

    fun setStartIntent2(mAdBean: WikiChildBean) {
     //   TCAgent.onEvent(AppManager.getAppManager().currentActivity(), "banner")
        when (mAdBean.jump_type) {
            1 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 1, mAdBean.jump_url))
                }
            }
            2 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 0, mAdBean.jump_url))
                }
            }
            3 -> {
                GameDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url,1)
            }
            4 -> {
                CirclesDetailsActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url,
                    ConstantsUtils.Page.PAGE_WIKI)
            }
            5 -> {
                GameWikiDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url)
            }
            6 -> {
                NewsDetailActivity.actionStart(
                    AppManager.getAppManager().currentActivity(),
                    "" + mAdBean.jump_url
                )
            }
            7 -> {
                VideoDetailActivity.actionStart(
                    AppManager.getAppManager().currentActivity(),
                    "" + mAdBean.jump_url,
                    ""
                )
            }
            8 -> {
                PostActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url)
            }
        }
    }

    /**
     * 第三版banner跳转
     */
    fun setStartIntent3(mAdBean: AdBean) {
       // TCAgent.onEvent(AppManager.getAppManager().currentActivity(), "banner")
        when (mAdBean.jump_type) {
            1 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 1, mAdBean.jump_url))
                }
            }
            2 -> {
                if (mAdBean.jump_url != null) {
                    EventBus.getDefault().post(ClassEvent("WebActivity", 0, mAdBean.jump_url))
                }
            }
            3 -> {
                GameDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url,1)
            }
            4 -> {
                CirclesDetailsActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url,
                    ConstantsUtils.Page.PAGE_WIKI)
            }
            5 -> {
                GameWikiDetailActivity.actionStart( AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url)
            }
            6 -> {
                NewsDetailActivity.actionStart(
                    AppManager.getAppManager().currentActivity(),
                    "" + mAdBean.jump_url
                )
            }
            7 -> {
                VideoDetailActivity.actionStart(
                    AppManager.getAppManager().currentActivity(),
                    "" + mAdBean.jump_url,
                    ""
                )
            }
            8 -> {
                PostActivity.actionStart(AppManager.getAppManager().currentActivity(),""+mAdBean.jump_url)
            }
        }
    }

}