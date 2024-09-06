package com.bachphucngequy.bitbull.domain.model

data class Coin(
    val id: String = "",
    val is_active: Boolean = false,
    val name: String = "",
    val rank: Int = -1,
    val symbol: String = "",
    var isFavourite: Boolean = false
)