package com.sydra.app.data.local

import com.sydra.app.data.api.ApiResult
import com.sydra.app.data.dto.OrderStatusDto
import com.sydra.app.data.dto.AddressInputDto
import com.sydra.app.data.dto.ReturnSubmissionDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalPrototypeApiTest {
    @Test
    fun orders_coverAllFiveDomainStatuses() = runBlocking {
        val api = LocalPrototypeApi()

        val result = api.fetchOrders()

        assertTrue(result is ApiResult.Success)
        val statuses = (result as ApiResult.Success).data.items.map { it.status }.toSet()
        assertEquals(OrderStatusDto.entries.toSet(), statuses)
    }

    @Test
    fun submittingReturn_movesOrderIntoAfterSale() = runBlocking {
        val api = LocalPrototypeApi()
        val submission = ReturnSubmissionDto(
            orderId = "order-completed",
            orderItemId = "item-completed",
            reason = "不想要了"
        )

        val submitted = api.submitReturn(submission, "test-key")
        val refreshed = api.fetchOrder("order-completed")

        assertTrue(submitted is ApiResult.Success)
        assertTrue(refreshed is ApiResult.Success)
        assertEquals(
            OrderStatusDto.AFTER_SALE,
            (refreshed as ApiResult.Success).data.status
        )
    }

    @Test
    fun submittingReturn_withSameIdempotencyKey_returnsSameRequest() = runBlocking {
        val api = LocalPrototypeApi()
        val submission = ReturnSubmissionDto(
            orderId = "order-completed",
            orderItemId = "item-completed",
            reason = "不想要了"
        )

        val first = api.submitReturn(submission, "same-return-key")
        val second = api.submitReturn(submission, "same-return-key")

        assertTrue(first is ApiResult.Success)
        assertTrue(second is ApiResult.Success)
        assertEquals(
            (first as ApiResult.Success).data.id,
            (second as ApiResult.Success).data.id
        )
    }

    @Test
    fun cancellingOrder_withSameIdempotencyKey_returnsSameOutcome() = runBlocking {
        val api = LocalPrototypeApi()

        val first = api.cancelOrder("order-pending-payment", "same-cancel-key")
        val second = api.cancelOrder("order-pending-payment", "same-cancel-key")

        assertTrue(first is ApiResult.Success)
        assertTrue(second is ApiResult.Success)
        assertEquals(
            (first as ApiResult.Success).data.orderId,
            (second as ApiResult.Success).data.orderId
        )
    }

    @Test
    fun savingDefaultAddress_keepsOnlyOneDefault() = runBlocking {
        val api = LocalPrototypeApi()
        val input = AddressInputDto(
            recipient = "李小姐",
            phone = "13900001111",
            region = listOf("广东省", "深圳市", "南山区"),
            detail = "海德三道 15 号",
            tag = "公司",
            isDefault = true
        )

        val saved = api.saveAddress(input, "address-key")
        val addresses = api.fetchAddresses()

        assertTrue(saved is ApiResult.Success)
        assertTrue(addresses is ApiResult.Success)
        assertEquals(1, (addresses as ApiResult.Success).data.count { it.isDefault })
        assertEquals(2, addresses.data.size)
    }
}
