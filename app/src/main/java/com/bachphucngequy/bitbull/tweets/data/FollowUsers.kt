package com.bachphucngequy.bitbull.tweets.data

data class FollowsUser(
    val id: Int,
    val name: String,
    val bio: String = "Hey there, welcome to my social app page!",
    val profileUrl: String,
    val isFollowing: Boolean = false
)

val sampleUsers = listOf(
    FollowsUser(
        id = 1,
        name = sampleProfiles[0].name,
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 2,
        name = sampleProfiles[1].name,
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 3,
        name = sampleProfiles[2].name,
        profileUrl = "https://picsum.photos/200"
    ),
    FollowsUser(
        id = 4,
        name = sampleProfiles[3].name,
        profileUrl = "https://picsum.photos/200"
    )
)