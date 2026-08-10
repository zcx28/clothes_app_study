package com.sydra.app.data.mapper

import com.sydra.app.data.dto.AddressDto
import com.sydra.app.data.dto.CancelOrderResponseDto
import com.sydra.app.data.dto.AfterSaleDto
import com.sydra.app.data.dto.AfterSaleStatusDto
import com.sydra.app.data.dto.HelpArticleDto
import com.sydra.app.data.dto.HelpCategoryDto
import com.sydra.app.data.dto.LogisticsDto
import com.sydra.app.data.dto.MemberBenefitDto
import com.sydra.app.data.dto.MemberCenterDto
import com.sydra.app.data.dto.OrderActionDto
import com.sydra.app.data.dto.OrderDto
import com.sydra.app.data.dto.OrderStatusDto
import com.sydra.app.data.dto.PageDto
import com.sydra.app.data.dto.ProfileDto
import com.sydra.app.data.dto.SupportCenterDto
import com.sydra.app.data.dto.SupportConversationDto
import com.sydra.app.data.dto.SupportMessageDto
import com.sydra.app.domain.model.Address
import com.sydra.app.domain.model.CancelOrderResult
import com.sydra.app.domain.model.AfterSaleRequest
import com.sydra.app.domain.model.AfterSaleStatus
import com.sydra.app.domain.model.HelpArticleModel
import com.sydra.app.domain.model.HelpCategoryModel
import com.sydra.app.domain.model.Logistics
import com.sydra.app.domain.model.LogisticsEvent
import com.sydra.app.domain.model.MemberBenefitModel
import com.sydra.app.domain.model.MemberCenterModel
import com.sydra.app.domain.model.Order
import com.sydra.app.domain.model.OrderAction
import com.sydra.app.domain.model.OrderItemSnapshot
import com.sydra.app.domain.model.OrderStatus
import com.sydra.app.domain.model.Page
import com.sydra.app.domain.model.ProfileModel
import com.sydra.app.domain.model.SupportCenterModel
import com.sydra.app.domain.model.SupportConversation
import com.sydra.app.domain.model.SupportMessage

fun ProfileDto.toModel(): ProfileModel = ProfileModel(
    id = id,
    displayName = displayName,
    avatarUrl = avatarUrl,
    memberLevel = memberLevel,
    orderCounts = orderCounts.mapKeys { it.key.toModel() }
)

fun MemberCenterDto.toModel(): MemberCenterModel = MemberCenterModel(
    level = level,
    benefits = benefits.map(MemberBenefitDto::toModel),
    disclaimer = disclaimer
)

fun MemberBenefitDto.toModel(): MemberBenefitModel = MemberBenefitModel(
    id = id,
    title = title,
    description = description,
    available = available
)

fun HelpCategoryDto.toModel(): HelpCategoryModel = HelpCategoryModel(
    id = id,
    title = title,
    articleIds = articleIds
)

fun HelpArticleDto.toModel(): HelpArticleModel = HelpArticleModel(
    id = id,
    categoryId = categoryId,
    title = title,
    version = version,
    content = content,
    relatedArticleIds = relatedArticleIds
)

fun OrderDto.toModel(): Order = Order(
    id = id,
    displayNo = displayNo,
    status = status.toModel(),
    items = items.map {
        OrderItemSnapshot(
            itemId = it.itemId,
            sku = it.sku,
            title = it.title,
            imageUrl = it.imageUrl,
            size = it.size,
            quantity = it.quantity,
            unitPriceCents = it.unitPriceCents
        )
    },
    totalAmountCents = totalAmountCents,
    paymentMethod = paymentMethod,
    addressSnapshot = addressSnapshot?.let {
        com.sydra.app.domain.model.AddressSnapshot(
            recipient = it.recipient,
            maskedPhone = it.maskedPhone,
            region = it.region,
            detail = it.detail
        )
    },
    availableActions = availableActions.map(OrderActionDto::toModel).toSet(),
    createdAt = createdAt,
    paidAt = paidAt
)

fun PageDto<OrderDto>.toModel(): Page<Order> = Page(
    items = items.map(OrderDto::toModel),
    nextCursor = nextCursor
)

