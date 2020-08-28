package com.tinytiger.common.net.scheduler


/**
 *
 * @author zhw_luke
 * @date 2019/10/15 0015 下午 4:04
 * @doc 线程调度工具类
 */
object SchedulerUtils {

    /**
     * io主线程
     */
    fun <T> ioToMain(): IoMainScheduler<T> {
        return IoMainScheduler()
    }
}
