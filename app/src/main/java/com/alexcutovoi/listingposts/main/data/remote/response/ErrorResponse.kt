package com.alexcutovoi.listingposts.main.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("documentation_url")
    var documentationUrl: String? = null
)