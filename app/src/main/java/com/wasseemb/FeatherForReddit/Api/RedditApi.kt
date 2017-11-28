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
//  @GET("{mode}/.json")
//  fun getNews(
//      @Path("mode") mode: String, @Query("limit") limit: String = "10", @Query(
//          "after") after: String? = "")
//      : Observable<RedditNewsResponse>

  @GET("/{mode}/.json")
  fun getNewsSort(
      @Path("mode") mode: String, @Query("limit") limit: String?, @Query(
          "after") after: String?, @Query("sort") sort: String?, @Query(
          "t") time: String?)
      : Observable<RedditNewsResponse>

  @GET("r/{subreddit}/{mode}/.json")
  fun openSub(@Path("subreddit") subreddit: String, @Path("mode") mode: String,
      @Query("after") after: String, @Query("sort") sort: String, @Query(
          "t") time: String?): Observable<RedditNewsResponse>

  @GET
  fun getPost(@Url permalink: String): Observable<List<Post>>
}