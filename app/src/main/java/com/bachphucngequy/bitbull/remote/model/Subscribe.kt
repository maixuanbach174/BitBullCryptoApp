package com.bachphucngequy.bitbull.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subscribe(
    val id: Long,
    val method: String = "SUBSCRIBE",
    val params: List<String>
)