package com.tinytiger.common.net.api


import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.mine.talent.TalentBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 3:01
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc api 其他
 */
interface ApiOtherService {


    /**
     * app配置信息
     */

    @GET("Common/getQiniuToken?")
    fun getQiniuToken(): Observable<QiniuBean>


    @GET("User/masterApply?")
    fun masterApply(): Observable<TalentBean>


    @FormUrlEncoded
    @POST("User/submitMasterApply?")
    fun submitMasterApply(
        @Field("apply_type") apply_type: String,
        @Field("key") key: String,
        @Field("real_name") real_name: String,
        @Field("id_number") id_number: String,
        @Field("image_one") image_one: String,
        @Field("image_two") image_two: String,
        @Field("profile") profile: String
    ): Observable<BaseBean>

    @FormUrlEncoded
    @POST("User/submitMasterApply?")
    fun submitMasterApply1(
        @Field("apply_type") apply_type: Int,
        @Field("key") key: String,
        @Field("external_name") external_name: String,
        @Field("site_name") site_name: String,
        @Field("image_one") image_one: String,
        @Field("image_two") image_two: String,
        @Field("profile") profile: String
    ): Observable<BaseBean>

    /* apply_type 	int 申请类型 1站内达人认证 2站外达人认证
     key 	string /User/masterApply接口返回的key
     real_name 	string 真实姓名 apply_type = 1 必填
     id_number 	string 身份证号码 apply_type = 1 必填
     image_one 	string 身份证正面 apply_type = 1 必填 apply_type = 2 的时候二选一
     image_two 	string 身份证反面 apply_type = 1 必填 apply_type = 2 的时候二选一

     external_name 	string 外站昵称 apply_type = 2 必填
     site_name 	string 外站站名 apply_type = 2 必填
     profile 	string 达人简介（必填）*/

}