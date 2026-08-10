package com.sydra.app.data.local

import com.sydra.app.data.api.ApiError
import com.sydra.app.data.api.ApiErrorCode
import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.api.SydraApi
import com.sydra.app.data.dto.AddressDto
import com.sydra.app.data.dto.AddressInputDto
import com.sydra.app.data.dto.AfterSaleDto
import com.sydra.app.data.dto.AfterSaleStatusDto
import com.sydra.app.data.dto.AfterSaleTimelineEventDto
import com.sydra.app.data.dto.AddressSnapshotDto
import com.sydra.app.data.dto.CancelOrderResponseDto
import com.sydra.app.data.dto.EvidenceDto
import com.sydra.app.data.dto.HelpArticleDto
import com.sydra.app.data.dto.HelpCategoryDto
import com.sydra.app.data.dto.LogisticsDto
import com.sydra.app.data.dto.LogisticsEventDto
import com.sydra.app.data.dto.MemberBenefitDto
import com.sydra.app.data.dto.MemberCenterDto
import com.sydra.app.data.dto.OrderActionDto
import com.sydra.app.data.dto.OrderDto
import com.sydra.app.data.dto.OrderItemDto
import com.sydra.app.data.dto.OrderStatusDto
import com.sydra.app.data.dto.PageDto
import com.sydra.app.data.dto.ProfileDto
import com.sydra.app.data.dto.ReturnSubmissionDto
import com.sydra.app.data.dto.SupportCenterDto
import com.sydra.app.data.dto.SupportConversationDto
import com.sydra.app.data.dto.SupportMessageDto

/**
 * Offline fixture that exercises the same contract as a future network API.
 * It is intentionally deterministic and must not be described as production data.
 */
class LocalPrototypeApi : SydraApi {
    private val cancelledOrdersByKey = mutableMapOf<String, CancelOrderResponseDto>()
    private val returnRequestsByKey = mutableMapOf<String, AfterSaleDto>()
    private val savedAddressesByKey = mutableMapOf<String, AddressDto>()

    private val orders = mutableListOf(
        order("pending-payment", OrderStatusDto.PENDING_PAYMENT, setOf(OrderActionDto.PAY, OrderActionDto.CANCEL)),
        order("pending-shipment", OrderStatusDto.PENDING_SHIPMENT, setOf(OrderActionDto.CANCEL, OrderActionDto.CHANGE_ADDRESS, OrderActionDto.SUPPORT)),
        order("pending-receipt", OrderStatusDto.PENDING_RECEIPT, setOf(OrderActionDto.TRACK, OrderActionDto.RETURN, OrderActionDto.SUPPORT)),
        order("completed", OrderStatusDto.COMPLETED, setOf(OrderActionDto.RETURN)),
        order("after-sale", OrderStatusDto.AFTER_SALE, setOf(OrderActionDto.SUPPORT))
    )

    private val afterSales = mutableListOf(
        AfterSaleDto(
            id = "as-20260806",
            orderId = "order-after-sale",
            orderItemId = "item-after-sale",
            reason = "订单信息拍错（类型、品牌等）",
            description = null,
            status = AfterSaleStatusDto.SUBMITTED,
            amountCents = 88000,
            timeline = listOf(
                AfterSaleTimelineEventDto(
                    id = "as-event-1",
                    occurredAt = "2026-08-06T10:00:00+08:00",
                    title = "退货申请已提交",
                    description = "等待官方审核"
                )
            )
        )
    )

    private val addresses = mutableListOf(
        AddressDto(
            id = "address-1",
            recipient = "王大宝",
            phone = "13800006688",
            region = listOf("陕西省", "西安市", "雁良区", "街道"),
            detail = "陕西省西安市雁良区上陵路",
            tag = "默认",
            isDefault = true
        )
    )

    private val conversations = mutableMapOf(
        "conversation-default" to mutableListOf(
            SupportMessageDto(
                id = "message-system-1",
                sender = "system",
                body = "你好，需要查询订单物流、退货或退款吗？",
                createdAt = "2026-08-10T10:00:00+08:00",
                sendStatus = "SENT"
            )
        )
    )

