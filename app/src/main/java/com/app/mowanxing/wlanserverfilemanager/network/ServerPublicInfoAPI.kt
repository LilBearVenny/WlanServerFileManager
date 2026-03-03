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
data class PublicInfoItemForGet(
    val info_id: Int,
    val info_created_at: String,
    val info_title: String,
    val info_content: String
)
/**
 * PHP接口
 */
interface PublicInfoApiService {
    @GET("get_public_info.php") // PHP接口
    suspend fun getPublicInfo(): List<PublicInfoItemForGet>
}
