package com.sydra.app.feature.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.domain.model.Order
import com.sydra.app.domain.model.OrderAction
import com.sydra.app.domain.model.OrderStatus
import com.sydra.app.feature.common.AsyncMessage
import com.sydra.app.feature.common.BlackButton
import com.sydra.app.feature.common.CommerceTopBar
import com.sydra.app.feature.common.OrderProductSnapshot
import com.sydra.app.feature.common.SydraAccent
import com.sydra.app.feature.common.SydraCanvas
import com.sydra.app.feature.common.SydraInk
import com.sydra.app.feature.common.SydraLine
import com.sydra.app.feature.common.SydraMuted
import com.sydra.app.feature.common.SydraWordmark
import com.sydra.app.feature.common.formatMoney
import com.sydra.app.feature.common.formatTimestamp

@Composable
fun OrderListScreen(
    state: OrderListUiState,
    onBack: () -> Unit,
    onStatusSelected: (OrderStatus?) -> Unit,
    onRefresh: () -> Unit,
    onOrderClick: (String) -> Unit,
    onTrack: (String) -> Unit,
    onReturn: (String, String) -> Unit,
    onChangeAddress: () -> Unit,
    onUnavailableAction: (String) -> Unit,
    onRequestCancel: (String) -> Unit,
    onDismissCancel: () -> Unit,
    onConfirmCancel: () -> Unit,
    onMessageShown: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            onMessageShown()
        }
    }

    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar("购买订单", onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OrderStatusTabs(state.selectedStatus, onStatusSelected)
            when (val load = state.loadState) {
                OrderLoadState.Loading -> AsyncMessage("", "", loading = true)
                OrderLoadState.Empty -> AsyncMessage(
                    title = emptyTitle(state.selectedStatus),
                    description = "订单产生后会显示在这里"
                )
                is OrderLoadState.Error -> AsyncMessage(
                    title = "订单加载失败",
                    description = load.message,
                    onRetry = onRefresh
                )
                is OrderLoadState.Content -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(load.orders, key = Order::id) { order ->
                        OrderCard(
                            order = order,
                            onOpen = { onOrderClick(order.id) },
                            onAction = { action ->
                                when (action) {
                                    OrderAction.VIEW_DETAILS -> onOrderClick(order.id)
                                    OrderAction.CANCEL -> onRequestCancel(order.id)
                                    OrderAction.TRACK -> onTrack(order.id)
                                    OrderAction.RETURN -> order.items.firstOrNull()?.let {
                                        onReturn(order.id, it.itemId)
                                    }
                                    OrderAction.CHANGE_ADDRESS -> onChangeAddress()
                                    OrderAction.PAY -> onUnavailableAction("支付能力将在真实支付接口接入后开放")
                                    OrderAction.SUPPORT -> onUnavailableAction("客服入口将在客服模块接入后开放")
                                    OrderAction.CONFIRM_RECEIPT -> onUnavailableAction("确认收货需服务端资格校验")
                                    OrderAction.SUPPLEMENT_EVIDENCE -> onUnavailableAction("请在售后详情补充凭证")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    CancelOrderDialog(
        visible = state.cancelTargetOrderId != null,
        submitting = state.isCancelling,
        onDismiss = onDismissCancel,
        onConfirm = onConfirmCancel
    )
}

@Composable
fun OrderDetailScreen(
    detailState: OrderDetailUiState,
    orderState: OrderListUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onTrack: (String) -> Unit,
    onReturn: (String, String) -> Unit,
    onChangeAddress: () -> Unit,
    onUnavailableAction: (String) -> Unit,
    onRequestCancel: (String) -> Unit,
    onDismissCancel: () -> Unit,
    onConfirmCancel: () -> Unit,
    onCancelled: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(orderState.message) {
        orderState.message?.let { snackbarHostState.showSnackbar(it) }
    }
    LaunchedEffect(orderState.cancelledOrderId) {
        if (orderState.cancelledOrderId != null) onCancelled()
    }
    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar("订单详情", onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (detailState) {
                OrderDetailUiState.Loading -> AsyncMessage("", "", loading = true)
                is OrderDetailUiState.Error -> AsyncMessage(
                    title = "订单详情加载失败",
                    description = detailState.message,
                    onRetry = onRefresh
                )
                is OrderDetailUiState.Content -> OrderDetailContent(
                    order = detailState.order,
                    onAction = { action ->
                        when (action) {
                            OrderAction.CANCEL -> onRequestCancel(detailState.order.id)
                            OrderAction.TRACK -> onTrack(detailState.order.id)
                            OrderAction.RETURN -> detailState.order.items.firstOrNull()?.let {
                                onReturn(detailState.order.id, it.itemId)
                            }
                            OrderAction.CHANGE_ADDRESS -> onChangeAddress()
                            OrderAction.PAY -> onUnavailableAction("支付能力将在真实支付接口接入后开放")
                            OrderAction.SUPPORT -> onUnavailableAction("客服入口将在客服模块接入后开放")
                            OrderAction.CONFIRM_RECEIPT -> onUnavailableAction("确认收货需服务端资格校验")
                            OrderAction.SUPPLEMENT_EVIDENCE -> onUnavailableAction("请在售后详情补充凭证")
                            OrderAction.VIEW_DETAILS -> Unit
                        }
                    }
                )
            }
        }
    }
    CancelOrderDialog(
        visible = orderState.cancelTargetOrderId != null,
        submitting = orderState.isCancelling,
        onDismiss = onDismissCancel,
        onConfirm = onConfirmCancel
    )
}

@Composable
private fun OrderStatusTabs(
    selected: OrderStatus?,
    onSelected: (OrderStatus?) -> Unit
) {
    val tabs = listOf(
        null to "全部",
        OrderStatus.PENDING_PAYMENT to "待付款",
        OrderStatus.PENDING_SHIPMENT to "待发货",
        OrderStatus.PENDING_RECEIPT to "待收货",
        OrderStatus.COMPLETED to "已完成",
        OrderStatus.AFTER_SALE to "售后"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .background(Color.White)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        tabs.forEach { (status, label) ->
            Column(
                modifier = Modifier
                    .clickable { onSelected(status) }
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = label,
                    color = if (selected == status) SydraInk else SydraMuted,
                    fontSize = 13.sp,
                    fontWeight = if (selected == status) FontWeight.Bold else FontWeight.Normal
                )
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(26.dp)
                        .height(2.dp)
                        .background(if (selected == status) SydraInk else Color.Transparent)
                )
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onOpen: () -> Unit,
    onAction: (OrderAction) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
        shape = RoundedCornerShape(10.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("订单编号：${order.displayNo}", color = SydraMuted, fontSize = 10.sp)
                Spacer(Modifier.weight(1f))
                Text(order.status.label(), color = SydraInk, fontSize = 12.sp)
            }
            HorizontalDivider(modifier = Modifier.padding(top = 12.dp), color = SydraLine)
            Row(
                modifier = Modifier.padding(top = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SydraWordmark()
                Spacer(Modifier.weight(1f))
                Text("共 ${order.items.sumOf { it.quantity }} 件", color = SydraInk, fontSize = 11.sp)
            }
            order.items.forEach { OrderProductSnapshot(it) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("实付款 ", color = SydraMuted, fontSize = 11.sp)
                Text(formatMoney(order.totalAmountCents), color = SydraAccent, fontSize = 17.sp)
            }
            OrderActionRow(order, onAction)
        }
    }
}

@Composable
private fun OrderDetailContent(order: Order, onAction: (OrderAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SydraWordmark()
                    Spacer(Modifier.weight(1f))
                    Text(order.status.label(), color = SydraInk, fontSize = 12.sp)
                }
                order.items.forEach { OrderProductSnapshot(it) }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoLine("实付款", formatMoney(order.totalAmountCents), strong = true)
                InfoLine("订单编号", order.displayNo)
                InfoLine("交易方式", order.paymentMethod ?: "待付款")
                InfoLine("创建时间", formatTimestamp(order.createdAt))
                InfoLine("付款时间", formatTimestamp(order.paidAt))
                order.addressSnapshot?.let {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SydraLine)
                    Text("收货信息", color = SydraInk, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${it.recipient}  ${it.maskedPhone}\n${it.region.joinToString(" ")} ${it.detail}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = SydraMuted,
                        fontSize = 12.sp,
                        lineHeight = 19.sp
                    )
                }
                OrderActionRow(order, onAction)
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String, strong: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = SydraMuted, fontSize = 12.sp)
        Spacer(Modifier.weight(1f))
        Text(
            value,
            color = SydraInk,
            fontSize = 12.sp,
            fontWeight = if (strong) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun OrderActionRow(order: Order, onAction: (OrderAction) -> Unit) {
    val actions = order.availableActions
        .filterNot { it == OrderAction.VIEW_DETAILS }
        .take(3)
    if (actions.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.End
    ) {
        actions.forEachIndexed { index, action ->
            if (index > 0) Spacer(Modifier.width(8.dp))
            if (action == OrderAction.PAY || action == OrderAction.TRACK) {
                BlackButton(
                    text = action.label(),
                    onClick = { onAction(action) },
                    modifier = Modifier.width(94.dp)
                )
            } else {
                OutlinedButton(
                    onClick = { onAction(action) },
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Text(action.label(), color = SydraInk, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun CancelOrderDialog(
    visible: Boolean,
    submitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("取消订单") },
        text = { Text("确认取消当前订单吗？提交后将刷新订单状态。") },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !submitting) {
                Text(if (submitting) "提交中…" else "确认取消", color = SydraAccent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !submitting) {
                Text("保留订单", color = SydraInk)
            }
        }
    )
}

private fun OrderStatus.label(): String = when (this) {
    OrderStatus.PENDING_PAYMENT -> "待付款"
    OrderStatus.PENDING_SHIPMENT -> "待发货"
    OrderStatus.PENDING_RECEIPT -> "待收货"
    OrderStatus.COMPLETED -> "已完成"
    OrderStatus.AFTER_SALE -> "售后处理中"
}

private fun OrderAction.label(): String = when (this) {
    OrderAction.VIEW_DETAILS -> "查看详情"
    OrderAction.PAY -> "付款"
    OrderAction.CANCEL -> "取消订单"
    OrderAction.CHANGE_ADDRESS -> "修改地址"
    OrderAction.TRACK -> "查看物流"
    OrderAction.RETURN -> "申请售后"
    OrderAction.SUPPORT -> "联系客服"
    OrderAction.CONFIRM_RECEIPT -> "确认收货"
    OrderAction.SUPPLEMENT_EVIDENCE -> "补充凭证"
}

private fun emptyTitle(status: OrderStatus?): String = when (status) {
    null -> "暂无订单"
    OrderStatus.PENDING_PAYMENT -> "暂无待付款订单"
    OrderStatus.PENDING_SHIPMENT -> "暂无待发货订单"
    OrderStatus.PENDING_RECEIPT -> "暂无待收货订单"
    OrderStatus.COMPLETED -> "暂无已完成订单"
    OrderStatus.AFTER_SALE -> "暂无售后订单"
}
