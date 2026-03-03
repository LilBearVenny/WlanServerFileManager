package com.app.mowanxing.wlanserverfilemanager.Page_Settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.mowanxing.wlanserverfilemanager.R
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_Button_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_Button_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_Button_WhenDisabled_Content_OnLightOrDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Card_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Card_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HintText_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HintText_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_ModalBottomSheet_Container_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_ModalBottomSheet_Container_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_OutlinedTextField_ContainerColor_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_OutlinedTextField_ContainerColor_OnLight
import com.app.mowanxing.wlanserverfilemanager.viewmodel.AvailableServerIpViewModel
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PreviewFontGrey
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PureBlack
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_PureWhite
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_TextFieldBgColor
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_Transparency
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_XiaomiBlue
import com.app.mowanxing.wlanserverfilemanager.viewmodel.LocalDataViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Page_Settings(
    localDataViewModel: LocalDataViewModel = hiltViewModel(),
    availableServerIpViewModel: AvailableServerIpViewModel = hiltViewModel()
) {
    val onDarkMode: Boolean = isSystemInDarkTheme()
    val readServerIp by localDataViewModel.readServerIp.collectAsState()
    val readDeviceId by localDataViewModel.readDeviceId.collectAsState()
    val availableServerIp by availableServerIpViewModel.availableServerIp.collectAsState()
    val availableServerIpLoadingState by availableServerIpViewModel.availableServerIpLoading
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showModalBottomSheetStateOfDeviceId by remember { mutableStateOf(false) }
    var showModalBottomSheetStateOfServerIp by remember { mutableStateOf(false) }
    var showModalBottomSheetStateOfAvailableServerIp by remember { mutableStateOf(false) }
    var textInModalBottomSheetOfDeviceIdTextField: String? by remember { mutableStateOf("") }
    var textInModalBottomSheetOfServerIpTextField: String? by remember { mutableStateOf("") }
    val fromComposeClipboardManager = LocalClipboardManager.current
    val currentContext = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(
        key1 = showModalBottomSheetStateOfDeviceId,
        key2 = showModalBottomSheetStateOfServerIp,
    ) {
        if (showModalBottomSheetStateOfDeviceId) {
            textInModalBottomSheetOfDeviceIdTextField = readDeviceId
        }
        if (showModalBottomSheetStateOfServerIp) {
            textInModalBottomSheetOfServerIpTextField = readServerIp
        }
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
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 更改服务器IP
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors =
                        if (onDarkMode)
                            CardDefaults.cardColors(containerColor = CustomColor_GeneralColor_Card_Content_OnDark)
                        else
                            CardDefaults.cardColors(containerColor = CustomColor_GeneralColor_Card_Content_OnLight),
                    modifier = Modifier
                        .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            coroutineScope.launch {
                                showModalBottomSheetStateOfServerIp = true
                            }
                        }
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Image(
                            /* painter = painterResource(id = R.drawable.custom_icon_server), */
                            imageVector = Icons.Outlined.Dns,
                            contentDescription = "更改连接服务器IP地址选项",
                            modifier = Modifier.size(60.dp).padding(top = 5.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "更改保存的IP地址",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row {
                            Text(
                                text = "当前: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color =
                                    if (onDarkMode)
                                        CustomColor_GeneralColor_HintText_OnDark
                                    else
                                        CustomColor_GeneralColor_HintText_OnLight,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                            readServerIp?.let { it ->
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color =
                                        if (onDarkMode)
                                            CustomColor_GeneralColor_HintText_OnDark
                                        else
                                            CustomColor_GeneralColor_HintText_OnLight,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "点击此处，扫描服务器IP地址",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_HintText_OnDark
                        else
                            CustomColor_GeneralColor_HintText_OnLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        availableServerIpViewModel.scanAvailableServerIp()
                        coroutineScope.launch {
                            showModalBottomSheetStateOfAvailableServerIp = true
                        }
                    }
                )
            }
            // 更改设备标识符
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors =
                        if (onDarkMode)
                            CardDefaults.cardColors(containerColor = CustomColor_GeneralColor_Card_Content_OnDark)
                        else
                            CardDefaults.cardColors(containerColor = CustomColor_GeneralColor_Card_Content_OnLight),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            coroutineScope.launch {
                                showModalBottomSheetStateOfDeviceId = true
                            }
                        }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Image(
                            /* painter = painterResource(id = R.drawable.custom_icon_phone), */
                            imageVector = Icons.Outlined.PhoneIphone,
                            contentDescription = "更改设备标识符选项",
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "更改设备标识符",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row {
                            Text(
                                text = "当前: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color =
                                    if (onDarkMode)
                                        CustomColor_GeneralColor_HintText_OnDark
                                    else
                                        CustomColor_GeneralColor_HintText_OnLight,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                            readDeviceId?.let { it ->
                                Text(
                                    text = it,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color =
                                        if (onDarkMode)
                                            CustomColor_GeneralColor_HintText_OnDark
                                        else
                                            CustomColor_GeneralColor_HintText_OnLight,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    // 显示服务器IP地址扫描结果的底部对话框
    @Composable
    fun Private_ModalBottomSheet_Display_AvaliableServerIp() {
        ModalBottomSheet(
            containerColor =
                if (onDarkMode)
                    CustomColor_ModalBottomSheet_Container_OnDark
                else
                    CustomColor_ModalBottomSheet_Container_OnLight,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .width(70.dp)
                        .height(4.dp)
                        .background(
                            color = Color.Gray.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            },
            scrimColor = Color.Black.copy(alpha = 0.5f),
            onDismissRequest = {
                coroutineScope.launch { modalBottomSheetState.hide() }
                showModalBottomSheetStateOfAvailableServerIp = false
                availableServerIpViewModel.clearAlreadyExistAvailableServerIpData()
            },
            sheetState = modalBottomSheetState,
            modifier = Modifier
        ){
            Box {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 35.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "查看可用的服务器IP地址",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = availableServerIp.firstOrNull() ?: "正在请求中，请稍候 ...",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row () {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (onDarkMode)
                                        CustomColor_Button_Content_OnDark
                                    else
                                        CustomColor_Button_Content_OnLight,
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(0.47f),
                            onClick = {
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                    showModalBottomSheetStateOfAvailableServerIp = false
                                }
                            }
                        ) {
                            Text(
                                text = "取消",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(5.dp),
                                color =
                                    if (onDarkMode)
                                        CustomColor_OutlinedTextField_ContainerColor_OnLight
                                    else
                                        CustomColor_OutlinedTextField_ContainerColor_OnDark
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.06f))
                        Button(
                            colors = (
                                    if (availableServerIp.toString().contains("无可用服务器IP")) {
                                        ButtonDefaults.buttonColors(containerColor = CustomColor_Button_WhenDisabled_Content_OnLightOrDark)
                                    } else {
                                        ButtonDefaults.buttonColors(containerColor = CustomColor_XiaomiBlue)
                                    }
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(0.47f),
                            onClick = {
                                if (availableServerIp.toString().contains("无可用服务器IP")) {
                                    Toast.makeText(currentContext, "禁止复制！", Toast.LENGTH_SHORT).show()
                                } else {
                                    fromComposeClipboardManager.setText(AnnotatedString(availableServerIp.firstOrNull().toString()))
                                    coroutineScope.launch {
                                        Toast.makeText(currentContext, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                                        modalBottomSheetState.hide()
                                        showModalBottomSheetStateOfAvailableServerIp = false
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "复制",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // 更改设备标识符底部对话框
    @Composable
    fun Private_ModalBottomSheet_Management_LocalDeviceId() {
        ModalBottomSheet(
            containerColor =
                if (onDarkMode)
                    CustomColor_ModalBottomSheet_Container_OnDark
                else
                    CustomColor_ModalBottomSheet_Container_OnLight,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .width(70.dp)
                        .height(4.dp)
                        .background(
                            color = Color.Gray.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            },
            scrimColor = Color.Black.copy(alpha = 0.5f),
            onDismissRequest = {
                coroutineScope.launch { modalBottomSheetState.hide() }
                showModalBottomSheetStateOfDeviceId = false
            },
            sheetState = modalBottomSheetState,
            modifier = Modifier
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 35.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "更改设备标识符",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = textInModalBottomSheetOfDeviceIdTextField.toString(),
                        onValueChange = { textInModalBottomSheetOfDeviceIdTextField = it },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CustomColor_XiaomiBlue,
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row () {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (onDarkMode)
                                        CustomColor_Button_Content_OnDark
                                    else
                                        CustomColor_Button_Content_OnLight,
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(0.47f),
                            onClick = {
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                    showModalBottomSheetStateOfDeviceId = false
                                }
                            }
                        ) {
                            Text(
                                text = "取消",
                                fontSize = 16.sp,
                                color =
                                    if (onDarkMode)
                                        CustomColor_OutlinedTextField_ContainerColor_OnLight
                                    else
                                        CustomColor_OutlinedTextField_ContainerColor_OnDark,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.06f))
                        Button(
                            colors =
                                if (textInModalBottomSheetOfDeviceIdTextField.isNullOrBlank())
                                    ButtonDefaults.buttonColors(containerColor = CustomColor_Button_WhenDisabled_Content_OnLightOrDark)
                                else
                                    ButtonDefaults.buttonColors(containerColor = CustomColor_XiaomiBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(0.47f),
                            onClick = {
                                if (textInModalBottomSheetOfDeviceIdTextField.isNullOrBlank()) {
                                    Toast.makeText(currentContext, "内容为空，禁止保存！", Toast.LENGTH_SHORT).show()
                                } else {
                                    localDataViewModel.saveDeviceId(textInModalBottomSheetOfDeviceIdTextField.toString())
                                    coroutineScope.launch {
                                        modalBottomSheetState.hide()
                                        showModalBottomSheetStateOfDeviceId = false
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "保存",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // 管理存储在设备的可用服务器IP地址数据的底部对话框
    @Composable
    fun Private_ModalBottomSheet_Management_LocalServerIpData() {
        ModalBottomSheet(
            containerColor =
                if (onDarkMode)
                    CustomColor_ModalBottomSheet_Container_OnDark
                else
                    CustomColor_ModalBottomSheet_Container_OnLight,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .width(70.dp)
                        .height(4.dp)
                        .background(
                            color = Color.Gray.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            },
            scrimColor = Color.Black.copy(alpha = 0.5f),
            onDismissRequest = {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                    showModalBottomSheetStateOfServerIp = false
                }
            },
            sheetState = modalBottomSheetState,
            modifier = Modifier
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 35.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "更改服务器IP地址",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = textInModalBottomSheetOfServerIpTextField.toString(),
                        onValueChange = { textInModalBottomSheetOfServerIpTextField = it },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CustomColor_XiaomiBlue,
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row() {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (onDarkMode)
                                        CustomColor_Button_Content_OnDark
                                    else
                                        CustomColor_Button_Content_OnLight,
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(0.47f),
                            onClick = {
                                coroutineScope.launch {
                                    modalBottomSheetState.hide()
                                    showModalBottomSheetStateOfServerIp = false
                                }
                            }
                        ) {
                            Text(
                                text = "取消",
                                fontSize = 16.sp,
                                color =
                                    if (onDarkMode)
                                        CustomColor_OutlinedTextField_ContainerColor_OnLight
                                    else
                                        CustomColor_OutlinedTextField_ContainerColor_OnDark,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.06f))
                        Button(
                            colors =
                                if (textInModalBottomSheetOfServerIpTextField.isNullOrBlank())
                                    ButtonDefaults.buttonColors(containerColor = CustomColor_Button_WhenDisabled_Content_OnLightOrDark)
                                else
                                    ButtonDefaults.buttonColors(containerColor = CustomColor_XiaomiBlue),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(0.47f),
                            onClick = {
                                if (textInModalBottomSheetOfServerIpTextField.isNullOrBlank()) {
                                    Toast.makeText(currentContext, "内容为空，禁止保存！", Toast.LENGTH_SHORT).show()
                                } else {
                                    localDataViewModel.saveServerIp(textInModalBottomSheetOfServerIpTextField.toString())
                                    coroutineScope.launch {
                                        modalBottomSheetState.hide()
                                        showModalBottomSheetStateOfServerIp = false
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "保存",
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }


    if (showModalBottomSheetStateOfServerIp) {
        Private_ModalBottomSheet_Management_LocalServerIpData()
    }

    if (showModalBottomSheetStateOfDeviceId) {
        Private_ModalBottomSheet_Management_LocalDeviceId()
    }

    if (showModalBottomSheetStateOfAvailableServerIp) {
        Private_ModalBottomSheet_Display_AvaliableServerIp()
    }
}