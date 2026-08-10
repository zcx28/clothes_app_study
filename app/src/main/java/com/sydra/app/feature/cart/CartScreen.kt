package com.sydra.app.feature.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.feature.catalog.ShopHeader

@Composable
fun CartScreen(
    items: List<CartLine>,
    onToggleSelection: (String) -> Unit,
    onSetAllSelected: (Boolean) -> Unit,
    onIncreaseQuantity: (String) -> Unit,
    onDecreaseQuantity: (String) -> Unit,
    onUpdateSize: (String, String) -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editingItemId by remember { mutableStateOf<String?>(null) }
    val editingItem = items.firstOrNull { it.id == editingItemId }
    val allSelected = items.isNotEmpty() && items.all { it.selected }
    val selectedItems = items.filter { it.selected }
    val selectedTotalCents = selectedItems.sumOf { it.priceCents * it.quantity }
    val selectedQuantity = selectedItems.sumOf { it.quantity }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ShopHeader(showSearch = false)
        if (items.isEmpty()) {
            EmptyCart(onContinueShopping = onContinueShopping)
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 4.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        CartLineRow(
                            item = item,
                            onToggleSelection = { onToggleSelection(item.id) },
                            onIncreaseQuantity = { onIncreaseQuantity(item.id) },
                            onDecreaseQuantity = { onDecreaseQuantity(item.id) },
                            onEditSize = { editingItemId = item.id }
                        )
                    }
                }
                CartSummaryBar(
                    allSelected = allSelected,
                    totalCents = selectedTotalCents,
                    selectedQuantity = selectedQuantity,
                    onToggleAll = { onSetAllSelected(!allSelected) },
                    onCheckout = {}
                )
            }
        }
    }

    if (editingItem != null) {
        SizeEditorDialog(
            item = editingItem,
            onSizeSelected = { size -> onUpdateSize(editingItem.id, size) },
            onDismiss = { editingItemId = null }
        )
    }
}

@Composable
private fun CartLineRow(
    item: CartLine,
    onToggleSelection: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onEditSize: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(134.dp)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.selected,
                onCheckedChange = { onToggleSelection() },
                modifier = Modifier.size(36.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Transparent,
                    checkmarkColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = item.title,
                modifier = Modifier.size(width = 72.dp, height = 102.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 4.dp, bottom = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.title,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        color = Color(0xFF555555),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (item.selected) {
                        TextButton(
                            onClick = onEditSize,
                            modifier = Modifier
                                .height(28.dp)
                                .padding(start = 4.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text("编辑", fontSize = 11.sp, color = Color(0xFF555555))
                        }
                    }
                }
                Text(
                    text = item.detail,
                    modifier = Modifier.padding(top = 3.dp),
                    fontSize = 11.sp,
                    color = Color(0xFF777777),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.priceLabel,
                        fontSize = 12.sp,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    QuantityStepper(
                        quantity = item.quantity,
                        size = item.size,
                        onIncrease = onIncreaseQuantity,
                        onDecrease = onDecreaseQuantity,
                        onEditSize = onEditSize
                    )
                }
            }
        }
        HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
    }
}

@Composable
private fun QuantityStepper(
    quantity: Int,
    size: String,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onEditSize: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        QuantityButton(
            label = "−",
            enabled = quantity > 1,
            onClick = onDecrease
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .clickable(
                    role = Role.Button,
                    onClickLabel = "编辑尺码 $size",
                    onClick = onEditSize
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = size, fontSize = 11.sp, color = Color(0xFF444444))
        }
        QuantityButton(
            label = "+",
            enabled = true,
            onClick = onIncrease
        )
    }
}

@Composable
private fun QuantityButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClickLabel = if (label == "+") "增加数量" else "减少数量",
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color(0xFFF7F7F7)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = label, fontSize = 15.sp, color = Color(0xFF777777))
        }
    }
}

@Composable
private fun CartSummaryBar(
    allSelected: Boolean,
    totalCents: Int,
    selectedQuantity: Int,
    onToggleAll: () -> Unit,
    onCheckout: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(width = 1.dp, color = Color(0xFFF0F0F0))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .width(90.dp)
                .clickable(
                    role = Role.Checkbox,
                    onClickLabel = if (allSelected) "取消全选" else "全选",
                    onClick = onToggleAll
                )
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = allSelected,
                onCheckedChange = { onToggleAll() },
                modifier = Modifier.size(34.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Transparent,
                    checkmarkColor = Color.White
                )
            )
            Text("全选", fontSize = 12.sp, color = Color(0xFF555555))
        }
        Text(
            text = "总计 ${formatCents(totalCents)}",
            modifier = Modifier.weight(1f),
            fontSize = 12.sp,
            color = Color(0xFF444444)
        )
        Button(
            onClick = onCheckout,
            modifier = Modifier
                .width(92.dp)
                .fillMaxHeight(),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("结算 ($selectedQuantity)", fontSize = 12.sp)
        }
    }
}

@Composable
private fun SizeEditorDialog(
    item: CartLine,
    onSizeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("修改尺码") },
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("S", "M", "L").forEach { size ->
                    SizeOption(
                        size = size,
                        selected = item.size == size,
                        onClick = { onSizeSelected(size) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("完成")
            }
        }
    )
}

@Composable
private fun SizeOption(
    size: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 54.dp, height = 40.dp)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) Color.Black else Color(0xFFBDBDBD)
            )
            .clickable(
                role = Role.RadioButton,
                onClickLabel = "选择尺码 $size",
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = size,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun EmptyCart(onContinueShopping: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 68.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text("未添加组件", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "No components added",
            modifier = Modifier.padding(top = 12.dp),
            fontSize = 16.sp,
            color = Color(0xFF999999),
            letterSpacing = 1.sp
        )
        OutlinedButton(
            onClick = onContinueShopping,
            modifier = Modifier.padding(top = 28.dp)
        ) {
            Text("继续选购")
        }
    }
}

private fun formatCents(cents: Int): String =
    "¥${cents / 100}.${(cents % 100).toString().padStart(2, '0')}"
