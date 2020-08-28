package com.tinytiger.common.net.data.yungaem

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.net.data.ShareInfo


class YunTimeResp : BaseResp<YunTimeBean>()

class YunTimeBean {
    var surplusTime=0//#剩余秒数
    var share_info: ShareInfo? = null
    var assess=0//#是否点评，0 未点评 1 已点评
}