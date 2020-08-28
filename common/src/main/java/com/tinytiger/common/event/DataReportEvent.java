package com.tinytiger.common.event;


/**
 * @author zhw_luke
 * @date 2018/9/19 0019 下午 1:52
 * @doc 数据上报通知
 */
public class DataReportEvent {

    /**
     * 内容浏览记录的id
     */
    public String view_log_id;

    /**
     * 页面停留
     */
    public int view_time;
    /**
     * 视频观看时长
     */
    public int view_video_time;

    public DataReportEvent(String view_log_id, int view_time) {
        this.view_log_id = view_log_id;
        this.view_time = view_time;
    }

    public DataReportEvent(String view_log_id, int view_time, int view_video_time) {
        this.view_log_id = view_log_id;
        this.view_time = view_time;
        this.view_video_time = view_video_time;
    }
}
