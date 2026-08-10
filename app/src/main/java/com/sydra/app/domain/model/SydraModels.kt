package com.sydra.app.domain.model

enum class OrderStatus {
    PENDING_PAYMENT,
    PENDING_SHIPMENT,
    PENDING_RECEIPT,
    COMPLETED,
    AFTER_SALE
}

enum class OrderAction {
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

enum class AfterSaleStatus {
    SUBMITTED,
    REFUNDING,
    COMPLETED,
    REJECTED
}

data class Page<T>(
    val items: List<T>,
    val nextCursor: String? = null
)

data class ProfileModel(
    val id: String,
    val displayName: String,
    val avatarUrl: String?,
    val memberLevel: String,
    val orderCounts: Map<OrderStatus, Int>
)

data class MemberCenterModel(
    val level: String,
    val benefits: List<MemberBenefitModel>,
    val disclaimer: String
)

data class MemberBenefitModel(
    val id: String,
    val title: String,
    val description: String,
    val available: Boolean
)

data class HelpCategoryModel(
    val id: String,
    val title: String,
    val articleIds: List<String>
)

data class HelpArticleModel(
    val id: String,
    val categoryId: String,
    val title: String,
    val version: String,
    val content: String,
    val relatedArticleIds: List<String>
)

data class AddressSnapshot(
    val recipient: String,
    val maskedPhone: String,
    val region: List<String>,
    val detail: String
)

data class OrderItemSnapshot(
    val itemId: String,
    val sku: String,
    val title: String,
    val imageUrl: String?,
    val size: String,
    val quantity: Int,
    val unitPriceCents: Int
)

data class Order(
    val id: String,
    val displayNo: String,
    val status: OrderStatus,
    val items: List<OrderItemSnapshot>,
    val totalAmountCents: Int,
    val paymentMethod: String?,
    val addressSnapshot: AddressSnapshot?,
    val availableActions: Set<OrderAction>,
    val createdAt: String,
    val paidAt: String?
)

data class CancelOrderResult(
    val orderId: String,
    val outcome: String,
    val updatedAt: String,
    val message: String
)

data class LogisticsEvent(
    val id: String,
    val occurredAt: String,
    val location: String,
    val description: String,
    val isCurrent: Boolean
)

data class Logistics(
    val orderId: String,
    val carrier: String?,
    val trackingNo: String?,
    val status: String,
    val recipientMasked: String,
    val events: List<LogisticsEvent>
)

data class Evidence(
    val id: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long
)

data class AfterSaleTimelineEvent(
    val id: String,
    val occurredAt: String,
    val title: String,
    val description: String
)

data class AfterSaleRequest(
    val id: String,
    val orderId: String,
    val orderItemId: String,
    val reason: String,
    val description: String?,
    val evidence: List<Evidence>,
    val status: AfterSaleStatus,
    val amountCents: Int,
    val timeline: List<AfterSaleTimelineEvent>
)

data class Address(
    val id: String,
    val recipient: String,
    val phone: String,
    val region: List<String>,
    val detail: String,
    val tag: String,
    val isDefault: Boolean
)

data class SupportCenterModel(
    val topics: List<String>,
    val disclaimer: String
)

data class SupportMessage(
    val id: String,
    val sender: String,
    val body: String,
    val createdAt: String,
    val sendStatus: String
)

data class SupportConversation(
    val id: String,
    val messages: List<SupportMessage>
)
