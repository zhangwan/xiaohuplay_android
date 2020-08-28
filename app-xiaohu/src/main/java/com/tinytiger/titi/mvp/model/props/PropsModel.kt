package com.tinytiger.titi.mvp.model.props


import com.tinytiger.common.net.RetrofitManager

import com.tinytiger.common.net.scheduler.SchedulerUtils
import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.props.PropsExchangeBean
import com.tinytiger.common.net.data.props.PropsInfoListBean
import com.tinytiger.common.net.data.props.PropsListBean
import com.tinytiger.common.net.data.props.PropsTabListBean


import io.reactivex.Observable


class PropsModel : BaseModel() {

    fun indexGoodsCate(need_group: Int):Observable<PropsTabListBean>{
        return RetrofitManager.service.indexGoodsCate(need_group)
                .compose(SchedulerUtils.ioToMain())
    }

    fun indexGoods(cate_id: String,page :Int):Observable<PropsListBean>{
        return RetrofitManager.service.indexGoods(cate_id,page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun searchGoodsTool(title: String,page :Int):Observable<PropsListBean>{
        return RetrofitManager.service.searchGoodsTool(title,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getMyMallprops(cate_id: String,page :Int):Observable<PropsListBean>{
        return RetrofitManager.service.getMyMallprops(cate_id,page)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getMyMallExchangeCode(page :Int):Observable<PropsListBean>{
        return RetrofitManager.service.getMyMallExchangeCode(page)
            .compose(SchedulerUtils.ioToMain())
    }


    fun wearProps(tool_id: String,cate_id: String):Observable<BaseBean>{
        return RetrofitManager.service.wearProps(tool_id,cate_id)
            .compose(SchedulerUtils.ioToMain())
    }

    fun getPropsInfo(tool_id :String):Observable<PropsInfoListBean>{
        return RetrofitManager.service.getPropsInfo(tool_id)
            .compose(SchedulerUtils.ioToMain())
    }


    fun buyTool(tool_id: String, num: Int):Observable<BaseBean>{
        return RetrofitManager.service.buyTool(tool_id,num)
            .compose(SchedulerUtils.ioToMain())
    }
    fun exchangeTools(tool_id: String, num: Int):Observable<PropsExchangeBean>{
        return RetrofitManager.service.exchangeTools(tool_id,num)
            .compose(SchedulerUtils.ioToMain())
    }
    fun getExchangeInfo(id: String):Observable<PropsExchangeBean>{
        return RetrofitManager.service.getExchangeInfo(id.toString())
            .compose(SchedulerUtils.ioToMain())
    }

}