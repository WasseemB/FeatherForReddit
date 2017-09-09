package com.wasseemb.FeatherForReddit.Api

import com.squareup.moshi.Json
import com.wasseemb.FeatherForReddit.model.DisplayableItem


/**
 * Created by Wasseem on 01/08/2017.
 */



//DisplayableItem is empty interface so that DelegateManager can work (Passing Different Types)
data class RedditNewsResponse (val data: RedditDataResponse) :DisplayableItem


data class RedditDataResponse(
    @Json(name = "children")
    val children: List<RedditChildrenResponse>,
    @Json(name = "after")
    val after: String?,
    @Json(name = "before")
    val before: String?
)

data class RedditChildrenResponse(val data: RedditNewsDataResponse) :DisplayableItem

data class RedditNewsDataResponse(
    @Json(name = "author")
    val author: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "subreddit")
    val subreddit: String,
    @Json(name = "num_comments")
    val num_comments: Double,
    @Json(name = "created")
    val created: Long,
    @Json(name = "thumbnail")
    val thumbnail: String,
    @Json(name = "url")
    val url: String,
    @Json(name = "score")
    val score: Double,
    @Json(name = "domain")
    val domain: String,
    @Json(name = "created_utc")
    val created_utc: Long,
    @Json(name = "preview")
    val preview: Preview
)

data class Preview(
    @Json(name = "images")
    val images: List<Image>,
    @Json(name = "enabled")
    val enabled: Boolean
)

data class Image(
    @Json(name = "source")
    var source: Source,
    @Json(name = "resolutions")
    var resolutions: List<Resolution>,
    @Json(name = "id")
    var id: String? = null
)

data class Resolution(

    @Json(name = "url")
    var url: String,
    @Json(name = "width")
    var width: Int,
    @Json(name = "height")
    var height: Int

)

data class Source(

    @Json(name = "url")
    var url: String,
    @Json(name = "width")
    var width: Int,
    @Json(name = "height")
    var height: Int

)


