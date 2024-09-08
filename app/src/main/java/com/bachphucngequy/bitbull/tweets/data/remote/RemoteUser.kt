package com.bachphucngequy.bitbull.tweets.data.remote

data class RemoteUser(
    val bio: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val userId: String = ""
)

var remoteUsers: List<RemoteUser> = listOf()