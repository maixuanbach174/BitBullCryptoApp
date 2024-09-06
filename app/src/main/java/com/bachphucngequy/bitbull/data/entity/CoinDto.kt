package com.bachphucngequy.bitbull.data.entity

import com.bachphucngequy.bitbull.domain.model.Coin

data class CoinDto(
    val id: String,
    val isActive: Boolean,
    val isNew: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
    val type: String
)

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        is_active = isActive,
        name = name,
        rank = rank,
        symbol = symbol
    )
}