package com.app.mowanxing.wlanserverfilemanager.Page_ServerClipboard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.mowanxing.wlanserverfilemanager.network.ClipboardItemForGet
import com.app.mowanxing.wlanserverfilemanager.network.ClipboardApiRetrofitClient
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_ClipboardCardViewColor
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HintText_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HintText_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HorizonDivider_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HorizonDivider_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_ItemOfList_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_ItemOfList_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_OutlinedTextField_ContainerColor_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_OutlinedTextField_ContainerColor_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PageBgDarkModeColor
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PreviewFontGrey
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PureWhite
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_TextFieldBgColor
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_Transparency
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_XiaomiBlue
import com.app.mowanxing.wlanserverfilemanager.viewmodel.ClipboardViewModel
import com.app.mowanxing.wlanserverfilemanager.viewmodel.LocalDataViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
@Composable
fun Page_ServerClipboard(
    localDataViewModel: LocalDataViewModel = hiltViewModel()
) {
    val onDarkMode: Boolean = isSystemInDarkTheme()
    var textInTextField by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val viewModel = remember { ClipboardViewModel() }
    val clipboardList by viewModel.clipboardList
    val composeClipboardManager = LocalClipboardManager.current
    val serverResponseMessage by viewModel.serverMessageFlow.collectAsState()
    val context = LocalContext.current
    val clipboardLoadingState by viewModel.clipboardLoadingState
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = clipboardLoadingState)
    val readDeviceId by localDataViewModel.readDeviceId.collectAsState()
    val readServerIp by localDataViewModel.readServerIp.collectAsState()
    val topBarDefaultHeight = TopAppBarDefaults.TopAppBarExpandedHeight /* 获取顶部导航栏的高度 */

    LaunchedEffect(key1 = Unit, key2 = readServerIp, key3 = serverResponseMessage) {
        ClipboardApiRetrofitClient.setBaseUrl(readServerIp.toString())  // 获取DataStore的ServerIp
        viewModel.loadClipboardData()
        serverResponseMessage?.let {
            Log.d("PageServerClipboard.kt", "拿到ViewModel的ServerMessage: $serverResponseMessage")
        }
    }

    if (serverResponseMessage != null) {
        ServerMessageAlertDialog(
            message = serverResponseMessage,
            onClearClientMessage = { viewModel.clearMessage() }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color =
                    if (onDarkMode)
                        CustomColor_GeneralColor_Page_Background_OnDark
                    else
                        CustomColor_GeneralColor_Page_Background_OnLight
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (3 + 15).dp, end = (3 + 15).dp, bottom = 5.dp)
                    .height(45.dp)
            ) {
                Text(
                    text = "上传内容至剪贴板",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(0.6f)
                        .wrapContentHeight(Alignment.CenterVertically)
                )
                Card(
                    modifier = Modifier
                        .fillMaxHeight(0.8f),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Button(
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                if (onDarkMode)
                                    CustomColor_XiaomiBlue
                                else
                                    CustomColor_XiaomiBlue
                        ),
                        modifier = Modifier.fillMaxSize(),
                        onClick = {
                            val submitText = textInTextField
                            viewModel.submitClipboardData(content = submitText, deviceId = readDeviceId.toString())
                            textInTextField = ""
                            Log.d("PageServerClipboard.kt", "点击提交后的ServerMessage: $serverResponseMessage")
                        },
                    ) {
                        Text(
                            text = "提交",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            OutlinedTextField(
                value = textInTextField,
                onValueChange = { textInTextField = it },
                placeholder = { Text(text = "请在此处粘贴内容", color = CustomColor_PreviewFontGrey) },
                singleLine = false,
                maxLines = 8,
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
                textStyle = TextStyle(fontSize = 14.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CustomColor_Transparency,
                    unfocusedBorderColor = CustomColor_Transparency,
                    focusedContainerColor =
                        if (onDarkMode)
                            CustomColor_OutlinedTextField_ContainerColor_OnDark
                        else
                            CustomColor_OutlinedTextField_ContainerColor_OnLight,
                    unfocusedContainerColor =
                        if (onDarkMode)
                            CustomColor_OutlinedTextField_ContainerColor_OnDark
                        else
                            CustomColor_OutlinedTextField_ContainerColor_OnLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "已输入 ${textInTextField.length} 个字符",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = (3 + 15).dp),
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_HintText_OnDark
                        else
                            CustomColor_GeneralColor_HintText_OnLight
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "点击此处清空文本",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(end = (3 + 15).dp)
                        .clickable { textInTextField = "" },
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_HintText_OnDark
                        else
                            CustomColor_GeneralColor_HintText_OnLight
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "剪贴板记录",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = (3 + 15).dp, bottom = 10.dp)
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.5.dp,
                color = Color.Gray.copy(alpha = 0.2f)
            )
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.loadClipboardData() },
                modifier = Modifier
            ) {
                when {
                    clipboardLoadingState -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    clipboardList.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { viewModel.loadClipboardData() }
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally // 水平居中
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "剪贴板列表数据为空",
                                    tint =
                                        if (onDarkMode)
                                            CustomColor_GeneralColor_HintText_OnDark
                                        else
                                            CustomColor_GeneralColor_HintText_OnLight,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(5.dp)) // Icon 和 Text 之间间距
                                Text(
                                    text = "暂无数据\n点击刷新",
                                    color =
                                        if (onDarkMode)
                                            CustomColor_GeneralColor_HintText_OnDark
                                        else
                                            CustomColor_GeneralColor_HintText_OnLight,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    else -> {
                        Box() {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(clipboardList, key = { it.id }) { item ->
                                    Private_ItemView_ClipboardRecordDisplay(
                                        item = item,
                                        onCopy = { text ->
                                            composeClipboardManager.setText(AnnotatedString(text))
                                            Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                            // 顶部渐变 遮罩在List外层
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.dp) /* 设置为 0.dp - 暂时弃用 */
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xAAFFFFFF),
                                                Color.Transparent
                                            )
                                        )
                                    ).align(Alignment.TopCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun Private_ItemView_ClipboardRecordDisplay(
    item: ClipboardItemForGet,
    onCopy: (String) -> Unit
) {
    val onDarkMode: Boolean = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors =
            if (onDarkMode)
                CardDefaults.cardColors(CustomColor_GeneralColor_ItemOfList_Content_OnDark)
            else
                CardDefaults.cardColors(CustomColor_GeneralColor_ItemOfList_Content_OnLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(modifier = Modifier) {
                Column(modifier = Modifier.weight(0.7f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "时间：",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.created_at,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "设备：",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                        )
                        Text(
                            text = item.device,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(0.3f)
                ) {
                    Text(
                        text = "点击复制内容",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color =
                            if (onDarkMode)
                                CustomColor_GeneralColor_HintText_OnDark
                            else
                                CustomColor_GeneralColor_HintText_OnLight,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onCopy(item.content) }
                            .fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color =
                    if (onDarkMode)
                        CustomColor_GeneralColor_HorizonDivider_OnDark
                    else
                        CustomColor_GeneralColor_HorizonDivider_OnLight
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 1.dp)
                )
            }
        }
    }
}
@Composable
fun ServerMessageAlertDialog(
    message: String?,
    onClearClientMessage: () -> Unit
) {
    if (message != null) {
        AlertDialog(
            icon = { Icon(Icons.Outlined.Info, contentDescription = "对话框提示图标") },
            onDismissRequest = onClearClientMessage,
            confirmButton = {
                TextButton(onClick = onClearClientMessage) {
                    Text(text = "确定", fontWeight = FontWeight.SemiBold)
                }
            },
            title = { Text(text = "内容上传状态", fontWeight = FontWeight.SemiBold, modifier = Modifier) },
            text = { Text(text = message, fontWeight = FontWeight.SemiBold, modifier = Modifier) },
            shape = RoundedCornerShape(10.dp)
        )
    }
}

