package com.alexcutovoi.listingposts.main.data.remote

import com.alexcutovoi.listingposts.main.data.remote.response.PostReplyResponse
import com.alexcutovoi.listingposts.main.data.remote.response.PostResponse
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Path

interface ListingPostsApi{

    @GET("posts")
    suspend fun listPosts(
    ) : Response<List<PostResponse>>

    @GET("posts/{postId}/comments")
    suspend fun listPostReplies(
        @Path("postId")postId: Int
    ) : Response<List<PostReplyResponse>>
}