package com.app.mowanxing.wlanserverfilemanager.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class AvailableServerIpViewModel @Inject constructor() : ViewModel() {
    private val _availableIps = MutableStateFlow<List<String>>(emptyList())
    private val _loading = mutableStateOf(false)
    fun scanAvailableServerIp() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true // 开始加载时设置加载状态

            val baseIp = "192.168.0." // 假设网段是这个，你可以改成动态获取
            val port = 80
            val path = "/ping.php"
            val reachableIps = mutableListOf<String>()

            for (i in 99..105) {
                val testUrl = "http://$baseIp$i:$port$path"
                try {
                    val connection = URL(testUrl).openConnection() as HttpURLConnection
                    connection.connectTimeout = 200
                    connection.readTimeout = 200
                    connection.requestMethod = "GET"
                    if (connection.responseCode == 200) {
                        reachableIps.add(baseIp + i)
                    }
                } catch (_: Exception) { }
            }

            if (reachableIps.isEmpty()) {
                reachableIps.add("无可用服务器IP\n请检查网络连接状态、服务器状态、客户端代码")
            }

            _availableIps.value = reachableIps
            _loading.value = false // 数据加载完成后，取消正在加载状态

            Log.d("AvailableServerIpViewModel.kt",_availableIps.asStateFlow().value.toString())
        }
    }

    fun clearAlreadyExistAvailableServerIpData() {
        _availableIps.value = emptyList()
    }
    val availableServerIp = _availableIps.asStateFlow()
    val availableServerIpLoading: State<Boolean> = _loading
}