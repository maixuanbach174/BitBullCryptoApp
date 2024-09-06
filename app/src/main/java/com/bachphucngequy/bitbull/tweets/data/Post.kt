package com.bachphucngequy.bitbull.tweets.data

data class Post(
    val id: Int,
    val text: String,
    val imageUrl: String,
    val createdAt: String,
    val likesCount: Int,
    var commentsCount: Int,
    val authorId: Int,
    val authorName: String,
    val authorImageUrl: String,
    val isLiked: Boolean = false,
    val isOwnPost: Boolean = false
)


var samplePosts = mutableListOf(
    Post(
        id = 1,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Sep 02, 2024",
        likesCount = 1,
        commentsCount = 0,
        authorId = 1,
        authorName = "Duy Phuc",
        authorImageUrl = "https://picsum.photos/200"
    ),
    Post(
        id = 2,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        imageUrl = "https://picsum.photos/400",
        createdAt = "May 03, 2023",
        likesCount = 3,
        commentsCount = 0,
        authorId = 2,
        authorName = "Xuan Bach",
        authorImageUrl = "https://picsum.photos/200"
    ),
    Post(
        id = 3,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Apr 12, 2023",
        likesCount = 2,
        commentsCount = 0,
        authorId = 3,
        authorName = "Trong Quy",
        authorImageUrl = "https://picsum.photos/200"
    ),
    Post(
        id = 4,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Mar 31, 2023",
        likesCount = 0,
        commentsCount = 0,
        authorId = 3,
        authorName = "Trong Quy",
        authorImageUrl = "https://picsum.photos/200"
    ),
    Post(
        id = 5,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Jan 30, 2023",
        likesCount = 0,
        commentsCount = 0,
        authorId = 4,
        authorName = "Danh Nghe",
        authorImageUrl = "https://picsum.photos/200"
    ),
    Post(
        id = 6,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        imageUrl = "https://picsum.photos/400",
        createdAt = "Jan 30, 2023",
        likesCount = 0,
        commentsCount = 0,
        authorId = 1,
        authorName = "Duy Phuc",
        authorImageUrl = "https://picsum.photos/200"
    ),
)