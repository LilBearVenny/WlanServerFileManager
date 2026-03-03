package com.app.mowanxing.wlanserverfilemanager

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mowanxing.wlanserverfilemanager.Page_ServerClipboard.Page_ServerClipboard
import com.app.mowanxing.wlanserverfilemanager.Page_ServerFiles.Page_ServerFiles
import com.app.mowanxing.wlanserverfilemanager.Page_ServerPublicInfo.Page_ServerPublicInfo
import com.app.mowanxing.wlanserverfilemanager.Page_Settings.Page_Settings
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_BottomBar_NavigationBar_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_BottomBar_NavigationBar_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HorizonDivider_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_HorizonDivider_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_Text_On_NavigationBar
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_TopBar_CenterAlignedTopAppBar_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_TopBar_CenterAlignedTopAppBar_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_TopNavigationBar
import com.app.mowanxing.wlanserverfilemanager.ui.theme.WlanServerFileManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import kotlin.collections.listOf

@HiltAndroidApp
class CustomApplication: Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WlanServerFileManagerTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val onDarkMode: Boolean = isSystemInDarkTheme()
    val pageList = remember {
        listOf(
            "文件",
            "剪贴板",
            "设置"
        )
    }
    val pageStatus = rememberPagerState(initialPage = 0, pageCount = { pageList.size })
    val coroutineScope = rememberCoroutineScope()
    val topBarDefaultHeight = TopAppBarDefaults.TopAppBarExpandedHeight

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    expandedHeight = TopAppBarDefaults.TopAppBarExpandedHeight * 0.875f,
                    modifier = Modifier,
                    title = {
                        Text(
                            text = when (pageStatus.currentPage) {
                                0 -> "文件"
                                1 -> "剪贴板"
                                else -> "设置"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            if (onDarkMode)
                                CustomColor_TopBar_CenterAlignedTopAppBar_Content_OnDark
                            else
                                CustomColor_TopBar_CenterAlignedTopAppBar_Content_OnLight,
                        titleContentColor =
                            if (onDarkMode)
                                CustomColor_GeneralColor_TextOfItem_Content_OnDark
                            else
                                CustomColor_GeneralColor_TextOfItem_Content_OnLight
                    )
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_HorizonDivider_OnDark
                        else
                            CustomColor_GeneralColor_HorizonDivider_OnLight
                )
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_HorizonDivider_OnDark
                        else
                            CustomColor_GeneralColor_HorizonDivider_OnLight
                )
                NavigationBar(
                    modifier = Modifier.height(topBarDefaultHeight * 1.1f),
                    containerColor =
                        if (onDarkMode)
                            CustomColor_BottomBar_NavigationBar_Content_OnDark
                        else
                            CustomColor_BottomBar_NavigationBar_Content_OnLight
                ) {
                    pageList.forEachIndexed { index, title ->
                        NavigationBarItem(
                            selected = pageStatus.currentPage == index,
                            colors = NavigationBarItemDefaults.colors(/* indicatorColor = CustomColor_NavigationBarItemSelectedColor */),
                            onClick = {
                                coroutineScope.launch {
                                    pageStatus.animateScrollToPage(index)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (index) {
                                        0 -> Icons.Outlined.FileCopy        // 文件
                                        1 -> Icons.Outlined.ContentPaste    // 剪贴板
                                        else -> Icons.Outlined.Settings     // 设置
                                    },
                                    contentDescription = title,
                                    modifier = Modifier.padding(vertical = 1.dp)
                                )
                            },
                            label = { Text(text = title, fontWeight = FontWeight.SemiBold) },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pageStatus,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> Page_ServerFiles()         // 文件
                1 -> Page_ServerClipboard()     // 剪贴板
                else -> Page_Settings()            // 设置
            }
        }
    }
}