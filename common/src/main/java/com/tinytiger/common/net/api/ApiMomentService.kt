
package com.tinytiger.common.net.api



import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.CollectBean
import com.tinytiger.common.net.data.circle.CircleInfoBean
import com.tinytiger.common.net.data.circle.CircleMeBean
import com.tinytiger.common.net.data.circle.CircleRecommendedBean
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionBean
import com.tinytiger.common.net.data.circle.home.CircleAttentionUserBean
import com.tinytiger.common.net.data.circle.post.PostDetailsBean
import com.tinytiger.common.net.data.circle.post.PostListBean
import com.tinytiger.common.net.data.circle.postsend.*
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.msg.ReplyAdoptionBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 3:01
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc api 圈子
 */
interface ApiMomentService {

    /**
     * *******************首页*******************************************************************
     */

    @GET("User/focusList?")
    fun focusList(@Query("page") page: Int): Observable<CircleAttentionBean>

    @GET("User/recommendedUserList?")
    fun recommendedUserList(@Query("page") page: Int,@Query("per_page") per_page: Int): Observable<CircleAttentionUserBean>


    @GET("circle/myCircleList?")
    fun myCircleList(@Query("page") page: Int): Observable<CircleMeBean>

    @GET("Circle/recommendedCircleList?")
    fun recommendedCircleList(): Observable<CircleRecommendedBean>

    @GET("Post/recommendedPost?")
    fun recommendedPost(@Query("page") page: Int): Observable<CircleAttentionBean>

    /**
     * *******************发帖*******************************************************************
     */
    @GET("Post/indexCircleByCate?")
    fun indexCircleByCate(@Query("keywords") keywords: String, @Query("page") page: Int): Observable<SelectCircler2Bean>

    @GET("Post/indexCircleByCate?")
    fun indexCircleByCate(@Query("page") page: Int): Observable<SelectCircler1Bean>



    @GET("Post/indexCircleByModular?")
    fun indexCircleByModular(@Query("keywords") keywords: String,@Query("page") page: Int,@Query("per_page") per_page: Int): Observable<SelectCirclerName2Bean>


    @GET("Post/indexCircleByModular?")
    fun indexCircleByModular(@Query("circle_id") circle_id: String, @Query("page") page: Int): Observable<SelectCirclerNameBean>


    @GET("Qa/getUserList?")
    fun getUserList(@Query("post_id") post_id: String): Observable<SelectFriendBean>

    @GET("Qa/getFollowUserList?")
    fun getFollowUserList(@Query("page") page: Int): Observable<SelectFriend2Bean>


    @FormUrlEncoded
    @POST("Post/addPost?")
    fun addPost(@Field("type") type: Int,
                @Field("is_draft") is_draft: Int,
                @Field("post_draft_id") post_id: Int,
                @Field("user_ids") user_ids: String,
                @Field("circle_ids") circle_ids: String,
                @Field("circle_names") circle_names: String,
                @Field("img_url") img_url: String,
                @Field("circle_id") circle_id: String,
                @Field("modular_id") modular_id: String,
                @Field("content") content: String,
                @Field("cover_url") cover_url: String,
                @Field("video_url") video_url: String,
                @Field("video_length") video_length: String,
                @Field("post_title") post_title: String
                ): Observable<DraftDetailBean>


    @GET("Post/indexDrafts?")
    fun indexDrafts(@Query("page") page: Int): Observable<DraftListBean>


    @GET("Post/delPost?")
    fun delPost(@Query("post_ids") post_ids: String,@Query("is_draft") is_draft: String): Observable<BaseBean>


    @GET("Post/draftDetail?")
    fun draftDetail(@Query("post_draft_id") post_draft_id: String): Observable<DraftDetailBean>


    @FormUrlEncoded
    @POST("circle/joinCircle?")
    fun joinCircle(@Field("circle_id") circle_id: String): Observable<BaseBean>

    @GET("circle/allCircleList?")
    fun allCircleList(@Query("page") page: Int): Observable<CircleMeBean>


    @GET("Post/getPostInfo?")
    fun getPostInfo(@Query("post_id") post_id: String): Observable<PostDetailsBean>

    @GET("PostComment/allComments?")
    fun allComments(@Query("post_id") post_id: String,@Query("comment_type") comment_type: Int,@Query("page") page: Int): Observable<PostListBean>


    @GET("PostComment/likePostComment?")
    fun likePostComment(@Query("comment_id") comment_id: String): Observable<BaseBean>

    @GET("Post/likePost?")
    fun likePost(@Query("post_id") post_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("PostComment/addComment?")
    fun addComment(@Field("post_id") post_id: String,@Field("comment_id") comment_id: String?,@Field("content") content: String): Observable<AddCommentBean>

    @GET("PostComment/delComment?")
    fun delComment(@Query("comment_id") comment_id: String): Observable<BaseBean>

    @GET("Post/replyAdoption?")
    fun replyAdoption(@Query("post_id") post_id: String,@Query("comment_id") comment_id: String): Observable<ReplyAdoptionBean>


    @GET("PostComment/subCommentList?")
    fun subCommentList(@Query("comment_id") comment_id: String,@Query("comment_type") comment_type: Int,@Query("page") page: Int): Observable<PostListBean>


    @GET("Post/collectPost?")
    fun collectPost(@Query("post_id") post_id: String): Observable<CollectBean>


    @GET("Post/inviteUser?")
    fun inviteUser(@Query("post_id") post_id: String,@Query("user_ids") user_ids: String): Observable<BaseBean>


    /**
     * *******************圈子详情*******************************************************************
     */

    @FormUrlEncoded
    @POST("circle/getGameCircleInfo?")
    fun getGameCircleInfo(@Field("game_id") game_id: String,@Field("circle_id") circle_id: String): Observable<CircleInfoBean>


    @FormUrlEncoded
    @POST("circle/getCircleTagModularPostList?")
    fun getCircleTagModularPostList(@Field("circle_id") circle_id: String,
                                    @Field("tag_modular_id") tag_modular_id: String,
                                    @Field("fixed_modular_type") fixed_modular_type: String,
                                    @Field("page") page: Int): Observable<PostInfoBean>



    @GET("Post/searchPost?")
    fun searchPost(@Query("title") search_val : String,@Query("page") page: Int): Observable<PostInfoBean>

    @GET("Post/searchPost?")
    fun searchPost(@Query("title") search_val : String,@Query("page") page: Int,@Query("circle_id") circle_id : String): Observable<PostInfoBean>



    @GET("circle/searchCircle?")
    fun searchCircle(@Query("title") search_val : String,@Query("page") page: Int): Observable<CircleMeBean>


}