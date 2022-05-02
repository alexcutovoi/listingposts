package com.alexcutovoi.listingposts.main.domain.model

abstract class Post(
    @Transient
    open val mainPostId: Int,
    open val relatedPostId: Int,
    open val postBody: String
)
