package com.bachphucngequy.bitbull.data.entity

data class SymbolPriceTickerItem(
    val price: String,
    val symbol: String
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            symbol
        )

        return matchingCombinations.any{
            it.contains(query, ignoreCase = true)
        }
    }
}