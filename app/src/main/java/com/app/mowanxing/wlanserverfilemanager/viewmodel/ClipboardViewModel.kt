package com.app.mowanxing.wlanserverfilemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.app.mowanxing.wlanserverfilemanager.network.ClipboardItemForGet
import com.app.mowanxing.wlanserverfilemanager.network.ClipboardApiRetrofitClient.clipboardApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClipboardViewModel: ViewModel() {
    /**
     * 拉取请求数据
     */
    private val _list = mutableStateOf<List<ClipboardItemForGet>>(emptyList())
    private val _loading = mutableStateOf(false)
    fun loadClipboardData() {
        viewModelScope.launch {
            _loading.value = true // 开始加载时设置加载状态
            try {
                val result = clipboardApi.getClipboardRecords() // 向数据源请求数据
                _list.value = result // 将请求到的数据注入_list
            } catch (e: Exception) {
                e.printStackTrace()
                _list.value = emptyList() // 请求失败时设置空列表
            } finally {
                _loading.value = false // 数据加载完成后，取消正在加载状态
            }
        }
    }
    val clipboardList: State<List<ClipboardItemForGet>> = _list
    val clipboardLoadingState: State<Boolean> = _loading

    /**
     * 提交数据
     */
    private val _messageFlow = MutableStateFlow<String?>(null)
    fun submitClipboardData(content: String, deviceId: String) {
        viewModelScope.launch {
            try {
                val response = clipboardApi.submitClipboard(content = content, device = deviceId)
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("ClipboardViewModel", "状态: ${body?.status}, 消息: ${body?.message}")
                    if (body?.status == "success") {
                        _messageFlow.value = "内容提交成功"
                    } else {
                        _messageFlow.value = "内容提交失败，原因：${body?.message}"
                    }
                } else {
                    Log.e("ClipboardViewModel", "HTTP错误: ${response.code()}")
                    _messageFlow.value = "HTTP错误：${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("ClipboardViewModel", "提交失败", e)
                _messageFlow.value = "网络错误：${e.localizedMessage}"
            }
        }
    }
    fun clearMessage() {
        _messageFlow.value = null
    }

    // 将服务端返回的消息传递给View
    val serverMessageFlow: StateFlow<String?> = _messageFlow
}