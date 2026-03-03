package com.app.mowanxing.wlanserverfilemanager.network

import android.util.Log
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.app.mowanxing.wlanserverfilemanager.viewmodel.LocalDataViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ServerFilesInfoForGet(
    val file_name: String,
    val file_size: Double,
    val file_size_unit: String,
    val file_modified_time: String,
    val file_created_time: String,
    val file_type: String
)

interface ServerFilesInfoApiService {
    @GET("this_dir_files_info_list.php") // PHP接口
    suspend fun getServerFilesInfo(): List<ServerFilesInfoForGet>
}

object ServerFilesApiRetrofitClient {
    @Volatile
    private var retrofit: Retrofit? = null

    fun setBaseUrl(ip: String) {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("ServerFilesInfoAPI.kt", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY // 打印完整内容
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://$ip/upload_files/uploads/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val serverFilesInfoApi: ServerFilesInfoApiService
        get() {
            val instance = retrofit ?: throw IllegalStateException(
                "ServerFilesApiRetrofitClient not initialized, call setBaseUrl() first."
            )
            Log.d("ServerFilesInfoAPI.kt", retrofit?.baseUrl().toString())
            return instance.create(ServerFilesInfoApiService::class.java)
        }
}