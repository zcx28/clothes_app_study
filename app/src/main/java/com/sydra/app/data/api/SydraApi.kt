package com.sydra.app.data.api

import com.sydra.app.data.dto.AddressDto
import com.sydra.app.data.dto.AddressInputDto
import com.sydra.app.data.dto.AfterSaleDto
import com.sydra.app.data.dto.CancelOrderResponseDto
import com.sydra.app.data.dto.HelpArticleDto
import com.sydra.app.data.dto.HelpCategoryDto
import com.sydra.app.data.dto.LogisticsDto
import com.sydra.app.data.dto.MemberCenterDto
import com.sydra.app.data.dto.OrderDto
import com.sydra.app.data.dto.PageDto
import com.sydra.app.data.dto.ProfileDto
import com.sydra.app.data.dto.ReturnSubmissionDto
import com.sydra.app.data.dto.SupportCenterDto
import com.sydra.app.data.dto.SupportConversationDto
import com.sydra.app.data.dto.SupportMessageDto
import com.sydra.app.data.dto.OrderStatusDto

/**
 * Replaceable application data contract.
 *
 * The current implementation is LocalPrototypeApi. A network implementation
 * can satisfy this contract later without changing Compose screens or routes.
 */
interface SydraApi {
    suspend fun fetchProfile(): ApiResult<ProfileDto>

    suspend fun fetchMemberCenter(): ApiResult<MemberCenterDto>

    suspend fun fetchHelpCategories(): ApiResult<List<HelpCategoryDto>>

    suspend fun fetchHelpArticle(articleId: String): ApiResult<HelpArticleDto>

    suspend fun fetchOrders(
        status: OrderStatusDto? = null,
        cursor: String? = null
    ): ApiResult<PageDto<OrderDto>>

    suspend fun fetchOrder(orderId: String): ApiResult<OrderDto>

    suspend fun cancelOrder(
        orderId: String,
        idempotencyKey: String
    ): ApiResult<CancelOrderResponseDto>

    suspend fun fetchLogistics(orderId: String): ApiResult<LogisticsDto>

    suspend fun fetchAfterSales(cursor: String? = null): ApiResult<PageDto<AfterSaleDto>>

    suspend fun fetchAfterSale(requestId: String): ApiResult<AfterSaleDto>

    suspend fun submitReturn(
        request: ReturnSubmissionDto,
        idempotencyKey: String
    ): ApiResult<AfterSaleDto>

    suspend fun fetchAddresses(): ApiResult<List<AddressDto>>

    suspend fun saveAddress(
        address: AddressInputDto,
        idempotencyKey: String
    ): ApiResult<AddressDto>

    suspend fun setDefaultAddress(addressId: String): ApiResult<AddressDto>

    suspend fun deleteAddress(addressId: String): ApiResult<Unit>

    suspend fun fetchSupportCenter(): ApiResult<SupportCenterDto>

    suspend fun fetchConversation(conversationId: String): ApiResult<SupportConversationDto>

    suspend fun sendMessage(
        conversationId: String,
        clientMessageId: String,
        text: String
    ): ApiResult<SupportMessageDto>
}

object SydraApiProvider {
    /** Local-only deterministic source used until an authenticated API exists. */
    val localPrototype: SydraApi by lazy {
        com.sydra.app.data.local.LocalPrototypeApi()
    }
}
