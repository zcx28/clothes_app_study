package com.sydra.app.feature.aftersale

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.BackHandler
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.domain.model.AfterSaleRequest
import com.sydra.app.domain.model.AfterSaleStatus
import com.sydra.app.feature.common.AsyncMessage
import com.sydra.app.feature.common.BlackButton
import com.sydra.app.feature.common.CommerceTopBar
import com.sydra.app.feature.common.SydraCanvas
import com.sydra.app.feature.common.SydraInk
import com.sydra.app.feature.common.SydraLine
import com.sydra.app.feature.common.SydraMuted
import com.sydra.app.feature.common.SydraWordmark
import com.sydra.app.feature.common.formatMoney
import com.sydra.app.feature.common.formatTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AfterSaleListScreen(
    state: AfterSaleListUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onRequestClick: (String) -> Unit
) {
    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar("售后", onBack) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                AfterSaleListUiState.Loading -> AsyncMessage("", "", loading = true)
                AfterSaleListUiState.Empty -> AsyncMessage("暂无售后申请", "提交退货或售后申请后会显示在这里")
                is AfterSaleListUiState.Error -> AsyncMessage(
                    title = "售后列表加载失败",
                    description = state.message,
                    onRetry = onRefresh
                )
                is AfterSaleListUiState.Content -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.requests, key = AfterSaleRequest::id) { request ->
                        AfterSaleCard(request, onClick = { onRequestClick(request.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun AfterSaleDetailScreen(
    state: AfterSaleDetailUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar("售后详情", onBack) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                AfterSaleDetailUiState.Loading -> AsyncMessage("", "", loading = true)
                is AfterSaleDetailUiState.Error -> AsyncMessage(
                    title = "售后详情加载失败",
                    description = state.message,
                    onRetry = onRefresh
                )
                is AfterSaleDetailUiState.Content -> AfterSaleDetailContent(state.request)
            }
        }
    }
}

@Composable
fun ReturnRequestScreen(
    state: ReturnRequestUiState,
    onBack: () -> Unit,
    onConfirmItem: () -> Unit,
    onSelectReason: (String) -> Unit,
    onContinueReason: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddEvidence: (String) -> Unit,
    onRemoveEvidence: (String) -> Unit,
    onSubmit: () -> Unit,
    onSubmitted: (String) -> Unit
) {
    BackHandler { onBack() }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { onAddEvidence(it.toString()) }
    }
    LaunchedEffect(state.submittedRequestId) {
        state.submittedRequestId?.let(onSubmitted)
    }
    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar("退货", onBack) },
        bottomBar = {
            BlackButton(
                text = when {
                    state.isSubmitting -> "提交中…"
                    state.step == ReturnStep.CONFIRM_ITEM -> "下一步"
                    state.step == ReturnStep.SELECT_REASON && state.reason == ReturnRequestViewModel.OTHER_REASON -> "下一步"
                    else -> "提交"
                },
                enabled = !state.isSubmitting,
                onClick = when (state.step) {
                    ReturnStep.CONFIRM_ITEM -> onConfirmItem
                    ReturnStep.SELECT_REASON -> onContinueReason
                    ReturnStep.EVIDENCE -> onSubmit
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 44.dp, vertical = 16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ReturnStepIndicator(state.step)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color.White
            ) {
                when (state.step) {
                    ReturnStep.CONFIRM_ITEM -> ConfirmReturnItem(state)
                    ReturnStep.SELECT_REASON -> ReturnReasonList(state.reason, onSelectReason)
                    ReturnStep.EVIDENCE -> ReturnEvidenceForm(
                        state = state,
                        onDescriptionChange = onDescriptionChange,
                        onPickEvidence = {
                            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                        },
                        onRemoveEvidence = onRemoveEvidence
                    )
                }
            }
            state.validationMessage?.let { FormMessage(it) }
            state.submitError?.let { FormMessage("提交失败：$it") }
        }
    }
}

