package com.bachphucngequy.bitbull.domain.model

import com.bachphucngequy.bitbull.data.entity.TeamMember

data class CoinDetail(
    val id: String = "",
    val name: String = "",
    val symbol: String = "",
    val description: String = "",
    val rank: Int = -1,
    val isActive: Boolean = false,
    val tags: List<String> = emptyList(),
    val team: List<TeamMember> = emptyList(),
    val logo: String = "",
)