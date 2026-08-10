package com.sydra.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatusDto {
    PENDING_PAYMENT,
    PENDING_SHIPMENT,
    PENDING_RECEIPT,
    COMPLETED,
    AFTER_SALE
}

@Serializable
enum class OrderActionDto {
    VIEW_DETAILS,
    PAY,
    CANCEL,
    CHANGE_ADDRESS,
    TRACK,
    RETURN,
    SUPPORT,
    CONFIRM_RECEIPT,
    SUPPLEMENT_EVIDENCE
}

@Serializable
enum class AfterSaleStatusDto {
    SUBMITTED,
    REFUNDING,
    COMPLETED,
    REJECTED
}

@Serializable
data class PageDto<T>(
    val items: List<T>,
    val nextCursor: String? = null
)

@Serializable
data class ProfileDto(
    val id: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val memberLevel: String,
    val orderCounts: Map<OrderStatusDto, Int> = emptyMap()
)

@Serializable
data class MemberCenterDto(
    val level: String,
    val benefits: List<MemberBenefitDto>,
    val disclaimer: String
)

@Serializable
data class MemberBenefitDto(
    val id: String,
    val title: String,
    val description: String,
    val available: Boolean
)

@Serializable
data class HelpCategoryDto(
    val id: String,
    val title: String,
    val articleIds: List<String>
)

@Serializable
data class HelpArticleDto(
    val id: String,
    val categoryId: String,
    val title: String,
    val version: String,
    val content: String,
    val relatedArticleIds: List<String> = emptyList()
)

@Serializable
data class AddressSnapshotDto(
    val recipient: String,
    val maskedPhone: String,
    val region: List<String>,
    val detail: String
)

@Serializable
data class OrderItemDto(
    val itemId: String,
    val sku: String,
    val title: String,
    val imageUrl: String? = null,
    val size: String,
    val quantity: Int,
    val unitPriceCents: Int
)

@Serializable
data class OrderDto(
    val id: String,
    val displayNo: String,
    val status: OrderStatusDto,
    val items: List<OrderItemDto>,
    val totalAmountCents: Int,
    val paymentMethod: String? = null,
    val addressSnapshot: AddressSnapshotDto? = null,
    val availableActions: List<OrderActionDto> = listOf(OrderActionDto.VIEW_DETAILS),
    val createdAt: String,
    val paidAt: String? = null
)

@Serializable
data class CancelOrderResponseDto(
    val orderId: String,
    val outcome: String,
    val updatedAt: String,
    val message: String
)

@Serializable
data class LogisticsEventDto(
    val id: String,
    val occurredAt: String,
    val location: String,
    val description: String,
    val isCurrent: Boolean
)

@Serializable
data class LogisticsDto(
    val orderId: String,
    val carrier: String? = null,
    val trackingNo: String? = null,
    val status: String,
    val recipientMasked: String,
    val events: List<LogisticsEventDto> = emptyList()
)

@Serializable
data class EvidenceDto(
    val id: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long
)

@Serializable
data class ReturnSubmissionDto(
    val orderId: String,
    val orderItemId: String,
    val reason: String,
    val description: String? = null,
    val evidenceIds: List<String> = emptyList()
)

@Serializable
data class AfterSaleDto(
    val id: String,
    val orderId: String,
    val orderItemId: String,
    val reason: String,
    val description: String? = null,
    val evidence: List<EvidenceDto> = emptyList(),
    val status: AfterSaleStatusDto,
    val amountCents: Int,
    val timeline: List<AfterSaleTimelineEventDto> = emptyList()
)

@Serializable
data class AfterSaleTimelineEventDto(
    val id: String,
    val occurredAt: String,
    val title: String,
    val description: String
)

@Serializable
data class AddressDto(
    val id: String,
    val recipient: String,
    val phone: String,
    val region: List<String>,
    val detail: String,
    val tag: String,
    val isDefault: Boolean
)

@Serializable
data class AddressInputDto(
    val id: String? = null,
    val recipient: String,
    val phone: String,
    val region: List<String>,
    val detail: String,
    val tag: String,
    val isDefault: Boolean
)

@Serializable
data class SupportCenterDto(
    val topics: List<String>,
    val disclaimer: String
)

@Serializable
data class SupportMessageDto(
    val id: String,
    val sender: String,
    val body: String,
    val createdAt: String,
    val sendStatus: String
)

@Serializable
data class SupportConversationDto(
    val id: String,
    val messages: List<SupportMessageDto>
)
