package com.wasseemb.FeatherForReddit.Api

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Wasseem on 01/08/2017.
 */
class RestApi() {
  private val redditApi: RedditApi

  init {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    redditApi = retrofit.create(RedditApi::class.java)
  }

  fun getNews(limit: String, after: String?): Observable<RedditNewsResponse> {
    return redditApi.getTop(limit, after)
  }

  fun openNewSub(subreddit: String,
      after: String = ""): Observable<RedditNewsResponse> = redditApi.openNewSub(subreddit, after)

}