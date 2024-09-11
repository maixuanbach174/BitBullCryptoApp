package com.bachphucngequy.bitbull.tweets.data.remote

data class RemoteFollows(
    val fromId: String = "",
    val toId: String = ""
)

var remoteFollows: List<RemoteFollows> = mutableListOf()