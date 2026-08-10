package com.sydra.app.feature.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.feature.catalog.ShopHeader

@Composable
fun CartScreen(
    items: List<CartLine>,
    onRemove: (String) -> Unit,
    onContinueShopping: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ShopHeader()
        if (items.isEmpty()) {
            EmptyCart(onContinueShopping = onContinueShopping)
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "购物车",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        CartLineRow(item = item, onRemove = { onRemove(item.id) })
                    }
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("合计", fontSize = 12.sp, color = Color(0xFF666666))
                        Text(
                            text = formatCents(items.sumOf { it.priceCents * it.quantity }),
                            modifier = Modifier.padding(top = 2.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("去结算")
                    }
                }
            }
        }
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

@Composable
private fun CartLineRow(item: CartLine, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.imageRes),
            contentDescription = item.title,
            modifier = Modifier
                .size(86.dp)
                .border(1.dp, Color(0xFFE1E1E1)),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        ) {
            Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                text = item.detail,
                modifier = Modifier.padding(top = 3.dp),
                fontSize = 11.sp,
                color = Color(0xFF666666)
            )
            Text(
                text = "尺码 ${item.size} · 数量 ${item.quantity}",
                modifier = Modifier.padding(top = 3.dp),
                fontSize = 11.sp,
                color = Color(0xFF666666)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(item.priceLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            OutlinedButton(
                onClick = onRemove,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .height(32.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
            ) {
                Text("移除", fontSize = 11.sp)
            }
        }
    }
}

private fun formatCents(cents: Int): String =
    "¥ ${cents / 100}.${(cents % 100).toString().padStart(2, '0')}"
