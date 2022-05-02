package com.alexcutovoi.listingposts.main.domain.model

data class UserPost(
    override val mainPostId: Int,
    override val relatedPostId: Int,
    override val postBody: String,
    val postTitle: String
) : Post(mainPostId, relatedPostId, postBody)