    override suspend fun fetchProfile(): ApiResult<ProfileDto> = success(
        ProfileDto(
            id = "user-local-001",
            displayName = "用户名",
            memberLevel = "T5",
            orderCounts = orders.groupingBy { it.status }.eachCount()
        )
    )

    override suspend fun fetchMemberCenter(): ApiResult<MemberCenterDto> = success(
        MemberCenterDto(
            level = "T5",
            disclaimer = "当前为本地原型内容，正式权益以服务端返回为准。",
            benefits = listOf(
                MemberBenefitDto("benefit-overview", "权益概览", "查看已确认的会员权益。", false),
                MemberBenefitDto("benefit-usage", "使用条件", "查看权益适用条件。", false),
                MemberBenefitDto("benefit-validity", "有效期说明", "查看权益有效期。", false)
            )
        )
    )

    override suspend fun fetchHelpCategories(): ApiResult<List<HelpCategoryDto>> = success(
        listOf(
            HelpCategoryDto("commerce", "购物与支付", listOf("checkout")),
            HelpCategoryDto("orders", "订单与物流", listOf("logistics")),
            HelpCategoryDto("aftersale", "退货与退款", listOf("returns"))
        )
    )

    override suspend fun fetchHelpArticle(articleId: String): ApiResult<HelpArticleDto> =
        helpArticles[articleId]?.let { success(it) } ?: failure(ApiErrorCode.NOT_FOUND, "帮助文章不存在")

    override suspend fun fetchOrders(
        status: OrderStatusDto?,
        cursor: String?
    ): ApiResult<PageDto<OrderDto>> = success(
        PageDto(
            items = orders.filter { status == null || it.status == status },
            nextCursor = null
        )
    )

    override suspend fun fetchOrder(orderId: String): ApiResult<OrderDto> =
        orders.firstOrNull { it.id == orderId }?.let { success(it) }
            ?: failure(ApiErrorCode.NOT_FOUND, "订单不存在")

    override suspend fun cancelOrder(
        orderId: String,
        idempotencyKey: String
    ): ApiResult<CancelOrderResponseDto> {
        cancelledOrdersByKey[idempotencyKey]?.let { return success(it) }
        val order = orders.firstOrNull { it.id == orderId }
            ?: return failure(ApiErrorCode.NOT_FOUND, "订单不存在")
        if (OrderActionDto.CANCEL !in order.availableActions) {
            return failure(ApiErrorCode.ORDER_NOT_ELIGIBLE, "当前订单不可取消")
        }
        orders.remove(order)
        val result = CancelOrderResponseDto(
            orderId = orderId,
            outcome = "CANCELLED",
            updatedAt = "2026-08-10T10:30:00+08:00",
            message = "订单已取消，本地原型已从当前订单列表移除。"
        )
        cancelledOrdersByKey[idempotencyKey] = result
        return success(result)
    }

    override suspend fun fetchLogistics(orderId: String): ApiResult<LogisticsDto> =
        if (orders.any { it.id == orderId }) {
            success(
                LogisticsDto(
                    orderId = orderId,
                    carrier = "本地演示物流",
                    trackingNo = "SYDRA-DEMO-001",
                    status = "已发货",
                    recipientMasked = "王大宝 138****6688",
                    events = listOf(
                        LogisticsEventDto("log-1", "2026-08-10T10:00:00+08:00", "西安", "商家已发货", true),
                        LogisticsEventDto("log-2", "2026-08-09T16:00:00+08:00", "西安", "包裹已揽收", false)
                    )
                )
            )
        } else {
            failure(ApiErrorCode.NOT_FOUND, "订单不存在")
        }

    override suspend fun fetchAfterSales(cursor: String?): ApiResult<PageDto<AfterSaleDto>> =
        success(PageDto(items = afterSales, nextCursor = null))

