package com.sydra.app.feature.address

import com.sydra.app.domain.model.AddressDraft
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AddressValidationTest {
    @Test
    fun validAddress_hasNoErrors() {
        val errors = validateAddressDraft(validAddress())

        assertTrue(errors.isEmpty())
    }

    @Test
    fun blankFields_returnFieldSpecificErrors() {
        val errors = validateAddressDraft(
            validAddress().copy(
                recipient = "",
                phone = "123",
                region = listOf("陕西省", "", "雁良区"),
                detail = ""
            )
        )

        assertEquals(setOf("recipient", "phone", "region", "detail"), errors.keys)
    }

    private fun validAddress() = AddressDraft(
        recipient = "王大宝",
        phone = "13800006688",
        region = listOf("陕西省", "西安市", "雁良区"),
        detail = "上陵路 1 号",
        tag = "默认",
        isDefault = true
    )
}
