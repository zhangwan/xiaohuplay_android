package com.tinytiger.titi.mvp.model.game

import com.tinytiger.common.base.BaseModel
import com.tinytiger.common.net.RetrofitManager
import com.tinytiger.common.net.data.gametools.GameLibListBean
import com.tinytiger.common.net.data.gametools.GameWikiEditBean
import com.tinytiger.common.net.data.gametools.GameWikiListBean
import com.tinytiger.common.net.scheduler.SchedulerUtils
import io.reactivex.Observable
import retrofit2.http.Field

class GameWikiEditModel : BaseModel() {

    fun getGameList(content_id: String, page: Int): Observable<GameWikiEditBean> {
        return RetrofitManager.serviceGame.getWikiUpdateUserList(content_id, page, 20)
            .compose(SchedulerUtils.ioToMain())
    }
}