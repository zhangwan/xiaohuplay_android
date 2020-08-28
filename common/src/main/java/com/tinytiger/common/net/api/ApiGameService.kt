
package com.tinytiger.common.net.api



import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.center.UserCollectWikiList
import com.tinytiger.common.net.data.gametools.GameWikiEditBean
import com.tinytiger.common.net.data.gametools.GameWikiListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryDetailListBean
import com.tinytiger.common.net.data.gametools.category.GameCategoryListBean
import com.tinytiger.common.net.data.gametools.wiki.WikiDitailList
import com.tinytiger.common.net.data.gametools.wiki.WikiStatusList
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.PushIndexBean
import com.tinytiger.common.net.data.wiki.MainWikiListBean
import com.tinytiger.common.net.data.wiki.WikiSearchListBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 3:01
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc api 游戏
 */
interface ApiGameService {


    /**
     * app配置信息
     */

    @GET("Common/getQiniuToken?")
    fun getQiniuToken(): Observable<QiniuBean>


    /**
     * ======================================= 游戏百科 ================================================
     */
    @GET("GameWiki/index?")
    fun getGameWiki(): Observable<MainWikiListBean>


    @GET("GameWiki/search?")
    fun searchGameWiki(@Query("keyword") keyword: String,@Query("game_id") game_id: String, @Query("page") page: Int): Observable<WikiSearchListBean>


    @GET("GameWiki/getTemContentInfo?")
    fun getTemContentInfo(@Query("content_id") content_id: String): Observable<WikiDitailList>


    @GET("GameWiki/getWikiStatus?")
    fun getWikiStatus(@Query("game_id") game_id: String, @Query("page") page: Int): Observable<WikiStatusList>


    @GET("GameWiki/openWiki?")
    fun openWiki(@Query("game_id") game_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("GameWiki/wikiUpdateUserList?")
    fun getWikiUpdateUserList(@Field("content_id") content_id: String,@Field("page") page: Int,
                              @Field("per_page") per_page: Int): Observable<GameWikiEditBean>

    @FormUrlEncoded
    @POST("GameWiki/entryError?")
    fun commitEntryError(@Field("content_id") content_id: String,@Field("error_desc") error_desc: String,
                         @Field("images") images: String): Observable<BaseBean>


    /**
     * ======================================= 游戏分类 ================================================
     */
    @GET("GameCategory/index?")
    fun getGameCategory(): Observable<GameCategoryListBean>

    @GET("GameCategory/detail?")
    fun getGameCategoryDetailList(@Query("cate_id") cate_id: Int,
                                  @Query("type") type: Int,
                                  @Query("page") page: Int): Observable<GameCategoryDetailListBean>


    @GET("GameCategory/banner?")
    fun getGameCategoryBanner(@Query("cate_id") cate_id: Int): Observable<GameCategoryBannerBean>


    /**
     * ======================================= 安利文 ================================================
     */
    @GET("recommend/amwayWallRecommend?")
    fun amwayWallRecommend(@Query("source") keyword: String): Observable<AmwayWallListBean>


    @GET("Game/indexGame?")
    fun indexGame(): Observable<PushIndexBean>

    @FormUrlEncoded
    @POST("GameWiki/cancelCollectTemContent?")
    fun cancelCollectTemContent(@Field("content_id") content_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("GameWiki/collectTemContent?")
    fun collectTemContent(@Field("content_id") content_id: String): Observable<BaseBean>


    @GET("User/getUserTemContentCollectList?")
    fun getUserTemContentCollectList(@Query("page") page: Int): Observable<UserCollectWikiList>
}