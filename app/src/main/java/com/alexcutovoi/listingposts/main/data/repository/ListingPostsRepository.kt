package com.alexcutovoi.listingposts.main.data.repository

import com.alexcutovoi.listingposts.common.DataState
import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.model.UserPost
import com.alexcutovoi.listingposts.main.domain.model.PostReply

interface ListingPostsRepository{
    suspend fun listPosts() : DataState<List<Post>>
    suspend fun listPostReplies(postId: Int) : DataState<List<Post>>
}