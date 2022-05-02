package com.alexcutovoi.listingposts.main.data.remote.response

import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.model.UserPost
import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("userId")
    override val mainPostId: Int,
    @SerializedName("id")
    override val relatedPostId: Int,
    @SerializedName("body")
    override val postBody: String,
    @SerializedName("title")
    val postTitle: String
) : Post(mainPostId, relatedPostId, postBody)

fun PostResponse.toPost(): Post {
    return UserPost(mainPostId = mainPostId, relatedPostId = relatedPostId, postTitle = postTitle, postBody = postBody)
}