    override suspend fun fetchAfterSale(requestId: String): ApiResult<AfterSaleDto> =
        afterSales.firstOrNull { it.id == requestId }?.let { success(it) }
            ?: failure(ApiErrorCode.NOT_FOUND, "售后申请不存在")

    override suspend fun submitReturn(
        request: ReturnSubmissionDto,
        idempotencyKey: String
    ): ApiResult<AfterSaleDto> {
        returnRequestsByKey[idempotencyKey]?.let { return success(it) }
        val order = orders.firstOrNull { it.id == request.orderId }
            ?: return failure(ApiErrorCode.NOT_FOUND, "订单不存在")
        if (OrderActionDto.RETURN !in order.availableActions) {
            return failure(ApiErrorCode.ORDER_NOT_ELIGIBLE, "当前订单不满足退货条件")
        }
        val result = AfterSaleDto(
            id = "as-${request.orderId}-${request.orderItemId}",
            orderId = request.orderId,
            orderItemId = request.orderItemId,
            reason = request.reason,
            description = request.description,
            evidence = request.evidenceIds.map { evidenceId ->
                EvidenceDto(evidenceId, "local-evidence.jpg", "image/jpeg", 120_000)
            },
            status = AfterSaleStatusDto.SUBMITTED,
            amountCents = order.totalAmountCents,
            timeline = listOf(
                AfterSaleTimelineEventDto(
                    id = "timeline-${request.orderId}",
                    occurredAt = "2026-08-10T10:35:00+08:00",
                    title = "退货申请已提交",
                    description = "等待官方审核"
                )
            )
        )
        afterSales.removeAll { it.id == result.id }
        afterSales += result
        orders[orders.indexOf(order)] = order.copy(
            status = OrderStatusDto.AFTER_SALE,
            availableActions = listOf(OrderActionDto.VIEW_DETAILS, OrderActionDto.SUPPORT)
        )
        returnRequestsByKey[idempotencyKey] = result
        return success(result)
    }

    override suspend fun fetchAddresses(): ApiResult<List<AddressDto>> = success(addresses.toList())

    override suspend fun saveAddress(
        address: AddressInputDto,
        idempotencyKey: String
    ): ApiResult<AddressDto> {
        savedAddressesByKey[idempotencyKey]?.let { return success(it) }
        val validationError = validateAddress(address)
        if (validationError != null) return ApiResult.Failure(validationError)

        val id = address.id ?: "address-${addresses.size + 1}"
        if (address.isDefault) {
            addresses.replaceAll { it.copy(isDefault = false) }
        }
        val saved = AddressDto(
            id = id,
            recipient = address.recipient,
            phone = address.phone,
            region = address.region,
            detail = address.detail,
            tag = address.tag,
            isDefault = address.isDefault || addresses.none { it.isDefault }
        )
        val existingIndex = addresses.indexOfFirst { it.id == id }
        if (existingIndex >= 0) addresses[existingIndex] = saved else addresses += saved
        savedAddressesByKey[idempotencyKey] = saved
        return success(saved)
    }

    override suspend fun setDefaultAddress(addressId: String): ApiResult<AddressDto> {
        val target = addresses.firstOrNull { it.id == addressId }
            ?: return failure(ApiErrorCode.NOT_FOUND, "地址不存在")
        addresses.replaceAll { it.copy(isDefault = it.id == addressId) }
        return success(target.copy(isDefault = true))
    }

    override suspend fun deleteAddress(addressId: String): ApiResult<Unit> {
        val target = addresses.firstOrNull { it.id == addressId }
            ?: return failure(ApiErrorCode.NOT_FOUND, "地址不存在")
        if (target.isDefault && addresses.size > 1) {
            return failure(ApiErrorCode.CONFLICT, "请先设置其他默认地址")
        }
        addresses.remove(target)
        return success(Unit)
    }

    override suspend fun fetchSupportCenter(): ApiResult<SupportCenterDto> = success(
        SupportCenterDto(
            topics = listOf("购物与支付", "订单与物流", "退货与退款"),
            disclaimer = "当前为本地客服原型，不展示未经确认的客服电话。"
        )
    )

