package com.sydra.app.feature.cart

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.sydra.app.feature.catalog.CatalogProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartLine(
    val id: String,
    val title: String,
    val detail: String,
    val size: String,
    val priceCents: Int,
    @DrawableRes val imageRes: Int,
    val quantity: Int = 1
) {
    val priceLabel: String
        get() = "¥ ${priceCents / 100}.${(priceCents % 100).toString().padStart(2, '0')}"
}

class CartViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<CartLine>>(emptyList())
    val items: StateFlow<List<CartLine>> = _items.asStateFlow()

    fun addProduct(product: CatalogProduct, size: String) {
        val lineId = "${product.id}-$size"
        val existing = _items.value.firstOrNull { it.id == lineId }
        _items.value = if (existing == null) {
            _items.value + CartLine(
                id = lineId,
                title = product.title,
                detail = "${product.subtitle} · ${product.sku}",
                size = size,
                priceCents = product.priceCents,
                imageRes = product.imageRes
            )
        } else {
            _items.value.map {
                if (it.id == lineId) it.copy(quantity = it.quantity + 1) else it
            }
        }
    }

    fun addConfiguration(categoryId: String, size: String) {
        _items.value = _items.value + CartLine(
            id = "configuration-$categoryId-${_items.value.size}",
            title = "${categoryId.uppercase()} CONFIGURATION",
            detail = "MODULAR SET · 221026",
            size = size,
            priceCents = 88000,
            imageRes = com.sydra.app.R.drawable.act_vest
        )
    }

    fun remove(lineId: String) {
        _items.value = _items.value.filterNot { it.id == lineId }
    }

    fun totalCents(): Int = _items.value.sumOf { it.priceCents * it.quantity }
}
