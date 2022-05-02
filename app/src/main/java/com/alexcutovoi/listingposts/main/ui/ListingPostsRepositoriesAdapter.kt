package com.alexcutovoi.listingposts.main.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.alexcutovoi.listingposts.R
import com.alexcutovoi.listingposts.main.domain.model.Post
import com.alexcutovoi.listingposts.main.domain.model.PostReply
import com.alexcutovoi.listingposts.main.domain.model.UserPost
import kotlin.random.Random

class ListingPostsRepositoriesAdapter(
    private val context: Context,
    posts: MutableList<Post>,
    private var callback: (() -> Unit)? = null,
    private val anotherCallback: ((postId: Int, postTitle: String) -> Unit)? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var posts = mutableListOf<Post>()

    init {
        this.posts = posts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = getViewLayout(posts)
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        return if(layout == R.layout.user_post_layout){
            return PostsViewHolder(view)
        } else {
            PostRepliesViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(posts[position]){
            is UserPost -> {
                val postsViewHolder = holder as PostsViewHolder
                val userPost = posts[position] as UserPost
                postsViewHolder.postTitle.text = userPost.postTitle
                postsViewHolder.postComment.text = userPost.postBody
                postsViewHolder.postContainer.setOnClickListener {
                    anotherCallback?.invoke(userPost.relatedPostId, userPost.postTitle)
                }
            }
            is PostReply -> {
                val postRepliesViewHolder = holder as PostRepliesViewHolder
                val postReply = posts[position] as PostReply

                val randomNumber = Random.nextInt(1, 5)
                postRepliesViewHolder.postReplyProfileImage.setImageResource(
                    if(randomNumber <= 3) R.drawable.ic_male_avatar else R.drawable.ic_female_avatar
                )

                postRepliesViewHolder.postReplyUserName.text = postReply.replierName
                postRepliesViewHolder.postReplyUserEmail.text = postReply.replierEmail
                postRepliesViewHolder.postReplyText.text = postReply.postBody
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.post_title)
        val postComment: TextView = itemView.findViewById(R.id.post_body)
        val postContainer: View = itemView.findViewById(R.id.post_container)
    }

    inner class PostRepliesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postReplyProfileImage: AppCompatImageView = itemView.findViewById(R.id.post_reply_profile_image)
        var postReplyUserName: TextView = itemView.findViewById(R.id.post_reply_user_name)
        var postReplyUserEmail: TextView = itemView.findViewById(R.id.post_reply_user_email)
        var postReplyText: TextView = itemView.findViewById(R.id.post_reply_text)
    }

    private fun getViewLayout(posts: List<Post>): Int {
        val viewLayout = if(posts.isEmpty() || posts[0] is UserPost){
            R.layout.user_post_layout
        } else if(posts[0] is PostReply) {
            R.layout.post_reply_layout
        } else {
            R.layout.user_post_layout
        }

        return viewLayout
    }

    fun addItems(posts: List<Post>) {
        this.posts.addAll(posts)
        notifyItemRangeChanged(0, this.posts.size-1)
    }
}