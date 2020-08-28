package com.tinytiger.titi.mvp.model.props


import com.tinytiger.common.net.RetrofitManager

import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsExchangeListBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean


import io.reactivex.Observable


class PropsExchangeModel : BaseModel() {

    fun submitExchangePreview(exchange_code_json: String):Observable<PropsExchangeListBean>{
        return RetrofitManager.service.submitExchangePreview(exchange_code_json)
                .compose(SchedulerUtils.ioToMain())
    }

    fun submitExchange(exchange_code_json: String):Observable<BaseBean>{
        return RetrofitManager.service.submitExchange(exchange_code_json)
            .compose(SchedulerUtils.ioToMain())
    }
}