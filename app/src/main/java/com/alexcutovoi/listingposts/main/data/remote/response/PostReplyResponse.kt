package com.alexcutovoi.listingposts.main.data.remote.response

import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.model.PostReply
import com.google.gson.annotations.SerializedName

data class PostReplyResponse(
    override val mainPostId: Int,
    @SerializedName("postId")
    override val relatedPostId: Int,
    @SerializedName("body")
    override val postBody: String,
    @SerializedName("name")
    val replierName: String,
    @SerializedName("email")
    val replierEmail: String
) : Post(mainPostId, relatedPostId, postBody)

fun PostReplyResponse.toPostReply(): PostReply {
    return PostReply(mainPostId = mainPostId, relatedPostId = relatedPostId, replierName = replierName, replierEmail = replierEmail, postBody = postBody)
}