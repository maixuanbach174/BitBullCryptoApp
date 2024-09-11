package com.bachphucngequy.bitbull.tweets.data.remote

data class RemoteComment(
    val content: String = "",
    val createdAt: String = "",
    val postId: String = "",
    val userId: String = ""
)

var remoteComments: List<RemoteComment> = listOf()