@Composable
private fun AfterSaleCard(request: AfterSaleRequest, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SydraWordmark()
                Spacer(Modifier.weight(1f))
                Text(request.status.label(), color = SydraInk, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = SydraLine)
            Text("申请编号 ${request.id}", color = SydraMuted, fontSize = 11.sp)
            Text(
                request.reason,
                modifier = Modifier.padding(top = 10.dp),
                color = SydraInk,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                Text("退款金额", color = SydraMuted, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                Text(formatMoney(request.amountCents), color = SydraInk, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AfterSaleDetailContent(request: AfterSaleRequest) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(shape = RoundedCornerShape(10.dp), color = Color.White) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SydraWordmark()
                    Spacer(Modifier.weight(1f))
                    Text(request.status.label(), color = SydraInk, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = SydraLine)
                DetailLine("申请编号", request.id)
                DetailLine("订单编号", request.orderId)
                DetailLine("退货原因", request.reason)
                DetailLine("退款金额", formatMoney(request.amountCents))
                request.description?.let { DetailLine("问题描述", it) }
                if (request.evidence.isNotEmpty()) DetailLine("凭证", "${request.evidence.size} 个")
            }
        }
        Surface(shape = RoundedCornerShape(10.dp), color = Color.White) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("处理进度", color = SydraInk, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                if (request.timeline.isEmpty()) {
                    Text("等待官方更新", modifier = Modifier.padding(top = 12.dp), color = SydraMuted, fontSize = 12.sp)
                } else {
                    request.timeline.forEach { event ->
                        Row(modifier = Modifier.padding(top = 16.dp)) {
                            Box(Modifier.padding(top = 5.dp).size(8.dp).background(SydraInk, CircleShape))
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(event.title, color = SydraInk, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text(event.description, modifier = Modifier.padding(top = 4.dp), color = SydraMuted, fontSize = 12.sp)
                                Text(formatTimestamp(event.occurredAt), modifier = Modifier.padding(top = 4.dp), color = SydraMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReturnStepIndicator(step: ReturnStep) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReturnStep.entries.forEachIndexed { index, item ->
            val active = item.ordinal <= step.ordinal
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (active) SydraInk else Color(0xFFD5D5D5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${index + 1}", color = Color.White, fontSize = 11.sp)
            }
            if (index < ReturnStep.entries.lastIndex) {
                Box(
                    Modifier
                        .width(42.dp)
                        .height(1.dp)
                        .background(if (item.ordinal < step.ordinal) SydraInk else SydraLine)
                )
            }
        }
    }
}

@Composable
private fun ConfirmReturnItem(state: ReturnRequestUiState) {
    Column(modifier = Modifier.padding(18.dp)) {
        Text("确认退货订单", color = SydraInk, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text("订单号", modifier = Modifier.padding(top = 22.dp), color = SydraMuted, fontSize = 12.sp)
        Text(state.orderId, modifier = Modifier.padding(top = 6.dp), color = SydraInk, fontSize = 14.sp)
        Text("订单项", modifier = Modifier.padding(top = 18.dp), color = SydraMuted, fontSize = 12.sp)
        Text(state.orderItemId, modifier = Modifier.padding(top = 6.dp), color = SydraInk, fontSize = 14.sp)
        Text(
            "订单和订单项已由上一页安全传入，无需再次手工填写。",
            modifier = Modifier.padding(top = 22.dp),
            color = SydraMuted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ReturnReasonList(selected: String?, onSelect: (String) -> Unit) {
    val reasons = listOf("订单信息拍错（类型、品牌等）", "不想要了", "缺货或错发", ReturnRequestViewModel.OTHER_REASON)
    Column(modifier = Modifier.padding(18.dp)) {
        Text("请选择您的退货原因", color = SydraInk, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        reasons.forEach { reason ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(reason) }
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(reason, modifier = Modifier.weight(1f), color = SydraInk, fontSize = 13.sp)
                RadioButton(selected = selected == reason, onClick = { onSelect(reason) })
            }
        }
    }
}

@Composable
private fun ReturnEvidenceForm(
    state: ReturnRequestUiState,
    onDescriptionChange: (String) -> Unit,
    onPickEvidence: () -> Unit,
    onRemoveEvidence: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("描述问题并上传凭证", color = SydraInk, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(150.dp),
            placeholder = { Text("请描述您遇到的问题") },
            shape = RoundedCornerShape(2.dp),
            maxLines = 6
        )
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .clickable(onClick = onPickEvidence)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.AddAPhoto, contentDescription = null, tint = Color.White)
                    Text("添加凭证", modifier = Modifier.padding(top = 4.dp), color = Color.White, fontSize = 10.sp)
                }
            }
            state.evidenceIds.forEachIndexed { index, uri ->
                EvidenceThumbnail(
                    uri = uri,
                    index = index,
                    onRemove = { onRemoveEvidence(uri) }
                )
            }
        }
        Text(
            "当前是本地 Photo Picker 原型；正式上传的类型、数量和大小以后端规则为准。",
            modifier = Modifier.padding(top = 12.dp),
            color = SydraMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun EvidenceThumbnail(uri: String, index: Int, onRemove: () -> Unit) {
    val resolver = LocalContext.current.contentResolver
    val bitmap by produceState<androidx.compose.ui.graphics.ImageBitmap?>(
        initialValue = null,
        uri,
        resolver
    ) {
        value = withContext(Dispatchers.IO) { loadEvidenceBitmap(resolver, uri) }
    }
    Box(modifier = Modifier.size(74.dp)) {
        if (bitmap == null) {
            Box(
                modifier = Modifier.fillMaxSize().background(SydraLine),
                contentAlignment = Alignment.Center
            ) {
                Text("凭证 ${index + 1}", color = SydraMuted, fontSize = 10.sp)
            }
        } else {
            Image(
                bitmap = bitmap!!,
                contentDescription = "凭证 ${index + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        IconButton(
            onClick = onRemove,
            modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
        ) {
            Icon(
                Icons.Outlined.Close,
                contentDescription = "移除凭证 ${index + 1}",
                tint = Color.White,
                modifier = Modifier.background(Color.Black).padding(3.dp)
            )
        }
    }
}

private fun loadEvidenceBitmap(resolver: ContentResolver, value: String) = runCatching {
    val uri = Uri.parse(value)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        resolver.loadThumbnail(uri, Size(256, 256), null).asImageBitmap()
    } else {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
        var sampleSize = 1
        while (bounds.outWidth / sampleSize > 512 || bounds.outHeight / sampleSize > 512) {
            sampleSize *= 2
        }
        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        resolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)?.asImageBitmap()
        }
    }
}.getOrNull()

@Composable
private fun DetailLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp)) {
        Text(label, color = SydraMuted, fontSize = 12.sp)
        Spacer(Modifier.weight(1f))
        Text(value, color = SydraInk, fontSize = 12.sp)
    }
}

@Composable
private fun FormMessage(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(top = 12.dp, start = 4.dp),
        color = Color(0xFFC62828),
        fontSize = 12.sp
    )
}

private fun AfterSaleStatus.label(): String = when (this) {
    AfterSaleStatus.SUBMITTED -> "待审核"
    AfterSaleStatus.REFUNDING -> "退款中"
    AfterSaleStatus.COMPLETED -> "已完成"
    AfterSaleStatus.REJECTED -> "未通过"
}
