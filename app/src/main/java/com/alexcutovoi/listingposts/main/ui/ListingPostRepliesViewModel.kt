package com.alexcutovoi.listingposts.main.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexcutovoi.listingposts.ListingPostsApplication
import com.alexcutovoi.listingposts.R
import com.alexcutovoi.listingposts.common.DataState
import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.usecases.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class ListingPostRepliesViewModel(
    private val getPostRepliesUseCase: GetPostRepliesUseCase,
    private var postId: Int) : ViewModel() {

    private val channel = Channel<Int>(capacity = 1)

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val postRepliesLiveData: MutableLiveData<ViewState<List<Post>>> by lazy {
        MutableLiveData<ViewState<List<Post>>>().also {
            init()
        }
    }

    fun getPostReplies(postId: Int){
        viewModelScope.launch {
            val repositoriesData = withContext(Dispatchers.Default) {
                postRepliesLiveData.postValue(ViewState.IsLoading<List<Post>>(null))

                getPostRepliesUseCase.listPostReplies(postId)
            }

            val repositoriesViewData = when(repositoriesData){
                is DataState.Success -> {
                    val list = (repositoriesData.data as List<Post>)

                    ViewState.Success<List<Post>>(list)
                }
                is DataState.Error -> {
                    ViewState.Error<List<Post>>(repositoriesData.exception?.localizedMessage ?:
                        ListingPostsApplication.getApplicationContext().getString(R.string.generic_error))
                }
                else -> {
                    ViewState.Error<List<Post>>(
                        ListingPostsApplication.getApplicationContext().getString(R.string.generic_error))
                }
            }

            postRepliesLiveData.postValue(repositoriesViewData)
        }
    }

    private fun init() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getPostReplies(postId)
            }
        }

        viewModelScope.launch {
            try {
                postId = channel.receive()
                channel.close()
            } catch(e: Exception){
                ViewState.Error<List<Post>>(
                    ListingPostsApplication.getApplicationContext().getString(
                        R.string.generic_error
                    )
                )
            }
        }
    }

    class ListingPostsReplesViewModelFactory(
        private val getPostRepliesUseCase: GetPostRepliesUseCase,
        private var postId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListingPostRepliesViewModel(getPostRepliesUseCase, postId) as T
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}