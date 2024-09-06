package com.bachphucngequy.bitbull.tweets.data.repository

interface FollowsRepository {
    suspend fun followOrUnfollow(followedUserId: Int, shouldFollow: Boolean): Boolean
}