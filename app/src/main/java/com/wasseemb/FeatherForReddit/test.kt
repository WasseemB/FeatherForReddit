package com.wasseemb.FeatherForReddit
import com.google.gson.annotations.SerializedName


/**
 * Created by Wasseem on 10/11/2017.
 */

data class DataDJson(
		@SerializedName("kind") val kind: String,
		@SerializedName("data") val data: Data
)

data class Data(
		@SerializedName("modhash") val modhash: String,
		@SerializedName("whitelist_status") val whitelistStatus: String,
		@SerializedName("children") val children: List<Children>,
		@SerializedName("after") val after: Any,
		@SerializedName("before") val before: Any
)

data class Children(
		@SerializedName("kind") val kind: String,
		@SerializedName("data") val data: Data
)

data class Data(
		@SerializedName("domain") val domain: String,
		@SerializedName("approved_at_utc") val approvedAtUtc: Any,
		@SerializedName("banned_by") val bannedBy: Any,
		@SerializedName("media_embed") val mediaEmbed: MediaEmbed,
		@SerializedName("thumbnail_width") val thumbnailWidth: Int,
		@SerializedName("subreddit") val subreddit: String,
		@SerializedName("selftext_html") val selftextHtml: Any,
		@SerializedName("selftext") val selftext: String,
		@SerializedName("likes") val likes: Any,
		@SerializedName("suggested_sort") val suggestedSort: Any,
		@SerializedName("user_reports") val userReports: List<Any>,
		@SerializedName("secure_media") val secureMedia: Any,
		@SerializedName("is_reddit_media_domain") val isRedditMediaDomain: Boolean,
		@SerializedName("link_flair_text") val linkFlairText: Any,
		@SerializedName("id") val id: String,
		@SerializedName("banned_at_utc") val bannedAtUtc: Any,
		@SerializedName("view_count") val viewCount: Any,
		@SerializedName("archived") val archived: Boolean,
		@SerializedName("clicked") val clicked: Boolean,
		@SerializedName("report_reasons") val reportReasons: Any,
		@SerializedName("title") val title: String,
		@SerializedName("num_crossposts") val numCrossposts: Int,
		@SerializedName("saved") val saved: Boolean,
		@SerializedName("can_mod_post") val canModPost: Boolean,
		@SerializedName("is_crosspostable") val isCrosspostable: Boolean,
		@SerializedName("pinned") val pinned: Boolean,
		@SerializedName("score") val score: Int,
		@SerializedName("approved_by") val approvedBy: Any,
		@SerializedName("over_18") val over18: Boolean,
		@SerializedName("hidden") val hidden: Boolean,
		@SerializedName("preview") val preview: Preview,
		@SerializedName("num_comments") val numComments: Int,
		@SerializedName("thumbnail") val thumbnail: String,
		@SerializedName("subreddit_id") val subredditId: String,
		@SerializedName("hide_score") val hideScore: Boolean,
		@SerializedName("edited") val edited: Boolean,
		@SerializedName("link_flair_css_class") val linkFlairCssClass: Any,
		@SerializedName("author_flair_css_class") val authorFlairCssClass: Any,
		@SerializedName("contest_mode") val contestMode: Boolean,
		@SerializedName("gilded") val gilded: Int,
		@SerializedName("locked") val locked: Boolean,
		@SerializedName("downs") val downs: Int,
		@SerializedName("brand_safe") val brandSafe: Boolean,
		@SerializedName("secure_media_embed") val secureMediaEmbed: SecureMediaEmbed,
		@SerializedName("removal_reason") val removalReason: Any,
		@SerializedName("post_hint") val postHint: String,
		@SerializedName("can_gild") val canGild: Boolean,
		@SerializedName("thumbnail_height") val thumbnailHeight: Int,
		@SerializedName("parent_whitelist_status") val parentWhitelistStatus: String,
		@SerializedName("name") val name: String,
		@SerializedName("spoiler") val spoiler: Boolean,
		@SerializedName("permalink") val permalink: String,
		@SerializedName("num_reports") val numReports: Any,
		@SerializedName("whitelist_status") val whitelistStatus: String,
		@SerializedName("stickied") val stickied: Boolean,
		@SerializedName("created") val created: Int,
		@SerializedName("url") val url: String,
		@SerializedName("author_flair_text") val authorFlairText: Any,
		@SerializedName("quarantine") val quarantine: Boolean,
		@SerializedName("author") val author: String,
		@SerializedName("created_utc") val createdUtc: Int,
		@SerializedName("subreddit_name_prefixed") val subredditNamePrefixed: String,
		@SerializedName("distinguished") val distinguished: Any,
		@SerializedName("media") val media: Any,
		@SerializedName("upvote_ratio") val upvoteRatio: Double,
		@SerializedName("mod_reports") val modReports: List<Any>,
		@SerializedName("is_self") val isSelf: Boolean,
		@SerializedName("visited") val visited: Boolean,
		@SerializedName("subreddit_type") val subredditType: String,
		@SerializedName("is_video") val isVideo: Boolean,
		@SerializedName("ups") val ups: Int
)

data class Preview(
		@SerializedName("images") val images: List<Image>,
		@SerializedName("enabled") val enabled: Boolean
)

data class Image(
		@SerializedName("source") val source: Source,
		@SerializedName("resolutions") val resolutions: List<Resolution>,
		@SerializedName("variants") val variants: Variants,
		@SerializedName("id") val id: String
)

data class Source(
		@SerializedName("url") val url: String,
		@SerializedName("width") val width: Int,
		@SerializedName("height") val height: Int
)

data class Resolution(
		@SerializedName("url") val url: String,
		@SerializedName("width") val width: Int,
		@SerializedName("height") val height: Int
)

data class Variants(
)

data class SecureMediaEmbed(
)

data class MediaEmbed(
)