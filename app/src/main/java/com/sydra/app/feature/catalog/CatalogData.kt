package com.sydra.app.feature.catalog

import androidx.annotation.DrawableRes
import com.sydra.app.R

data class CatalogProduct(
    val id: String,
    val categoryId: String,
    val title: String,
    val subtitle: String,
    val sku: String,
    val priceCents: Int,
    @DrawableRes val imageRes: Int
) {
    val priceLabel: String
        get() = "¥ ${priceCents / 100}.${(priceCents % 100).toString().padStart(2, '0')}"
}

/**
 * Local catalog substitute used by the UI until the production API is connected.
 * Screens depend on this shape, so a remote repository can replace it later
 * without changing navigation or the visual flow.
 */
object CatalogRepository {
    /**
     * Temporary visual fixtures downloaded from public Pexels photo pages for
     * this prototype. They are local assets so the catalog still works offline;
     * production should replace them with SYDRA-owned product photography.
     */
    private val productBlueprints = listOf(
        "vest" to ("MODULAR VEST" to R.drawable.product_01),
        "blazer" to ("CONNECTOR BLAZER" to R.drawable.product_02),
        "coat" to ("STRUCTURED COAT" to R.drawable.product_03),
        "tailored" to ("TAILORED JACKET" to R.drawable.product_04),
        "black" to ("BLACK JACKET" to R.drawable.product_05),
        "pattern" to ("STATEMENT COAT" to R.drawable.product_06)
    )

    private val categoryDefinitions = listOf(
        "act" to ("ACT" to "KNITWEAR & TOPS"),
        "event" to ("EVENT" to "SUITING"),
        "avant" to ("AVANT" to "CASUAL BOTTOMS"),
        "form" to ("FORM" to "JACKETS & COATS"),
        "core" to ("CORE" to "ESSENTIALS")
    )

    val products: List<CatalogProduct> = categoryDefinitions.flatMapIndexed { categoryIndex, (categoryId, names) ->
        productBlueprints.mapIndexed { productIndex, (slug, product) ->
            val sku = (221026 + categoryIndex * productBlueprints.size + productIndex).toString()
            CatalogProduct(
                id = "$categoryId-$sku-$slug",
                categoryId = categoryId,
                title = names.first,
                subtitle = if (productIndex == 0) names.second else product.first,
                sku = sku,
                priceCents = 88000,
                imageRes = product.second
            )
        }
    }

    fun productsFor(categoryId: String): List<CatalogProduct> =
        products.filter { it.categoryId == categoryId }

    fun productById(productId: String): CatalogProduct =
        products.firstOrNull { it.id == productId } ?: products.first()
}
