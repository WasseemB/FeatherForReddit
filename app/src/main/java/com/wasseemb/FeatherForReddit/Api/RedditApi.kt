package com.wasseemb.FeatherForReddit.Api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by Wasseem on 01/08/2017.
 */
interface RedditApi {
  @GET("/.json")
  fun getNews(
      @Query("limit") limit: String = "10", @Query("after") after: String? = "")
      : Observable<RedditNewsResponse>

  @GET("r/{subreddit}/.json")
  fun openNewSub(@Path("subreddit") subreddit: String,
      @Query("after") after: String): Observable<RedditNewsResponse>

  @GET("r/{subreddit}/{mode}/.json")
  fun openNewSub(@Path("subreddit") subreddit: String, @Path("mode") mode: String,
      @Query("after") after: String): Observable<RedditNewsResponse>

  @GET("r/{subreddit}/{mode}/.json")
  fun openSubSort(@Path("subreddit") subreddit: String, @Path("mode") mode: String,
      @Query("after") after: String, @Query("sort") sort: String, @Query(
          "t") time: String): Observable<RedditNewsResponse>

  @GET
  fun getPost(@Url permalink: String): Observable<List<Post>>
}