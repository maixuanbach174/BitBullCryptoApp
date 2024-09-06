package com.bachphucngequy.bitbull.tweets.data

// fromUserId is following toUserId
data class FollowRelationship(
    val fromUserId: Int,
    val toUserId: Int,
    val followDate: String
)

val sampleFollowRelationship: MutableList<FollowRelationship> = mutableListOf(
    FollowRelationship(
        fromUserId = 1,
        toUserId = 2,
        followDate = "Sep 1, 2024"
    ),
    FollowRelationship(
        fromUserId = 2,
        toUserId = 3,
        followDate = "Sep 1, 2024"
    ),
    FollowRelationship(
        fromUserId = 3,
        toUserId = 1,
        followDate = "Sep 1, 2024"
    )
)