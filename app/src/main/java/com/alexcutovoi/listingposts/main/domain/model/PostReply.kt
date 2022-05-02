package com.alexcutovoi.listingposts.main.domain.model

data class PostReply(
    override val mainPostId: Int,
    override val relatedPostId: Int,
    override val postBody: String,
    val replierName: String,
    val replierEmail: String
) : Post(mainPostId, relatedPostId, postBody)