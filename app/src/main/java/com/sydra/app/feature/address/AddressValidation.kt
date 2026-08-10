package com.sydra.app.feature.address

import com.sydra.app.domain.model.AddressDraft

fun validateAddressDraft(address: AddressDraft): Map<String, String> = buildMap {
    if (address.recipient.isBlank()) put("recipient", "请输入联系人")
    if (!address.phone.matches(Regex("1\\d{10}"))) put("phone", "请输入 11 位中国大陆手机号")
    if (address.region.size < 3 || address.region.any(String::isBlank)) put("region", "请填写完整省、市、区")
    if (address.detail.isBlank()) put("detail", "请输入详细地址")
}
