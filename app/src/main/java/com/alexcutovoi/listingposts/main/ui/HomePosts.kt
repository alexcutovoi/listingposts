package com.alexcutovoi.listingposts.main.ui

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexcutovoi.listingposts.R
import com.alexcutovoi.listingposts.main.domain.usecases.GetPostsUseCase
import com.alexcutovoi.listingposts.common.InternetManager
import com.alexcutovoi.listingposts.databinding.FragmentHomePostsBinding
import com.alexcutovoi.listingposts.main.data.remote.HttpClientImpl
import com.alexcutovoi.listingposts.main.data.repository.ListingPostsRepositoryImpl
import com.alexcutovoi.listingposts.main.domain.model.Post

class HomePosts : Fragment(), BaseView {
    private lateinit var fragmentHomePostsBinding: FragmentHomePostsBinding
    private lateinit var listingPostsViewModel: ListingPostsViewModel
    private val listingPostsRepositoriesAdapter: ListingPostsRepositoriesAdapter by lazy {
        ListingPostsRepositoriesAdapter(this.requireContext(), mutableListOf(), anotherCallback = { postId, postTitle ->
            goToPostReplies(postId, postTitle)
        })
    }

    private val internetManager: InternetManager by lazy {
        InternetManager(this.requireContext().getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentHomePostsBinding = FragmentHomePostsBinding.inflate(inflater, viewGroup, false)
        setupConfig()
        return fragmentHomePostsBinding.root
    }

    private fun setupConfig() {
        fragmentHomePostsBinding.posts.adapter = listingPostsRepositoriesAdapter
        fragmentHomePostsBinding.posts.layoutManager = LinearLayoutManager(this.requireContext())

        listingPostsViewModel = ViewModelProvider(this, ListingPostsViewModel.ListingPostsViewModelFactory(
            GetPostsUseCase(ListingPostsRepositoryImpl(HttpClientImpl())))
        ).get(ListingPostsViewModel::class.java)

        listingPostsViewModel.repositoriesLiveData.observe(viewLifecycleOwner) { viewState ->
            when(viewState){
                is ViewState.Success -> {
                    hideLoading()
                    listingPostsRepositoriesAdapter.addItems((viewState.data as List<Post>))
                }
                is ViewState.IsLoading -> {
                    showLoading()
                    hideReloadButton()
                }
                is ViewState.Error -> {
                    hideLoading()
                    hideReloadButton()
                    Toast.makeText(this.requireContext(), getString(R.string.generic_error), Toast.LENGTH_LONG).show()
                }
                else -> {
                    hideLoading()
                    hideReloadButton()
                    Toast.makeText(this.requireContext(), getString(R.string.generic_error), Toast.LENGTH_LONG).show()
                }
            }
        }

        fragmentHomePostsBinding.tryAgainButton.setOnClickListener {
            getPosts()
        }
    }

    override fun showLoading() {
        fragmentHomePostsBinding.reloadContainer.visibility = View.VISIBLE
    }
    override fun hideLoading() {
        fragmentHomePostsBinding.reloadContainer.visibility = View.GONE
    }

    private fun showReloadButton(){
        fragmentHomePostsBinding.tryAgainButton.visibility = View.VISIBLE
    }

    private fun hideReloadButton(){
        fragmentHomePostsBinding.tryAgainButton.visibility = View.GONE
    }

    private fun getPosts() {
        if(internetManager.checkInternet()) {
            listingPostsViewModel.getPosts()
        } else {
            Toast.makeText(this.requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
            hideLoading()
            showReloadButton()
        }
    }

    private fun goToPostReplies(postId: Int, postTitle: String) {
        Navigation.findNavController(fragmentHomePostsBinding.root).navigate(
            HomePostsDirections.postRepliesAction(postId, postTitle)
        )
    }
}