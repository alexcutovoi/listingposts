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

class ListingPostsViewModel(
    private val getPostsUseCase: GetPostsUseCase) : ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val repositoriesLiveData: MutableLiveData<ViewState<List<Post>>> by lazy {
        MutableLiveData<ViewState<List<Post>>>().also {
            init()
        }
    }

    fun getPosts(){
        viewModelScope.launch {
            val repositoriesData = withContext(Dispatchers.Default) {
                repositoriesLiveData.postValue(ViewState.IsLoading<List<Post>>(null))

                getPostsUseCase.listPosts()
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

            repositoriesLiveData.postValue(repositoriesViewData)
        }
    }

    private fun init() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                getPosts()
            }
        }
    }

    class ListingPostsViewModelFactory(
        private val getPostsUseCase: GetPostsUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListingPostsViewModel(getPostsUseCase) as T
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}