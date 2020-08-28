package com.tinytiger.titi.mvp.model


import com.tinytiger.common.net.RetrofitManager

import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.InitBean
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.titi.R


import io.reactivex.Observable


class MainModel : BaseModel() {

    fun startApp():Observable<InitBean>{
        return RetrofitManager.service.startApp()
                .compose(SchedulerUtils.ioToMain())
    }


    fun changeViewLength(view_log_id:String,view_time:Int,view_video_time:Int):Observable<BaseBean>{
        return RetrofitManager.service.changeViewLength(view_log_id,view_time,view_video_time)
            .compose(SchedulerUtils.ioToMain())
    }
    //位置id (选填) 1开屏页 2 首页banner 3弹窗广告
    fun clickAdRecord(ad_id: String,position_id:Int):Observable<BaseBean>{
        val user_id= SpUtils.getString(R.string.user_id,"0")
        return RetrofitManager.service.clickAdRecord(ad_id,user_id,""+position_id)
            .compose(SchedulerUtils.ioToMain())
    }
    /**
     * 分享数据上报
     */
    fun addShare(class_name:String,content_id:String,share_channel_id:String):Observable<BaseBean>{
        //id (1QQ,2QQ空间,3微信,4微信朋友圈,5复制链接)
        var channel_id = 0
        when (share_channel_id) {
            "WEIXIN_CIRCLE" -> {
                channel_id = 4
            }
            "QQ" -> {
                channel_id = 1
            }
            "QQZONE" -> {
                channel_id = 2
            }
            "Copy" -> {
                channel_id = 5
            }
            else -> {
                channel_id = 3
            }
        }

      when(class_name){
          "GameAssess"->{
              return RetrofitManager.service.addShareGameAssess(content_id,channel_id)
                  .compose(SchedulerUtils.ioToMain())
          }
          "Circle"->{
              return RetrofitManager.service.addShare(content_id,channel_id)
                  .compose(SchedulerUtils.ioToMain())
          }
          "Post"->{
              return RetrofitManager.service.addSharePost(content_id,channel_id)
                  .compose(SchedulerUtils.ioToMain())
          }
          else->{
              return RetrofitManager.service.addShare(content_id,channel_id)
                  .compose(SchedulerUtils.ioToMain())
          }
      }
    }

}