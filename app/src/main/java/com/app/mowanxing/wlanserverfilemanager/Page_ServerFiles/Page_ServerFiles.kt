package com.app.mowanxing.wlanserverfilemanager.Page_ServerFiles

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.mowanxing.wlanserverfilemanager.R
import com.app.mowanxing.wlanserverfilemanager.network.ServerFilesApiRetrofitClient
import com.app.mowanxing.wlanserverfilemanager.network.ServerFilesInfoForGet
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PreviewFontGrey
import com.app.mowanxing.wlanserverfilemanager.viewmodel.LocalDataViewModel
import com.app.mowanxing.wlanserverfilemanager.viewmodel.ServerFilesInfoViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_DropDownMenu_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_DropDownMenu_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_FloatingActionButtonContainerColor
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_FloatingActionButton_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_FloatingActionButton_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HorizonDivider_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HorizonDivider_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_ItemOfList_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_ItemOfList_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Time_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Time_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_ItemOfList_WhenPressing_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_ItemOfList_WhenPressing_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PageBgDarkModeColor
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Page_ServerFiles(
    localDataViewModel: LocalDataViewModel = hiltViewModel()
) {
    val onDarkMode: Boolean = isSystemInDarkTheme()
    val viewModel = remember { ServerFilesInfoViewModel() }
    val serverFilesInfoList by viewModel.serverFilesInfoList
    val serverFilesInfoLoadingState by viewModel.serverFilesInfoLoadingState
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = serverFilesInfoLoadingState)
    val readDeviceId by localDataViewModel.readDeviceId.collectAsState()
    val readServerIp by localDataViewModel.readServerIp.collectAsState()
    val currentContext = LocalContext.current

    // 用于选择文件
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                // 文件选择完成后，执行上传
                uploadFileToServer(currentContext, it, readServerIp.toString())
            }
        }
    )

    LaunchedEffect(key1 = readServerIp) {
        if (readServerIp.isNullOrBlank()) {
            Log.d("PageServerFiles.kt", "readServerIp为空，等待DataStore加载完毕")
            return@LaunchedEffect
        }

        Log.d("PageServerFiles.kt", "读取readServerIp变量值: $readServerIp")
        ServerFilesApiRetrofitClient.setBaseUrl(readServerIp!!)
        viewModel.loadServerFilesInfoData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (onDarkMode)
                    CustomColor_GeneralColor_Page_Background_OnDark
                else
                    CustomColor_GeneralColor_Page_Background_OnLight
            )
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.loadServerFilesInfoData() },
            modifier = Modifier
        ) {
            when {
                serverFilesInfoLoadingState -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                serverFilesInfoList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { viewModel.loadServerFilesInfoData() }
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally // 水平居中
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "文件列表数据为空",
                                tint = CustomColor_PreviewFontGrey,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(5.dp)) // Icon 和 Text 之间间距
                            Text(
                                text = "暂无数据\n点击刷新",
                                color = CustomColor_PreviewFontGrey,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(serverFilesInfoList, key = { it.file_name }) { item ->
                            Private_ItemView_ServerFileDisplay(
                                item = item,
                                serverIp = readServerIp,
                                context = currentContext
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            shape = RoundedCornerShape(40.dp),
            onClick = { filePickerLauncher.launch("*/*") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor =
                if (onDarkMode)
                    CustomColor_FloatingActionButton_Content_OnDark
                else
                    CustomColor_FloatingActionButton_Content_OnLight
        ) {
            Icon(
                imageVector = Icons.Outlined.UploadFile,
                contentDescription = "上传文件",
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun Private_ItemView_ServerFileDisplay(
    item: ServerFilesInfoForGet,
    serverIp: String?,
    context: Context
) {
    val onDarkMode: Boolean = isSystemInDarkTheme()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressing by interactionSource.collectIsPressedAsState()
    var expandedMenuPopState by remember { mutableStateOf(false) } // 控制长按item的弹出菜单显示

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    /* 列表的Item项被点击时 */
                    if (isPressing) {
                        /* 深色模式 */
                        if (onDarkMode) { CustomColor_ItemOfList_WhenPressing_Content_OnDark }
                        /* 浅色模式 */
                        else { CustomColor_ItemOfList_WhenPressing_Content_OnLight }
                    }
                    /* 列表的Item项未被点击时 */
                    else {
                        /* 深色模式 */
                        if (onDarkMode) { CustomColor_GeneralColor_ItemOfList_Content_OnDark }
                        /* 浅色模式 */
                        else { CustomColor_GeneralColor_ItemOfList_Content_OnLight }
                    }
            )
            .combinedClickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { /* 普通点击 */ },
                onLongClick = { expandedMenuPopState = true } // 长按弹出菜单
            )
    ) {
        // 主体内容
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.file_type.lowercase() in listOf("jpg", "jpeg", "png")) {
                Card (
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .size(60.dp)
                ) {
                    // —— 显示网络图片缩略图 ——
                    AsyncImage(
                        model = "http://${serverIp}/upload_files/uploads/${item.file_name}",
                        contentDescription = "图片缩略图",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.custom_icon_file_format_images),
                        error = painterResource(R.drawable.custom_icon_file_format_images)
                    )
                }
            } else {
                // —— 显示静态文件类型图标 ——
                Image(
                    painter =
                        when (item.file_type.lowercase()) {
                            "txt" -> painterResource(id = R.drawable.custom_icon_file_format_txt)
                            "pdf" -> painterResource(id = R.drawable.custom_icon_file_format_pdf)
                            "doc", "docx" -> painterResource(id = R.drawable.custom_icon_file_format_images_word)
                            "xls", "xlsx" -> painterResource(id = R.drawable.custom_icon_file_format_excel)
                            "ppt", "pptx" -> painterResource(id = R.drawable.custom_icon_file_format_images_powerpoint)
                            "apk" -> painterResource(id = R.drawable.custom_icon_file_format_apk)
                            "py", "php" -> painterResource(id = R.drawable.custom_icon_file_format_codefiles)
                            "html", "css", "js" -> painterResource(id = R.drawable.custom_icon_file_format_codefiles)
                            else -> painterResource(id = R.drawable.custom_iconf_file_format_other)
                        },
                    contentDescription = "文件格式预览图标",
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .size(60.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.file_name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .fillMaxWidth(), // ✅ 建议加上，让 Text 占满剩余空间
                    maxLines = 1, // ✅ 限制只显示一行
                    overflow = TextOverflow.Ellipsis // ✅ 超出部分用“…”省略
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${item.file_type.uppercase()} · ${item.file_modified_time} · ${item.file_size} ${item.file_size_unit}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_TextOfItem_Time_OnDark
                        else
                            CustomColor_GeneralColor_TextOfItem_Time_OnLight
                )
            }
            // 长按弹出的菜单
            DropdownMenu(
                expanded = expandedMenuPopState,
                onDismissRequest = { expandedMenuPopState = false },
                shape = RoundedCornerShape(10.dp),
                containerColor =
                    if (onDarkMode)
                        CustomColor_DropDownMenu_Content_OnDark
                    else
                        CustomColor_DropDownMenu_Content_OnLight,
                modifier = Modifier
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CloudDownload,
                            contentDescription = "下载文件图标",
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    },
                    text = {
                        Text(
                            text = "下载该文件",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    },
                    onClick = {
                        expandedMenuPopState = false
                        downloadServerFile(context, item.file_name, serverIp = serverIp.toString())
                    },
                )
            }
        }
        // 底部分割线（叠加在最底层）
        HorizontalDivider(
            color =
                if (onDarkMode)
                    CustomColor_GeneralColor_HorizonDivider_OnDark
                else
                    CustomColor_GeneralColor_HorizonDivider_OnLight,
            thickness = 0.5.dp,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}
private fun downloadServerFile(context: Context, fileName: String, serverIp: String) {
    val url = "http://${serverIp}/upload_files/uploads/$fileName" // 你的服务器下载地址
    val request = DownloadManager.Request(url.toUri()).apply {
        setTitle(fileName)
        setDescription("正在下载 $fileName ...")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // 下载过程显式通知
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ServerBridgeAppDownloads/$fileName")
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setMimeType("application/octet-stream")
        setAllowedOverMetered(true) // 允许使用流量下载
        setAllowedOverRoaming(true) // 允许漫游网络下载
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)

    Toast.makeText(context, "开始下载：$fileName", Toast.LENGTH_SHORT).show()
}

private fun uploadFileToServer(context: Context, uri: Uri, serverIp: String) {
    val contentResolver = context.contentResolver
    val fileName = getFileName(contentResolver, uri) ?: "unknown_file"

    // 将 Uri 转换为临时文件
    val inputStream = contentResolver.openInputStream(uri) ?: return
    val tempFile = File(context.cacheDir, fileName)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }

    val client = OkHttpClient()

    // 构造表单请求体
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file",
            fileName,
            tempFile.asRequestBody("application/octet-stream".toMediaType())
        )
        .build()

    val phpFileName = "upload_for_server_bridge.php"

    val request = Request.Builder()
        .url("http://${serverIp}/upload_files/${phpFileName}")
        .post(requestBody)
        .build()

    Log.d("Page_ServerFiles.kt", "Request.Builder().url()参数: ${request.url.toString()}")

    // 异步执行上传
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(context, "上传失败：${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: Call, response: Response) {
            (context as? Activity)?.runOnUiThread {
                if (response.isSuccessful) {
                    val responseBody = response.body.string()
                    val jsonObject = JSONObject(responseBody)
                    Toast.makeText(context, "文件[${jsonObject.getString("file_name")}]已上传", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context, "文件已上传", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "服务器错误：${response.code}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}
private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var name: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        }
    }
    return name
}

