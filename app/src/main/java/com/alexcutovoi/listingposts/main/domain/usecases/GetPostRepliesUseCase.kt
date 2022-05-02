package com.alexcutovoi.listingposts.main.domain.usecases

import com.alexcutovoi.listingposts.common.DataState
import com.alexcutovoi.listingposts.main.data.repository.ListingPostsRepository
import com.alexcutovoi.listingposts.main.domain.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPostRepliesUseCase(private val listingPostsRepository: ListingPostsRepository) {
    suspend fun listPostReplies(postId: Int): DataState<List<Post>> {
        return withContext(Dispatchers.Default){
            listingPostsRepository.listPostReplies(postId)
        }
    }
}