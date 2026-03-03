package com.app.mowanxing.wlanserverfilemanager.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mowanxing.wlanserverfilemanager.network.ClipboardItemForGet
import com.app.mowanxing.wlanserverfilemanager.network.ServerFilesApiRetrofitClient.serverFilesInfoApi
import com.app.mowanxing.wlanserverfilemanager.network.ServerFilesInfoForGet
import kotlinx.coroutines.launch

class ServerFilesInfoViewModel: ViewModel() {
    private val _list = mutableStateOf<List<ServerFilesInfoForGet>>(emptyList())
    private val _loading = mutableStateOf(false)

    fun loadServerFilesInfoData() {
        viewModelScope.launch{
            _loading.value = true // 开始加载时设置加载状态
            try {
                val result = serverFilesInfoApi.getServerFilesInfo() // 向数据源请求数据
                Log.d("ServerFilesInfoViewModel.kt", result.toString())
                _list.value = result // 将请求到的数据注入_list
            } catch (e: Exception) {
                print(e.printStackTrace())
                _list.value = emptyList() // 请求失败时设置空列表
            } finally {
                _loading.value = false // 数据加载完成后，取消正在加载状态
            }
        }

    }

    val serverFilesInfoList: State<List<ServerFilesInfoForGet>> = _list
    val serverFilesInfoLoadingState: State<Boolean> = _loading
}