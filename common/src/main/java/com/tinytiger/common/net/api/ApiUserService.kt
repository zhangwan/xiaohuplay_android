
package com.tinytiger.common.net.api



import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.UserDynamicBean
import com.tinytiger.common.net.data.user.UserDynamicList
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 3:01
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc api 用户
 */
interface ApiUserService {


    @FormUrlEncoded
    @POST("Fans/doFollow?")
    fun doFollow(@Field("user_id") user_id: String): Observable<FollowUserBean>

    @FormUrlEncoded
    @POST("Fans/cancelFans?")
    fun cancelFans(@Field("user_id") user_id: String): Observable<FollowUserBean>


    @GET("UserHomepage/getUserDynamic?")
    fun getUserDynamic(@Query("user_id") user_id: String,@Query("page") page: Int): Observable<UserDynamicList>
}