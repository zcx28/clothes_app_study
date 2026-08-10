package com.sydra.app.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.R
import kotlinx.coroutines.launch

private val Ink = Color(0xFF222222)
private val Secondary = Color(0xFF777777)
private val Divider = Color(0xFFE9E9E9)

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()

    fun showMessage(message: String) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            ProfileHero()
            Spacer(modifier = Modifier.height(28.dp))
            ProfileOrders(onAction = ::showMessage)
            Spacer(modifier = Modifier.height(28.dp))
            ProfileMemberLinks(onAction = ::showMessage)
            Spacer(modifier = Modifier.height(28.dp))
            ProfileAddress(onClick = { showMessage("地址管理即将开放") })
            Spacer(modifier = Modifier.height(20.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
private fun ProfileHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.profile_mountain),
            contentDescription = "SYDRA 山景主题",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.18f))
        )
        Image(
            painter = painterResource(R.drawable.sydra_wordmark),
            contentDescription = "SYDRA",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 26.dp)
                .width(104.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 16.dp, bottom = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(58.dp),
                shape = CircleShape,
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.sydra_wordmark),
                        contentDescription = null,
                        modifier = Modifier.width(45.dp),
                        colorFilter = ColorFilter.tint(Ink)
                    )
                }
            }
            Text(
                text = "用户名",
                modifier = Modifier.padding(start = 12.dp),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        MemberBadge(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 27.dp)
        )
    }
}

@Composable
private fun MemberBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(2.dp)),
        shape = RoundedCornerShape(2.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MEMBER",
                color = Ink,
                fontSize = 16.sp,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "T5",
                color = Ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileOrders(onAction: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    role = Role.Button,
                    onClickLabel = "查看全部订单",
                    onClick = { onAction("订单列表即将开放") }
                )
                .padding(horizontal = 20.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("我的订单", color = Ink, fontSize = 15.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text("全部订单", color = Secondary, fontSize = 12.sp)
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(18.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val orderItems = listOf(
                OrderItem("待付款", Icons.Outlined.AccountBalanceWallet),
                OrderItem("待发货", Icons.Outlined.Inventory2),
                OrderItem("待收货", Icons.Outlined.LocalShipping),
                OrderItem("已完成", Icons.Outlined.AssignmentTurnedIn),
                OrderItem("售后", Icons.AutoMirrored.Outlined.ReceiptLong)
            )
            orderItems.forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            role = Role.Button,
                            onClickLabel = "查看${item.label}订单",
                            onClick = { onAction("${item.label}订单即将开放") }
                        )
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = Ink,
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = item.label,
                        modifier = Modifier.padding(top = 7.dp),
                        color = Secondary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileMemberLinks(onAction: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "SYDRA 会员",
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Ink,
            fontSize = 16.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileLinkCard(
                imageRes = R.drawable.product_04,
                title = "会员信息",
                onClick = { onAction("会员信息即将开放") },
                modifier = Modifier.weight(1f)
            )
            ProfileLinkCard(
                imageRes = R.drawable.product_05,
                title = "使用说明",
                onClick = { onAction("使用说明即将开放") },
                modifier = Modifier.weight(1f)
            )
            ProfileLinkCard(
                imageRes = R.drawable.product_06,
                title = "客户服务",
                onClick = { onAction("客户服务即将开放") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProfileLinkCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(148.dp)
            .clip(RoundedCornerShape(1.dp))
            .clickable(
                role = Role.Button,
                onClickLabel = title,
                onClick = onClick
            )
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.28f))
        )
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, end = 8.dp, bottom = 11.dp),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ProfileAddress(onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.material3.HorizontalDivider(color = Divider)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    role = Role.Button,
                    onClickLabel = "打开收货地址",
                    onClick = onClick
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "收货地址",
                tint = Ink,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = "收货地址",
                modifier = Modifier.padding(start = 10.dp),
                color = Ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(22.dp)
            )
        }
        androidx.compose.material3.HorizontalDivider(color = Divider)
    }
}

private data class OrderItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
