package com.wasseemb.FeatherForReddit.Api

import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.google.gson.annotations.SerializedName


/**
 * Created by Wasseem on 01/08/2017.
 */



//DisplayableItem is empty interface so that DelegateManager can work (Passing Different Types)
data class RedditNewsResponse (val data: RedditDataResponse) :DisplayableItem


data class RedditDataResponse(
    @SerializedName( "modhash")
    var modhash: String = "",
    @SerializedName( "whitelist_status")
    var whitelistStatus: String = "",
    @SerializedName( "children")
    val children: List<RedditChildrenResponse>,
    @SerializedName( "after")
    val after: String?,
    @SerializedName( "before")
    val before: String?
)

data class RedditChildrenResponse(val data: RedditNewsDataResponse) :DisplayableItem

data class RedditNewsDataResponse(
    @SerializedName( "author")
    val author: String,
    @SerializedName( "title")
    val title: String,
    @SerializedName( "subreddit")
    val subreddit: String,
    @SerializedName( "num_comments")
    val num_comments: Double,
    @SerializedName( "created")
    val created: Long,
    @SerializedName( "thumbnail")
    val thumbnail: String,
    @SerializedName( "url")
    val url: String,
    @SerializedName( "score")
    val score: Double,
    @SerializedName( "domain")
    val domain: String,
    @SerializedName( "created_utc")
    val created_utc: Long,
    @SerializedName( "preview")
    val preview: Preview,
    @SerializedName( "permalink")
    val permalink: String
)

data class Preview(
    @SerializedName( "images")
    val images: List<Image>,
    @SerializedName( "enabled")
    val enabled: Boolean
)

data class Image(
    @SerializedName( "source")
    var source: Source,
    @SerializedName( "resolutions")
    var resolutions: List<Resolution>,
    @SerializedName( "id")
    var id: String? = null
)

data class Resolution(

    @SerializedName( "url")
    var url: String,
    @SerializedName( "width")
    var width: Int,
    @SerializedName( "height")
    var height: Int

)

data class Source(

    @SerializedName( "url")
    var url: String,
    @SerializedName( "width")
    var width: Int,
    @SerializedName( "height")
    var height: Int

)


