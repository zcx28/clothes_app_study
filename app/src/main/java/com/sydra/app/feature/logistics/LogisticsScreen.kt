package com.sydra.app.feature.logistics

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.domain.model.Logistics
import com.sydra.app.domain.model.LogisticsEvent
import com.sydra.app.feature.common.AsyncMessage
import com.sydra.app.feature.common.BlackButton
import com.sydra.app.feature.common.CommerceTopBar
import com.sydra.app.feature.common.SydraAccent
import com.sydra.app.feature.common.SydraCanvas
import com.sydra.app.feature.common.SydraInk
import com.sydra.app.feature.common.SydraLine
import com.sydra.app.feature.common.SydraMuted
import com.sydra.app.feature.common.formatTimestamp

@Composable
fun LogisticsScreen(
    state: LogisticsUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar("查看物流", onBack) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {
                LogisticsUiState.Loading -> AsyncMessage("", "", loading = true)
                is LogisticsUiState.Error -> AsyncMessage(
                    title = "物流加载失败",
                    description = state.message,
                    onRetry = onRefresh
                )
                is LogisticsUiState.Content -> LogisticsContent(state.logistics, onRefresh)
            }
        }
    }
}

@Composable
private fun LogisticsContent(logistics: Logistics, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 150.dp, bottom = 28.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = logistics.status,
                        color = SydraInk,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Text(logistics.carrier ?: "等待承运商", color = SydraMuted, fontSize = 12.sp)
                }
                Text(
                    text = logistics.trackingNo?.let { "运单号 $it" } ?: "商家已发货，暂无运单号",
                    modifier = Modifier.padding(top = 8.dp),
                    color = SydraMuted,
                    fontSize = 12.sp
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp), color = SydraLine)
                if (logistics.events.isEmpty()) {
                    Text("商家已发货，物流轨迹稍后更新", color = SydraMuted, fontSize = 13.sp)
                } else {
                    logistics.events.forEachIndexed { index, event ->
                        LogisticsTimelineRow(event, showLine = index != logistics.events.lastIndex)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp), color = SydraLine)
                Text("送至", color = SydraInk, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(
                    logistics.recipientMasked,
                    modifier = Modifier.padding(top = 7.dp),
                    color = SydraMuted,
                    fontSize = 12.sp
                )
                BlackButton(
                    text = "刷新物流",
                    onClick = onRefresh,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 22.dp)
                )
            }
        }
    }
}

@Composable
private fun LogisticsTimelineRow(event: LogisticsEvent, showLine: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(if (event.isCurrent) 10.dp else 7.dp)
                    .background(if (event.isCurrent) SydraAccent else Color(0xFFA9A9A9), CircleShape)
            )
            if (showLine) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(72.dp)
                        .background(SydraLine)
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, bottom = if (showLine) 10.dp else 0.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(2.dp),
                color = if (event.isCurrent) Color(0xFFFDEEF1) else Color(0xFFF0F4F5)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
                    Text(
                        text = event.description,
                        color = if (event.isCurrent) SydraAccent else SydraInk,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${event.location} · ${formatTimestamp(event.occurredAt)}",
                        modifier = Modifier.padding(top = 4.dp),
                        color = SydraMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
