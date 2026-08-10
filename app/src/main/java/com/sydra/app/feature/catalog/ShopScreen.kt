package com.sydra.app.feature.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sydra.app.R
import com.sydra.app.navigation.SydraRoute

@Composable
fun ShopModeSelectionScreen(
    onConfiguratorClick: () -> Unit,
    onProductsListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ShopHeader()
        ModeCard(
            imageRes = R.drawable.shop_configurator,
            title = "CONFIGURATOR",
            onClick = onConfiguratorClick,
            modifier = Modifier.weight(1f)
        )
        ModeCard(
            imageRes = R.drawable.shop_products_list,
            title = "PRODUCTS LIST",
            onClick = onProductsListClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
internal fun ShopHeader(
    onBackClick: (() -> Unit)? = null,
    showSearch: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (showSearch || onBackClick != null) {
            IconButton(
                onClick = { onBackClick?.invoke() },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = if (onBackClick == null) {
                        Icons.Outlined.Search
                    } else {
                        Icons.Outlined.ArrowBack
                    },
                    contentDescription = if (onBackClick == null) "搜索" else "返回"
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.sydra_wordmark),
            contentDescription = "SYDRA",
            modifier = Modifier
                .align(Alignment.Center)
                .width(112.dp),
            colorFilter = ColorFilter.tint(Color.Black)
        )
    }
}

@Composable
private fun ModeCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClickLabel = "进入 $title",
                onClick = onClick
            )
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "MODE",
                modifier = Modifier.padding(top = 2.dp),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(84.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
fun CategorySelectionScreen(
    mode: SydraRoute.ShopMode,
    onCategoryClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ShopHeader(onBackClick = onBackClick)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            CategoryModeTabs()
            CategorySeriesList(mode = mode, onCategoryClick = onCategoryClick)
        }
    }
}

@Composable
private fun CategoryModeTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "WEARABLE",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "ARTEFACTS",
            modifier = Modifier.padding(start = 28.dp),
            color = Color(0xFFB8B8B8),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(top = 8.dp)
            .width(96.dp),
        thickness = 2.dp,
        color = Color.Black
    )
}

@Composable
private fun CategorySeriesList(
    mode: SydraRoute.ShopMode,
    onCategoryClick: (String) -> Unit
) {
    val series = when (mode) {
        SydraRoute.ShopMode.CONFIGURATOR -> listOf(
            CategorySeries("ROUTINE", "TOPS", "内搭上衣 / トップス"),
            CategorySeries("ACT", "KNITWEAR & TOPS", "针织上衣 / ニットウェアとトップス"),
            CategorySeries("FORM", "JACKETS & COATS", "夹克与外套 / ジャケット&コート"),
            CategorySeries("EVENT", "SUITING", "正装 / フォーマルウェア"),
            CategorySeries("AVANT", "CASUAL BOTTOMS", "休闲下装 / カジュアルボトムス")
        )
        SydraRoute.ShopMode.PRODUCTS_LIST -> listOf(
            CategorySeries("ACT", "KNITWEAR & TOPS", "针织上衣 / ニットウェアとトップス"),
            CategorySeries("EVENT", "SUITING", "正装 / フォーマルウェア"),
            CategorySeries("AVANT", "CASUAL BOTTOMS", "休闲下装 / カジュアルボトムス"),
            CategorySeries("FORM", "JACKETS & COATS", "夹克与外套 / ジャケット&コート"),
            CategorySeries("CORE", "ESSENTIALS", "基础打底 / エッセンシャル")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 24.dp)
    ) {
        series.forEach { item ->
            if (mode == SydraRoute.ShopMode.CONFIGURATOR) {
                ConfiguratorSeriesRow(
                    item = item,
                    onClick = { onCategoryClick(item.title.lowercase()) }
                )
            } else {
                CategorySeriesCard(
                    item = item,
                    onClick = { onCategoryClick(item.title.lowercase()) }
                )
            }
        }
    }
}

private data class CategorySeries(
    val title: String,
    val englishSubtitle: String,
    val multilingualSubtitle: String
)

@Composable
private fun ConfiguratorSeriesRow(
    item: CategorySeries,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClickLabel = "进入 ${item.title}",
                onClick = onClick
            )
            .padding(top = 26.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "${item.englishSubtitle} / ${item.multilingualSubtitle}",
                modifier = Modifier.padding(top = 2.dp),
                fontSize = 12.sp
            )
        }
        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "选择 ${item.title}",
            modifier = Modifier.size(28.dp),
            tint = Color.Black
        )
    }
}

@Composable
private fun CategorySeriesCard(
    item: CategorySeries,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .border(width = 1.dp, color = Color(0xFF2B2B2B))
            .clickable(
                role = Role.Button,
                onClickLabel = "进入 ${item.title}",
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column {
            Text(
                text = item.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Text(
                text = item.englishSubtitle,
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 12.sp,
                letterSpacing = 0.8.sp
            )
            Text(
                text = item.multilingualSubtitle,
                modifier = Modifier.padding(top = 2.dp),
                color = Color(0xFF555555),
                fontSize = 11.sp
            )
        }
    }
}
