package com.alexcutovoi.listingposts.main.ui

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alexcutovoi.listingposts.R
import com.alexcutovoi.listingposts.common.InternetManager
import com.alexcutovoi.listingposts.databinding.FragmentPostRepliesBinding
import com.alexcutovoi.listingposts.main.data.remote.HttpClientImpl
import com.alexcutovoi.listingposts.main.data.repository.ListingPostsRepositoryImpl
import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.usecases.GetPostRepliesUseCase

class PostReplies : Fragment(), BaseView {
    private lateinit var fragmentPostRepliesBinding: FragmentPostRepliesBinding
    private lateinit var listingPostRepliesViewModel: ListingPostRepliesViewModel
    private val repositoriesAdapter: ListingPostsRepositoriesAdapter by lazy {
        ListingPostsRepositoriesAdapter(this.requireContext(), arrayListOf())
    }

    private val postRepliesArgs: PostRepliesArgs by navArgs()
    private var postRepliesId: Int = 0

    private val internetManager: InternetManager by lazy {
        InternetManager(this.requireContext().getSystemService(ConnectivityManager::class.java))
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentPostRepliesBinding = FragmentPostRepliesBinding.inflate(inflater, viewGroup, false)
        setupConfig()
        return fragmentPostRepliesBinding.root
    }

    private fun setupConfig() {
        fragmentPostRepliesBinding.postsReplies.adapter = repositoriesAdapter
        fragmentPostRepliesBinding.postsReplies.layoutManager = LinearLayoutManager(this.requireContext())

        postRepliesId = postRepliesArgs.postId

        listingPostRepliesViewModel = ViewModelProvider(this, ListingPostRepliesViewModel.ListingPostsReplesViewModelFactory(
            GetPostRepliesUseCase(ListingPostsRepositoryImpl(HttpClientImpl())),
            postRepliesId)
        ).get(ListingPostRepliesViewModel::class.java)

        listingPostRepliesViewModel.postRepliesLiveData.observe(viewLifecycleOwner) { viewState ->
            when(viewState){
                is ViewState.Success -> {
                    hideLoading()
                    fragmentPostRepliesBinding.postReplyTitleText.text = this.requireContext().getString(R.string.post_reply_title, postRepliesArgs.postTitle)
                    repositoriesAdapter.addItems((viewState.data as List<Post>))
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

        fragmentPostRepliesBinding.tryAgainButton.setOnClickListener {
            getPostReplies(postRepliesId)
        }
    }

    override fun showLoading() {
        fragmentPostRepliesBinding.reloadContainer.visibility = View.VISIBLE
        fragmentPostRepliesBinding.dataContainer.visibility = View.GONE
    }
    override fun hideLoading() {
        fragmentPostRepliesBinding.reloadContainer.visibility = View.GONE
        fragmentPostRepliesBinding.dataContainer.visibility = View.VISIBLE
    }

    private fun showReloadButton() {
        fragmentPostRepliesBinding.tryAgainButton.visibility = View.VISIBLE
    }

    private fun hideReloadButton(){
        fragmentPostRepliesBinding.tryAgainButton.visibility = View.GONE
    }

    private fun getPostReplies(postRepliesId: Int) {
        if(internetManager.checkInternet()) {
            listingPostRepliesViewModel.getPostReplies(postRepliesId)
        } else {
            Toast.makeText(this.requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show()
            hideLoading()
            showReloadButton()
        }
    }
}