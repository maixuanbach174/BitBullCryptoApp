package com.bachphucngequy.bitbull.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subscribe(
    val id: Int,
    val method: String = "SUBSCRIBE",
    val params: List<String>
)