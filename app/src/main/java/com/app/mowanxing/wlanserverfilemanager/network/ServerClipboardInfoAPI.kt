package com.app.mowanxing.wlanserverfilemanager.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 数据类
 */
data class ClipboardItemForGet(
    val id: Int,
    val created_at: String,
    val device: String,
    val content: String
)
/**
 * 接收服务器返回的信息
 */
data class ClipboardItemForPostResponse(
    val status: String,
    val message: String
)
/**
 * Retrofit接口：定义请求PHP接口
 */
interface ClipboardApiService {
    @GET("get_clipboard.php") // PHP接口
    suspend fun getClipboardRecords(): List<ClipboardItemForGet>

    @FormUrlEncoded
    @POST("submit_clipboard.php")
    suspend fun submitClipboard(
        @Field("content") content: String,
        @Field("device") device: String
    ): Response<ClipboardItemForPostResponse>
}
/**
 * Retrofit封装：提供全局可用的api对象
 */
object ClipboardApiRetrofitClient {
    @Volatile
    private var retrofit: Retrofit? = null
    fun setBaseUrl(ip: String) {
        retrofit = Retrofit.Builder()
            .baseUrl("http://$ip/web_app_clipboard/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val clipboardApi: ClipboardApiService
        get() {
            val instance = retrofit ?: throw IllegalStateException("ClipboardApiRetrofitClient not initialized, call setBaseUrl() first.")
            return instance.create(ClipboardApiService::class.java)
        }
}