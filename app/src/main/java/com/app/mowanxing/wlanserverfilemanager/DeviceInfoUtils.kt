package com.app.mowanxing.wlanserverfilemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs
import androidx.annotation.RequiresPermission
import java.net.InetAddress
import java.net.NetworkInterface


object DeviceInfoUtil {

    // 设备开机时长
    fun getDeviceUptime(context: Context): Long {
        val deviceUptime = SystemClock.elapsedRealtime() / 1000 / 60
        return deviceUptime
    }

    // Android 操作系统序号
    fun getDeviceSystemReleaseVersion(context: Context): String {
        val releaseVersion = Build.VERSION.RELEASE
        return releaseVersion
    }

    // Android 操作系统SDK序号
    fun getDeviceSystemSdkVersion(context: Context): Int {
        val sdkIntVersion = Build.VERSION.SDK_INT
        return sdkIntVersion
    }

    // Android ID
    @SuppressLint("HardwareIds")
    fun getDeviceAndroidID(context: Context): String {
        val deviceAndroidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        return deviceAndroidId
    }

    // 系统内核版本
    fun getDeviceSystemCoreVersion(context: Context): String? {
        val deviceSysCoreVer = System.getProperty("os.version")
        return deviceSysCoreVer
    }

    // 安全补丁安装时间
    fun getDeviceAndroidPatchTime(context: Context): String {
        val deviceAndroidPatch = Build.VERSION.SECURITY_PATCH
        return deviceAndroidPatch
    }

    // 设备所属品牌
    fun getDeviceBrandInfo(context: Context): String {
        val deviceBrand = Build.BRAND
        return deviceBrand
    }

    // 设备制造商
    fun getDeviceManufacturerInfo(context: Context): String {
        val deviceManufacturer = Build.MANUFACTURER
        return deviceManufacturer
    }

    /**
     * 获取电池信息
     * 返回 Pair<电量百分比, 是否充电>
     */
    fun getBatteryInfo(context: Context): Pair<Int, Boolean> {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryVal = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        val chargeStatus = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val isCharging = chargeStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) in
                arrayOf(BatteryManager.BATTERY_STATUS_CHARGING, BatteryManager.BATTERY_STATUS_FULL)

        return Pair(batteryVal, isCharging)
    }

    // 设备型号
    fun getDeviceModelInfo(context: Context): String {
        val deviceModel = Build.MODEL
        return deviceModel
    }

    // 获取硬件名称
    fun getDeviceHardware(context: Context): String {
        return Build.HARDWARE
    }

    // 获取主板名称
    fun getDeviceBoard(context: Context): String {
        return Build.BOARD
    }

    // 获取支持的 CPU 架构
    fun getDeviceSupportedAbis(context: Context): String {
        return Build.SUPPORTED_ABIS.joinToString(", ")
    }

    // 获取屏幕分辨率和密度
    fun getDeviceDisplayMetrics(context: Context): String {
        val metrics = Resources.getSystem().displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val densityDpi = metrics.densityDpi
        return "分辨率: ${width}x$height, 密度: ${densityDpi}dpi"
    }

    // 获取运营商名称（可能返回 null 或空字符串）
    @SuppressLint("MissingPermission")
    fun getNetworkOperatorName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkOperatorName ?: "未知"
    }

    // 获取当前移动网络类型
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return "无网络"
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "未知"

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "移动数据"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "以太网"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "蓝牙"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
            else -> "其他"
        }
    }

    // 检查开发者选项状态
    fun isDeveloperModeEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            ) != 0
        } catch (e: Exception) {
            false
        }
    }

    // 检查USB调试
    fun isUsbDebugEnabled(context: Context): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED, 0
            ) != 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 获取当前设备 IP 地址（WiFi 或移动网络）
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getDeviceIpAddress(context: Context): String {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork ?: return "未连接网络"
            val caps = cm.getNetworkCapabilities(network) ?: return "无法获取网络能力"
            when {
                caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    val wifiManager =
                        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val ipInt = wifiManager.connectionInfo.ipAddress
                    // 转换成点分十进制
                    return ((ipInt and 0xFF).toString() + "." +
                            (ipInt shr 8 and 0xFF) + "." +
                            (ipInt shr 16 and 0xFF) + "." +
                            (ipInt shr 24 and 0xFF))
                }
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    // 尝试获取移动网络 IP
                    try {
                        val interfaces = NetworkInterface.getNetworkInterfaces()
                        for (intf in interfaces) {
                            val addrs = intf.inetAddresses
                            for (addr in addrs) {
                                if (!addr.isLoopbackAddress && addr is InetAddress) {
                                    val sAddr = addr.hostAddress
                                    if (sAddr.indexOf(':') < 0) return sAddr
                                }
                            }
                        }
                    } catch (_: Exception) { }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "无法获取 IP"
    }

    /**
     * 获取内部存储总容量（Bytes）
     */
    fun getTotalInternalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long
        val totalBlocks: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.blockSizeLong
            totalBlocks = stat.blockCountLong
        } else {
            blockSize = stat.blockSize.toLong()
            totalBlocks = stat.blockCount.toLong()
        }
        return blockSize * totalBlocks
    }

    /**
     * 获取内部存储可用容量（Bytes）
     */
    fun getAvailableInternalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long
        val availableBlocks: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.blockSizeLong
            availableBlocks = stat.availableBlocksLong
        } else {
            blockSize = stat.blockSize.toLong()
            availableBlocks = stat.availableBlocks.toLong()
        }
        return blockSize * availableBlocks
    }

    /**
     * 格式化 Bytes 为可读的 MB / GB
     */
    fun formatSize(bytes: Long): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        return when {
            gb >= 1 -> String.format("%.2f GB", gb)
            mb >= 1 -> String.format("%.2f MB", mb)
            kb >= 1 -> String.format("%.2f KB", kb)
            else -> "$bytes Bytes"
        }
    }
}
