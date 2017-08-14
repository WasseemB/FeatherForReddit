package com.wasseemb.FeatherForReddit.Api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Wasseem on 01/08/2017.
 */
interface RedditApi {
  @GET("/.json")
  fun getTop(
      @Query("limit") limit: String = "10", @Query("after") after: String? = "")
      : Observable<RedditNewsResponse>

  @GET("r/{subreddit}/.json") fun openNewSub(@Path("subreddit") subreddit: String,
      @Query("after") after: String): Observable<RedditNewsResponse>
}