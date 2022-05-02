package com.alexcutovoi.listingposts

import android.app.Application
import android.content.Context

class ListingPostsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        APP_CONTEXT = applicationContext
    }

    companion object {
        private lateinit var APP_CONTEXT: Context

        fun getApplicationContext(): Context {
            return ListingPostsApplication.APP_CONTEXT
        }
    }
}