package com.app.mowanxing.wlanserverfilemanager.Page_ServerPublicInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_ItemOfList_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_ItemOfList_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_Page_Background_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Content_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Content_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Time_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Time_OnLight
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Title_OnDark
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_GeneralColor_TextOfItem_Title_OnLight

@Composable
fun Page_ServerPublicInfo() {
    val onDarkMode: Boolean = isSystemInDarkTheme()
    // 模拟公告数据
    val notices = listOf(
        NoticeItem("服务器维护通知", "服务器将在今晚 23:00 进行短暂维护，请提前保存文件。", "2025-11-05 20:30"),
        NoticeItem("版本更新公告", "App v2.4.1 已发布，修复若干崩溃问题并提升性能。", "2025-11-02 09:10"),
        NoticeItem("文件存储优化", "服务器文件存储方案已升级，上传速度提升 30%。", "2025-10-28 15:45"),
        NoticeItem("安全提示", "请勿轻信陌生人发送的下载链接，避免账号被盗。", "2025-10-20 12:00")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (onDarkMode)
                    CustomColor_GeneralColor_Page_Background_OnDark
                else
                    CustomColor_GeneralColor_Page_Background_OnLight
            ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notices) { notice ->
            NoticeCard(notice)
        }
    }
}

@Composable
fun NoticeCard(notice: NoticeItem) {
    val onDarkMode: Boolean = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors =
            if (onDarkMode)
                CardDefaults.cardColors(CustomColor_GeneralColor_ItemOfList_Content_OnDark)
            else
                CardDefaults.cardColors(CustomColor_GeneralColor_ItemOfList_Content_OnLight)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = Color(0xFF2196F3),
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = notice.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_TextOfItem_Title_OnDark
                        else
                            CustomColor_GeneralColor_TextOfItem_Title_OnLight
                )   /* 标题 */

                Text(
                    text = notice.time,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_TextOfItem_Time_OnDark
                        else
                            CustomColor_GeneralColor_TextOfItem_Time_OnLight
                )   /* 时间 */

                Text(
                    text = notice.content,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp),
                    color =
                        if (onDarkMode)
                            CustomColor_GeneralColor_TextOfItem_Content_OnDark
                        else
                            CustomColor_GeneralColor_TextOfItem_Content_OnLight
                )   /* 内容 */
            }
        }
    }
}

data class NoticeItem(
    val title: String,
    val content: String,
    val time: String
)

