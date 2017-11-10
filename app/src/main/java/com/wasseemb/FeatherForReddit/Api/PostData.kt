package com.wasseemb.FeatherForReddit.Api

import com.google.gson.annotations.SerializedName


/**
 * Created by Wasseem on 07/10/2017.
 */

data class Post(
    @SerializedName( "kind") var kind: String = "", //Listing
    @SerializedName( "data") var data: PostDataResponse
)

data class PostDataResponse(
    @SerializedName( "modhash")
    var modhash: String = "",
    @SerializedName( "whitelist_status")
    var whitelistStatus: String = "",
    @SerializedName( "children")
    val children: List<PostChildrenResponse>,
    @SerializedName( "after")
    val after: String?,
    @SerializedName( "before")
    val before: String?
)

data class PostChildrenResponse(val data: PostNewsDataResponse)


data class PostNewsDataResponse(
    @SerializedName( "domain") var domain: String = "", //imgur.com
    @SerializedName( "subreddit") var subreddit: String = "", //pics
    @SerializedName( "selftext") var selftext: String = "",
    @SerializedName( "title") var title: String = "",
    @SerializedName( "thumbnail") var thumbnail: String = "",
    @SerializedName( "num_comments") val num_comments: Double,
    @SerializedName( "score") val score: Double,
    @SerializedName( "author") val author: String,
    @SerializedName( "created_utc") val created_utc: Long

)
