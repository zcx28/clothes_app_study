package com.sydra.app.feature.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.feature.cart.CartLine
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(
    categoryId: String,
    onCategoryClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val products = CatalogRepository.productsFor(categoryId)
    val categoryTitle = categoryId.uppercase()

    Column(modifier = modifier.fillMaxSize()) {
        ShopHeader(onBackClick = onBackClick)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFF232323))
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PRODUCT LIST",
                color = Color.White,
                fontSize = 14.sp,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "搜索商品",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Row(modifier = Modifier.fillMaxSize()) {
            ProductCategoryRail(
                selectedCategoryId = categoryId,
                onCategoryClick = onCategoryClick
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = categoryTitle,
                    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(640.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(products) { product ->
                        ProductTile(product = product, onClick = { onProductClick(product.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCategoryRail(
    selectedCategoryId: String,
    onCategoryClick: (String) -> Unit
) {
    val categories = listOf("act", "event", "avant", "form", "core")
    Column(
        modifier = Modifier
            .width(108.dp)
            .fillMaxSize()
            .background(Color(0xFFD6D7D9))
    ) {
        categories.forEach { category ->
            val selected = category == selectedCategoryId
            Text(
                text = category.uppercase(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        role = Role.Button,
                        onClickLabel = "选择 ${category.uppercase()}",
                        onClick = { onCategoryClick(category) }
                    )
                    .background(if (selected) Color.White else Color.Transparent)
                    .padding(horizontal = 12.dp, vertical = 15.dp),
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                letterSpacing = 0.7.sp
            )
        }
    }
}

@Composable
private fun ProductTile(product: CatalogProduct, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClickLabel = "查看 ${product.title}",
                onClick = onClick
            )
            .padding(bottom = 12.dp)
    ) {
        Image(
            painter = painterResource(product.imageRes),
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = product.sku,
            modifier = Modifier.padding(top = 2.dp),
            fontSize = 11.sp,
            color = Color(0xFF555555)
        )
    }
}

@Composable
fun ProductDetailScreen(
    productId: String,
    onAddToCart: (CatalogProduct, String) -> Unit,
    onBuyNow: (CatalogProduct, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val product = CatalogRepository.productById(productId)
    var selectedSize by rememberSaveable { mutableStateOf("S") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
        ShopHeader(onBackClick = onBackClick)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(product.imageRes),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = product.title,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = product.subtitle,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 12.sp,
                color = Color(0xFF555555)
            )
            Text(
                text = product.sku,
                modifier = Modifier.padding(top = 2.dp),
                fontSize = 12.sp,
                color = Color(0xFF555555)
            )
            Text(
                text = product.priceLabel,
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
            Row(
                modifier = Modifier.padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("S", "M", "L").forEach { size ->
                    SizeChoice(
                        size = size,
                        selected = selectedSize == size,
                        onClick = { selectedSize = size }
                    )
                }
            }
            Text(
                text = "已选尺码：$selectedSize",
                modifier = Modifier.padding(top = 10.dp, bottom = 24.dp),
                fontSize = 12.sp,
                color = Color(0xFF555555)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onAddToCart(product, selectedSize)
                    snackbarScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "已加入购物车",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("加入购物车")
            }
            Button(
                onClick = { onBuyNow(product, selectedSize) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("立即购买")
            }
        }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 96.dp)
        )
    }
}

@Composable
private fun SizeChoice(size: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 54.dp, height = 34.dp)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = Color.Black
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
fun ConfiguratorScreen(
    categoryId: String,
    onAddToCart: (String, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var selectedSize by rememberSaveable { mutableStateOf("S") }
    val heroImage = if (step == 2) {
        com.sydra.app.R.drawable.config_jacket
    } else {
        com.sydra.app.R.drawable.act_vest
    }

    Column(modifier = modifier.fillMaxSize()) {
        ShopHeader(onBackClick = onBackClick)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(heroImage),
                contentDescription = "配置器预览",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp),
                contentScale = ContentScale.Fit
            )
            ConfiguratorSteps(currentStep = step)
            ConfiguratorOptions(
                step = step,
                selectedSize = selectedSize,
                onSizeSelected = { selectedSize = it }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("880.00", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("overview", fontSize = 10.sp, color = Color(0xFF666666))
            }
            if (step > 1) {
                OutlinedButton(
                    onClick = { step -= 1 },
                    modifier = Modifier.size(width = 58.dp, height = 50.dp)
                ) {
                    Icon(Icons.Outlined.ChevronLeft, contentDescription = "上一步")
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            Button(
                onClick = {
                    if (step < 4) step += 1 else onAddToCart(categoryId, selectedSize)
                },
                modifier = Modifier.size(width = if (step == 4) 132.dp else 58.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                if (step == 4) {
                    Text("COMPLETE", fontSize = 12.sp)
                } else {
                    Icon(Icons.Outlined.ChevronRight, contentDescription = "下一步")
                }
            }
        }
    }
}

@Composable
private fun ConfiguratorSteps(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        (1..4).forEach { step ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (step <= currentStep) step.toString() else "+",
                    fontSize = 24.sp,
                    fontWeight = if (step == currentStep) FontWeight.Bold else FontWeight.Normal,
                    color = if (step <= currentStep) Color.Black else Color(0xFFBDBDBD)
                )
                Text(
                    text = "221026",
                    fontSize = 9.sp,
                    color = Color(0xFF777777)
                )
            }
        }
    }
}

@Composable
private fun ConfiguratorOptions(
    step: Int,
    selectedSize: String,
    onSizeSelected: (String) -> Unit
) {
    val options = when (step) {
        1 -> listOf("N/A" to "000000", "ACT VEST" to "221026", "CONNECTOR" to "221026")
        2 -> listOf("N/A" to "000000", "CONNECTOR JACKET" to "221026", "ACT VEST" to "221026")
        3 -> listOf("D60" to "221026", "D90" to "221026", "D120" to "221026")
        else -> listOf("SIZE S" to "S", "SIZE M" to "M", "SIZE L" to "L")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE4E4E4))
            .padding(18.dp)
    ) {
        Text(
            text = "${step}. SELECT COMPONENT",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { (label, value) ->
                val selected = if (step == 4) selectedSize == value else value != "000000" && value != "D120"
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = {
                                if (step == 4 && value in listOf("S", "M", "L")) {
                                    onSizeSelected(value)
                                }
                            }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(if (label == "N/A") Color.White else Color(0xFFE5E5E5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, fontSize = 10.sp, color = Color(0xFF555555))
                    }
                    Text(
                        text = value,
                        modifier = Modifier.padding(top = 6.dp),
                        fontSize = 10.sp
                    )
                    RadioButton(
                        selected = selected,
                        onClick = null,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

fun CatalogProduct.toCartLine(size: String): CartLine = CartLine(
    id = "$id-$size",
    title = title,
    detail = "$subtitle · $sku",
    size = size,
    priceCents = priceCents,
    imageRes = imageRes
)
