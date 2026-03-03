package com.app.mowanxing.wlanserverfilemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mowanxing.wlanserverfilemanager.datastore.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalDataViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    val readServerIp = dataStoreManager._readServerIP.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun saveServerIp(ip: String) {
        viewModelScope.launch {
            dataStoreManager._saveServerIp(ip)
        }
    }

    val readDeviceId = dataStoreManager._readDeviceId.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun saveDeviceId(id: String) {
        viewModelScope.launch {
            dataStoreManager._saveDeviceId(id)
        }
    }
}