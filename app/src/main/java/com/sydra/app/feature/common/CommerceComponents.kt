package com.sydra.app.feature.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.R
import com.sydra.app.domain.model.OrderItemSnapshot

val SydraInk = Color(0xFF171717)
val SydraMuted = Color(0xFF8B8B8B)
val SydraCanvas = Color(0xFFF7F7F7)
val SydraLine = Color(0xFFE8E8E8)
val SydraAccent = Color(0xFFE60012)

@Composable
fun CommerceTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(52.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "返回",
                    tint = SydraInk
                )
            }
            Text(
                text = title,
                modifier = Modifier.align(Alignment.Center),
                color = SydraInk,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(color = SydraLine)
    }
}

@Composable
fun BlackButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFBDBDBD),
            disabledContentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun AsyncMessage(
    title: String,
    description: String,
    onRetry: (() -> Unit)? = null,
    loading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (loading) {
            CircularProgressIndicator(color = SydraInk, strokeWidth = 2.dp)
        } else {
            Text(title, color = SydraInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = description,
                modifier = Modifier.padding(top = 10.dp),
                color = SydraMuted,
                fontSize = 13.sp
            )
            if (onRetry != null) {
                OutlinedButton(
                    onClick = onRetry,
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text("重试", color = SydraInk)
                }
            }
        }
    }
}

@Composable
fun OrderProductSnapshot(
    item: OrderItemSnapshot,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(resolveOrderImage(item.imageUrl)),
            contentDescription = item.title,
            modifier = Modifier.size(width = 68.dp, height = 86.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        ) {
            Text(item.title, color = SydraInk, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(
                text = "SKU ${item.sku} · 尺码 ${item.size}",
                modifier = Modifier.padding(top = 5.dp),
                color = SydraMuted,
                fontSize = 11.sp
            )
            Text(
                text = formatMoney(item.unitPriceCents),
                modifier = Modifier.padding(top = 12.dp),
                color = SydraAccent,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("× ${item.quantity}", color = SydraInk, fontSize = 12.sp)
            if (trailing != null) {
                Spacer(Modifier.height(20.dp))
                trailing()
            }
        }
    }
}

@Composable
fun SydraWordmark(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.sydra_wordmark),
        contentDescription = "SYDRA",
        modifier = modifier.width(82.dp),
        colorFilter = ColorFilter.tint(SydraInk)
    )
}

fun formatMoney(cents: Int): String = "¥${cents / 100}.${(cents % 100).toString().padStart(2, '0')}"

fun formatTimestamp(value: String?): String = value
    ?.replace('T', ' ')
    ?.substringBefore('+')
    ?: "--"

private fun resolveOrderImage(imageUrl: String?): Int = when {
    imageUrl?.contains("product_02") == true -> R.drawable.product_02
    imageUrl?.contains("product_03") == true -> R.drawable.product_03
    imageUrl?.contains("product_04") == true -> R.drawable.product_04
    imageUrl?.contains("product_05") == true -> R.drawable.product_05
    imageUrl?.contains("product_06") == true -> R.drawable.product_06
    else -> R.drawable.product_01
}