fun CancelOrderResponseDto.toModel(): CancelOrderResult = CancelOrderResult(
    orderId = orderId,
    outcome = outcome,
    updatedAt = updatedAt,
    message = message
)

fun PageDto<AfterSaleDto>.toAfterSalePageModel(): Page<AfterSaleRequest> = Page(
    items = items.map(AfterSaleDto::toModel),
    nextCursor = nextCursor
)

fun LogisticsDto.toModel(): Logistics = Logistics(
    orderId = orderId,
    carrier = carrier,
    trackingNo = trackingNo,
    status = status,
    recipientMasked = recipientMasked,
    events = events.map {
        LogisticsEvent(
            id = it.id,
            occurredAt = it.occurredAt,
            location = it.location,
            description = it.description,
            isCurrent = it.isCurrent
        )
    }
)

fun AfterSaleDto.toModel(): AfterSaleRequest = AfterSaleRequest(
    id = id,
    orderId = orderId,
    orderItemId = orderItemId,
    reason = reason,
    description = description,
    evidence = evidence.map {
        com.sydra.app.domain.model.Evidence(
            id = it.id,
            fileName = it.fileName,
            mimeType = it.mimeType,
            sizeBytes = it.sizeBytes
        )
    },
    status = status.toModel(),
    amountCents = amountCents,
    timeline = timeline.map {
        com.sydra.app.domain.model.AfterSaleTimelineEvent(
            id = it.id,
            occurredAt = it.occurredAt,
            title = it.title,
            description = it.description
        )
    }
)

fun AddressDto.toModel(): Address = Address(
    id = id,
    recipient = recipient,
    phone = phone,
    region = region,
    detail = detail,
    tag = tag,
    isDefault = isDefault
)

fun SupportCenterDto.toModel(): SupportCenterModel = SupportCenterModel(
    topics = topics,
    disclaimer = disclaimer
)

fun SupportMessageDto.toModel(): SupportMessage = SupportMessage(
    id = id,
    sender = sender,
    body = body,
    createdAt = createdAt,
    sendStatus = sendStatus
)

fun SupportConversationDto.toModel(): SupportConversation = SupportConversation(
    id = id,
    messages = messages.map(SupportMessageDto::toModel)
)

private fun OrderStatusDto.toModel(): OrderStatus = when (this) {
    OrderStatusDto.PENDING_PAYMENT -> OrderStatus.PENDING_PAYMENT
    OrderStatusDto.PENDING_SHIPMENT -> OrderStatus.PENDING_SHIPMENT
    OrderStatusDto.PENDING_RECEIPT -> OrderStatus.PENDING_RECEIPT
    OrderStatusDto.COMPLETED -> OrderStatus.COMPLETED
    OrderStatusDto.AFTER_SALE -> OrderStatus.AFTER_SALE
}

private fun OrderActionDto.toModel(): OrderAction = when (this) {
    OrderActionDto.VIEW_DETAILS -> OrderAction.VIEW_DETAILS
    OrderActionDto.PAY -> OrderAction.PAY
    OrderActionDto.CANCEL -> OrderAction.CANCEL
    OrderActionDto.CHANGE_ADDRESS -> OrderAction.CHANGE_ADDRESS
    OrderActionDto.TRACK -> OrderAction.TRACK
    OrderActionDto.RETURN -> OrderAction.RETURN
    OrderActionDto.SUPPORT -> OrderAction.SUPPORT
    OrderActionDto.CONFIRM_RECEIPT -> OrderAction.CONFIRM_RECEIPT
    OrderActionDto.SUPPLEMENT_EVIDENCE -> OrderAction.SUPPLEMENT_EVIDENCE
}

private fun AfterSaleStatusDto.toModel(): AfterSaleStatus = when (this) {
    AfterSaleStatusDto.SUBMITTED -> AfterSaleStatus.SUBMITTED
    AfterSaleStatusDto.REFUNDING -> AfterSaleStatus.REFUNDING
    AfterSaleStatusDto.COMPLETED -> AfterSaleStatus.COMPLETED
    AfterSaleStatusDto.REJECTED -> AfterSaleStatus.REJECTED
}
