package com.sydra.app.data.repository

import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.api.SydraApi
import com.sydra.app.data.api.SydraApiProvider
import com.sydra.app.data.dto.AddressInputDto
import com.sydra.app.data.dto.OrderStatusDto
import com.sydra.app.data.dto.ReturnSubmissionDto
import com.sydra.app.data.mapper.toAfterSalePageModel
import com.sydra.app.data.mapper.toModel
import com.sydra.app.domain.model.Address
import com.sydra.app.domain.model.AddressDraft
import com.sydra.app.domain.model.AfterSaleRequest
import com.sydra.app.domain.model.CancelOrderResult
import com.sydra.app.domain.model.Logistics
import com.sydra.app.domain.model.Order
import com.sydra.app.domain.model.OrderStatus
import com.sydra.app.domain.model.Page

interface OrderRepository {
    suspend fun getOrders(status: OrderStatus?): ApiResult<Page<Order>>
    suspend fun getOrder(orderId: String): ApiResult<Order>
    suspend fun cancelOrder(orderId: String, idempotencyKey: String): ApiResult<CancelOrderResult>
}

interface LogisticsRepository {
    suspend fun getLogistics(orderId: String): ApiResult<Logistics>
}

interface AfterSaleRepository {
    suspend fun getRequests(): ApiResult<Page<AfterSaleRequest>>
    suspend fun getRequest(requestId: String): ApiResult<AfterSaleRequest>
    suspend fun submitReturn(
        orderId: String,
        orderItemId: String,
        reason: String,
        description: String?,
        evidenceIds: List<String>,
        idempotencyKey: String
    ): ApiResult<AfterSaleRequest>
}

interface AddressRepository {
    suspend fun getAddresses(): ApiResult<List<Address>>
    suspend fun saveAddress(address: AddressDraft, idempotencyKey: String): ApiResult<Address>
    suspend fun setDefault(addressId: String): ApiResult<Address>
    suspend fun deleteAddress(addressId: String): ApiResult<Unit>
}

class SydraCommerceRepository(private val api: SydraApi) :
    OrderRepository,
    LogisticsRepository,
    AfterSaleRepository,
    AddressRepository {

    override suspend fun getOrders(status: OrderStatus?): ApiResult<Page<Order>> =
        api.fetchOrders(status = status?.toDto()).map { it.toModel() }

    override suspend fun getOrder(orderId: String): ApiResult<Order> =
        api.fetchOrder(orderId).map { it.toModel() }

    override suspend fun cancelOrder(
        orderId: String,
        idempotencyKey: String
    ): ApiResult<CancelOrderResult> = api.cancelOrder(orderId, idempotencyKey).map { it.toModel() }

    override suspend fun getLogistics(orderId: String): ApiResult<Logistics> =
        api.fetchLogistics(orderId).map { it.toModel() }

    override suspend fun getRequests(): ApiResult<Page<AfterSaleRequest>> =
        api.fetchAfterSales().map { it.toAfterSalePageModel() }

    override suspend fun getRequest(requestId: String): ApiResult<AfterSaleRequest> =
        api.fetchAfterSale(requestId).map { it.toModel() }

    override suspend fun submitReturn(
        orderId: String,
        orderItemId: String,
        reason: String,
        description: String?,
        evidenceIds: List<String>,
        idempotencyKey: String
    ): ApiResult<AfterSaleRequest> = api.submitReturn(
        request = ReturnSubmissionDto(
            orderId = orderId,
            orderItemId = orderItemId,
            reason = reason,
            description = description,
            evidenceIds = evidenceIds
        ),
        idempotencyKey = idempotencyKey
    ).map { it.toModel() }

    override suspend fun getAddresses(): ApiResult<List<Address>> =
        api.fetchAddresses().map { addresses -> addresses.map { it.toModel() } }

    override suspend fun saveAddress(
        address: AddressDraft,
        idempotencyKey: String
    ): ApiResult<Address> = api.saveAddress(
        address = AddressInputDto(
            id = address.id,
            recipient = address.recipient,
            phone = address.phone,
            region = address.region,
            detail = address.detail,
            tag = address.tag,
            isDefault = address.isDefault
        ),
        idempotencyKey = idempotencyKey
    ).map { it.toModel() }

    override suspend fun setDefault(addressId: String): ApiResult<Address> =
        api.setDefaultAddress(addressId).map { it.toModel() }

    override suspend fun deleteAddress(addressId: String): ApiResult<Unit> =
        api.deleteAddress(addressId)
}

object CommerceRepositoryProvider {
    val localPrototype: SydraCommerceRepository by lazy {
        SydraCommerceRepository(SydraApiProvider.localPrototype)
    }
}

private inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Failure -> this
}

private fun OrderStatus.toDto(): OrderStatusDto = when (this) {
    OrderStatus.PENDING_PAYMENT -> OrderStatusDto.PENDING_PAYMENT
    OrderStatus.PENDING_SHIPMENT -> OrderStatusDto.PENDING_SHIPMENT
    OrderStatus.PENDING_RECEIPT -> OrderStatusDto.PENDING_RECEIPT
    OrderStatus.COMPLETED -> OrderStatusDto.COMPLETED
    OrderStatus.AFTER_SALE -> OrderStatusDto.AFTER_SALE
}
