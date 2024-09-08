package com.bachphucngequy.bitbull.tweets.data.remote

data class RemotePostLike(
    val postId: String = "",
    val userId: String = ""
)

var remoteLikes: List<RemotePostLike> = listOf()