package com.sydra.app.feature.address

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.domain.model.Address
import com.sydra.app.domain.model.AddressDraft
import com.sydra.app.feature.common.AsyncMessage
import com.sydra.app.feature.common.BlackButton
import com.sydra.app.feature.common.CommerceTopBar
import com.sydra.app.feature.common.SydraCanvas
import com.sydra.app.feature.common.SydraInk
import com.sydra.app.feature.common.SydraLine
import com.sydra.app.feature.common.SydraMuted

@Composable
fun AddressListScreen(
    state: AddressUiState,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    onSetDefault: (String) -> Unit,
    onRequestDelete: (String) -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
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
        topBar = { CommerceTopBar("收货地址", onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BlackButton(
                text = "添加地址",
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 38.dp, vertical = 16.dp)
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> AsyncMessage("", "", loading = true)
                state.loadError != null -> AsyncMessage(
                    title = "地址加载失败",
                    description = state.loadError,
                    onRetry = onRefresh
                )
                state.addresses.isEmpty() -> AsyncMessage("暂无收货地址", "添加一个地址用于收货")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.addresses, key = Address::id) { address ->
                        AddressCard(
                            address = address,
                            onEdit = { onEdit(address.id) },
                            onDelete = { onRequestDelete(address.id) },
                            onSetDefault = { onSetDefault(address.id) }
                        )
                    }
                }
            }
        }
    }
    if (state.deleteTargetId != null) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            title = { Text("删除地址") },
            text = { Text("确认删除这条收货地址吗？") },
            confirmButton = {
                TextButton(onClick = onConfirmDelete, enabled = !state.isMutating) {
                    Text(if (state.isMutating) "处理中…" else "删除", color = Color(0xFFC62828))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDelete, enabled = !state.isMutating) {
                    Text("取消", color = SydraInk)
                }
            }
        )
    }
}

@Composable
fun AddressFormScreen(
    address: Address?,
    state: AddressUiState,
    onBack: () -> Unit,
    onSave: (AddressDraft) -> Unit
) {
    var recipient by rememberSaveable(address?.id) { mutableStateOf(address?.recipient.orEmpty()) }
    var phone by rememberSaveable(address?.id) { mutableStateOf(address?.phone.orEmpty()) }
    var province by rememberSaveable(address?.id) { mutableStateOf(address?.region?.getOrNull(0).orEmpty()) }
    var city by rememberSaveable(address?.id) { mutableStateOf(address?.region?.getOrNull(1).orEmpty()) }
    var district by rememberSaveable(address?.id) { mutableStateOf(address?.region?.getOrNull(2).orEmpty()) }
    var detail by rememberSaveable(address?.id) { mutableStateOf(address?.detail.orEmpty()) }
    var tag by rememberSaveable(address?.id) { mutableStateOf(address?.tag ?: "默认") }
    var isDefault by rememberSaveable(address?.id) { mutableStateOf(address?.isDefault ?: false) }

    Scaffold(
        containerColor = SydraCanvas,
        topBar = { CommerceTopBar(if (address == null) "添加地址" else "编辑地址", onBack) },
        bottomBar = {
            BlackButton(
                text = if (state.isSaving) "保存中…" else "保存地址",
                enabled = !state.isSaving,
                onClick = {
                    onSave(
                        AddressDraft(
                            id = address?.id,
                            recipient = recipient,
                            phone = phone,
                            region = listOf(province, city, district),
                            detail = detail,
                            tag = tag,
                            isDefault = isDefault
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 38.dp, vertical = 16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = RoundedCornerShape(10.dp), color = Color.White) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AddressField("联系人", recipient, { recipient = it }, state.fieldErrors["recipient"])
                    AddressField("手机号", phone, { phone = it }, state.fieldErrors["phone"])
                }
            }
            Surface(shape = RoundedCornerShape(10.dp), color = Color.White) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("所在地区", color = SydraInk, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactRegionField("省", province, { province = it }, Modifier.weight(1f))
                        CompactRegionField("市", city, { city = it }, Modifier.weight(1f))
                        CompactRegionField("区", district, { district = it }, Modifier.weight(1f))
                    }
                    state.fieldErrors["region"]?.let { FieldError(it) }
                    AddressField("详细地址", detail, { detail = it }, state.fieldErrors["detail"])
                    Text("标签", color = SydraInk, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        listOf("默认", "公司", "商家").forEach { option ->
                            FilterChip(
                                selected = tag == option,
                                onClick = { tag = option },
                                label = { Text(option) },
                                shape = RoundedCornerShape(2.dp)
                            )
                        }
                    }
                }
            }
            Surface(shape = RoundedCornerShape(10.dp), color = Color.White) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("设为默认收货地址", color = SydraInk, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "下单时优先定位至默认地址",
                            modifier = Modifier.padding(top = 6.dp),
                            color = SydraMuted,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = isDefault,
                        onCheckedChange = { isDefault = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color.Black)
                    )
                }
            }
            state.message?.let { FieldError(it) }
        }
    }
}

@Composable
private fun AddressCard(
    address: Address,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Surface(shape = RoundedCornerShape(10.dp), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onEdit)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(address.region.joinToString(" "), color = SydraMuted, fontSize = 11.sp)
                Spacer(Modifier.weight(1f))
                if (address.isDefault) {
                    Text(
                        "默认",
                        modifier = Modifier.background(Color.Black).padding(horizontal = 7.dp, vertical = 3.dp),
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = "删除地址", tint = SydraMuted)
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Outlined.Edit, contentDescription = "编辑地址", tint = SydraMuted)
                }
            }
            Text(address.detail, color = SydraInk, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.padding(top = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(address.recipient, color = SydraMuted, fontSize = 12.sp)
                Text(address.phone, modifier = Modifier.padding(start = 14.dp), color = SydraMuted, fontSize = 12.sp)
                Spacer(Modifier.weight(1f))
                if (!address.isDefault) {
                    Text(
                        "设为默认",
                        modifier = Modifier.clickable(onClick = onSetDefault).padding(8.dp),
                        color = SydraInk,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        singleLine = true,
        isError = error != null,
        supportingText = error?.let { message -> { Text(message) } }
    )
}

@Composable
private fun CompactRegionField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = modifier,
        shape = RoundedCornerShape(2.dp),
        singleLine = true
    )
}

@Composable
private fun FieldError(message: String) {
    Text(message, color = Color(0xFFC62828), fontSize = 11.sp)
}
