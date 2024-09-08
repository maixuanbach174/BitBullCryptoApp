package com.bachphucngequy.bitbull.tweets.data

// fromUserId is following toUserId
data class FollowRelationship(
    val fromUserId: String,
    val toUserId: String
)

val sampleFollowRelationship: MutableList<FollowRelationship> = mutableListOf(
    FollowRelationship(
        fromUserId = "1",
        toUserId = "2"
    ),
    FollowRelationship(
        fromUserId = "2",
        toUserId = "3"
    ),
    FollowRelationship(
        fromUserId = "3",
        toUserId = "1"
    )
)