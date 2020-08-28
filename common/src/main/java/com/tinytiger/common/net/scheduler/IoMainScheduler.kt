package com.tinytiger.common.net.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *
 * @author zhw_luke
 * @date 2019/10/15 0015 下午 4:04
 * @doc io线程调度
 */
class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())