    override suspend fun fetchConversation(conversationId: String): ApiResult<SupportConversationDto> =
        conversations[conversationId]?.let {
            success(SupportConversationDto(conversationId, it.toList()))
        } ?: failure(ApiErrorCode.NOT_FOUND, "会话不存在")

    override suspend fun sendMessage(
        conversationId: String,
        clientMessageId: String,
        text: String
    ): ApiResult<SupportMessageDto> {
        if (text.isBlank()) return failure(ApiErrorCode.INVALID_ARGUMENT, "消息不能为空")
        if (text.contains("[network-error]")) {
            return failure(ApiErrorCode.NETWORK, "网络异常，请稍后重试", retryable = true)
        }
        val messages = conversations.getOrPut(conversationId) { mutableListOf() }
        val message = SupportMessageDto(
            id = clientMessageId,
            sender = "user",
            body = text,
            createdAt = "2026-08-10T10:40:00+08:00",
            sendStatus = "SENT"
        )
        messages += message
        return success(message)
    }

    private fun order(
        suffix: String,
        status: OrderStatusDto,
        extraActions: Set<OrderActionDto>
    ): OrderDto = OrderDto(
        id = "order-$suffix",
        displayNo = "SYDRA-20260810-${suffix.uppercase()}",
        status = status,
        items = listOf(
            OrderItemDto(
                itemId = "item-$suffix",
                sku = "221026",
                title = "MODULAR VEST",
                imageUrl = "local://product_01.jpg",
                size = "S",
                quantity = 1,
                unitPriceCents = 88000
            )
        ),
        totalAmountCents = 88000,
        paymentMethod = "微信支付",
        addressSnapshot = AddressSnapshotDto(
            recipient = "王大宝",
            maskedPhone = "138****6688",
            region = listOf("陕西省", "西安市"),
            detail = "雁良区上陵路"
        ),
        availableActions = (setOf(OrderActionDto.VIEW_DETAILS) + extraActions).toList(),
        createdAt = "2026-08-10T09:00:00+08:00",
        paidAt = if (status == OrderStatusDto.PENDING_PAYMENT) null else "2026-08-10T09:05:00+08:00"
    )

    private fun validateAddress(address: AddressInputDto): ApiError? {
        val fieldErrors = buildMap {
            if (address.recipient.isBlank()) put("recipient", "请输入联系人")
            if (!address.phone.matches(Regex("1\\d{10}"))) put("phone", "请输入 11 位手机号")
            if (address.region.size < 3 || address.region.any(String::isBlank)) put("region", "请选择完整地区")
            if (address.detail.isBlank()) put("detail", "请输入详细地址")
        }
        return if (fieldErrors.isEmpty()) null else ApiError(
            code = ApiErrorCode.ADDRESS_INVALID,
            message = "地址信息不完整",
            fieldErrors = fieldErrors
        )
    }

    private companion object {
        val helpArticles = mapOf(
            "checkout" to HelpArticleDto(
                id = "checkout",
                categoryId = "commerce",
                title = "购物与支付",
                version = "local-0.1",
                content = "正式支付渠道、订单确认和服务端支付单仍需在联调阶段接入。"
            ),
            "logistics" to HelpArticleDto(
                id = "logistics",
                categoryId = "orders",
                title = "订单与物流",
                version = "local-0.1",
                content = "订单发货后可在订单详情中查看物流时间线。物流信息以服务端承运商数据为准。"
            ),
            "returns" to HelpArticleDto(
                id = "returns",
                categoryId = "aftersale",
                title = "退货与退款",
                version = "local-0.1",
                content = "退货资格、凭证规则和退款时效需以正式售后政策和服务端校验为准。"
            )
        )
    }
}

private fun <T> success(value: T): ApiResult.Success<T> = ApiResult.Success(value)

private fun failure(
    code: ApiErrorCode,
    message: String,
    retryable: Boolean = false
): ApiResult.Failure = ApiResult.Failure(ApiError(code, message, retryable))
