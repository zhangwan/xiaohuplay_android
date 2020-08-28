package com.tinytiger.titi.ui.mine.me.circle.vp

import com.tinytiger.common.basis.BasisView
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.common.net.data.circle.CircleHistoryBean


interface CircleView : BasisView {

     fun setHistoryList(list: PageVo<CircleHistoryBean>?)
}