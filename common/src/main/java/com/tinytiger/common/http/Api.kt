package com.tinytiger.common.http

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by zwy on 2020/6/5.
 */
interface Api {

    @GET("{path}")
    fun doGet(@Path(value = "path", encoded = true) url: String, @QueryMap map: Map<String, String?>): Observable<ResponseBody>

    @GET("{path}")
    fun doGet(@Path(value = "path", encoded = true) url: String): Observable<ResponseBody>

    @POST("{path}")
    fun doPost(@Path(value = "path", encoded = true) url: String, @QueryMap map: Map<String, String?>): Observable<ResponseBody>

    @POST("{path}")
    fun doPost(@Path(value = "path", encoded = true) url: String): Observable<ResponseBody>

    @POST("{path}")
    fun doBody(@Path(value = "path", encoded = true) url: String, @Body any: Any?): Observable<ResponseBody>

    @POST("{path}")
    fun doBody(@Path(value = "path", encoded = true) var1: String, @HeaderMap headers: Map<String, String?>, @Body body: Any?): Observable<ResponseBody>

    @POST("{path}")
    fun doQueryBody(@Path(value = "path", encoded = true) var1: String, @QueryMap headers: Map<String, String?>, @Body body: Any?): Observable<ResponseBody>

    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>

    @Multipart
    @Headers("CONNECT_TIMEOUT:60" , "READ_TIMEOUT:360", "WRITE_TIMEOUT:360")
    @POST("{path}")
    @JvmSuppressWildcards
    fun uploadFile(@Path(value = "path", encoded = true) var1: String,@PartMap  map :Map<String, RequestBody>): Observable<ResponseBody>


}