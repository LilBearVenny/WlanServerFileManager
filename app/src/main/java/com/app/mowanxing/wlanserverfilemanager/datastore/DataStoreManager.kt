package com.app.mowanxing.wlanserverfilemanager.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// 创建 DataStore 单例
val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    companion object {
        private val SERVER_IP_KEY = stringPreferencesKey("server_ip")
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    }

    // 写入服务器IP
    suspend fun _saveServerIp(ip: String) {
        context.dataStore.edit { it[SERVER_IP_KEY] = ip }
    }

    // 读取服务器IP
    val _readServerIP: Flow<String?> = context.dataStore.data.map { it[SERVER_IP_KEY] }

    // 写入本机标识符
    suspend fun _saveDeviceId(id: String) {
        context.dataStore.edit { it[DEVICE_ID_KEY] = id }
    }

    // 读取本机标识符
    val _readDeviceId: Flow<String?> = context.dataStore.data.map { it[DEVICE_ID_KEY] }
}
