package com.alexcutovoi.listingposts.main.data.repository

import com.alexcutovoi.listingposts.common.DataState
import com.alexcutovoi.listingposts.common.utils.Constants
import com.alexcutovoi.listingposts.main.data.remote.ListingPostsApi
import com.alexcutovoi.listingposts.main.data.remote.HttpClient
import com.alexcutovoi.listingposts.main.data.remote.response.*
import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.model.PostReply
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ListingPostsRepositoryImpl(private val httpClient: HttpClient): ListingPostsRepository{
    private val listingPostsApi: ListingPostsApi

    init {
        httpClient.create(Constants.LISTING_POSTS_BASE_URL)
        listingPostsApi = httpClient.createService(ListingPostsApi::class.java)
    }

    override suspend fun listPosts(): DataState<List<Post>> {
        return withContext(Dispatchers.Default){
            try {
                val response = listingPostsApi.listPosts()

                if(response.isSuccessful) {
                    val list = response.body()!!.map { post -> post.toPost() }
                    DataState.Success<List<Post>>(list)
                }
                else {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    DataState.Error<List<Post>>(null, Exception(errorResponse.message))
                }
            } catch (e: HttpException){
                DataState.Error<List<Post>>(null, e)
            }
        }
    }

    override suspend fun listPostReplies(postId: Int): DataState<List<Post>> {
        return withContext(Dispatchers.Default){
            try {
                val response = listingPostsApi.listPostReplies(postId)

                if(response.isSuccessful) {
                    val replies = response.body()!!.map { postReply -> postReply.toPostReply() }
                    DataState.Success<List<Post>>(replies)
                }
                else {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    DataState.Error<List<Post>>(null, Exception(errorResponse.message))
                }
            } catch (e: HttpException){
                DataState.Error<List<Post>>(null, e)
            }
        }
    }
}