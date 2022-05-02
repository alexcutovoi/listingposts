package com.alexcutovoi.listingposts.main.domain.usecases

import com.alexcutovoi.listingposts.common.DataState
import com.alexcutovoi.listingposts.main.data.repository.ListingPostsRepository
import com.alexcutovoi.listingposts.main.domain.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPostsUseCase(private val listingPostsRepository: ListingPostsRepository) {
    suspend fun listPosts(): DataState<List<Post>> {
        return withContext(Dispatchers.Default){
            listingPostsRepository.listPosts()
        }
    }
}