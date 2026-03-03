package com.app.mowanxing.wlanserverfilemanager.Page_ServerImages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.mowanxing.wlanserverfilemanager.ui.theme.CustomColor_CardViewBorderGrey

/**
 * 未显示和未使用的@Composable函数
 */
@Composable fun Page_ServerImages() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(2.dp)
        ) {
            items(20) {
                Private_ItemView_ServerImageDisplay()
            }
        }
    }
}

@Composable
fun Private_ItemView_ServerImageDisplay() {
    Card(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp, top = 3.dp, bottom = 3.dp)
            .fillMaxWidth()
            .border(width = 0.5.dp, color = CustomColor_CardViewBorderGrey, shape = RoundedCornerShape(8.dp))
            .aspectRatio(0.8f),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // 图片区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                // 这里用颜色代替图片
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                )
            }
            // 分割线
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.5.dp,
                color = Color.Gray.copy(alpha = 0.2f)
            )
            // 内容区域
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                // 底部信息栏
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 用户头像
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFFE0E0E0), CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 用户名
                    Text(
                        text = "用户名",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // 喜欢按钮
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "喜欢",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}