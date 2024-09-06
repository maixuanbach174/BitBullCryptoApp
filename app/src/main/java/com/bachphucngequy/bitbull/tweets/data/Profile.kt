package com.bachphucngequy.bitbull.tweets.data

data class Profile(
    val id: Int,
    val name: String,
    val bio: String,
    val profileUrl: String,
    var followersCount: Int,
    var followingCount: Int,
    val isOwnProfile: Boolean = false,
    val isFollowing: Boolean = false
)


val sampleProfiles = mutableListOf(
    Profile(
        id = 1,
        name = "Duy Phuc",
        bio = "Hey there, welcome to my Social App page!",
        profileUrl = "https://picsum.photos/200",
        followersCount = 0,
        followingCount = 0,
        isOwnProfile = true,
        isFollowing = true
    ),

    Profile(
        id = 2,
        name = "Xuan Bach",
        profileUrl = "https://picsum.photos/200",
        bio = "Hey there, welcome to my Social App page!",
        followersCount = 0,
        followingCount = 0,
        isOwnProfile = false,
        isFollowing = false
    ),

    Profile(
        id = 3,
        name = "Trong Quy",
        profileUrl = "https://picsum.photos/200",
        bio = "Hey there, welcome to my Social App page!",
        followersCount = 0,
        followingCount = 0,
        isOwnProfile = false,
        isFollowing = false
    ),

    Profile(
        id = 4,
        name = "Danh Nghe",
        profileUrl = "https://picsum.photos/200",
        bio = "Hey there, welcome to my Social App page!",
        followersCount = 0,
        followingCount = 0,
        isOwnProfile = false,
        isFollowing = false
    )
)