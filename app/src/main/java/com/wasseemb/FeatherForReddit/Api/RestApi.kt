package com.wasseemb.FeatherForReddit.Api

import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Wasseem on 01/08/2017.
 */
class RestApi() {
  private val redditApi: RedditApi

  init {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    val httpClient = OkHttpClient.Builder()
    httpClient.addInterceptor(logging)

    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.reddit.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        //  .client(httpClient.build())
        .build()

    redditApi = retrofit.create(RedditApi::class.java)
  }

//  fun getNews(limit: String = "", mode: String = "hot",
//      after: String? = ""): Observable<RedditNewsResponse> = redditApi.getNews(limit, mode, after)

  fun getNews(limit: String? = null, mode: String = "hot", after: String? = null,
      time: String? = null): Observable<RedditNewsResponse> = redditApi.getNewsSort(mode, limit,
      after,
      mode, time)


  fun getPost(permalink: String): Observable<List<Post>> = redditApi.getPost(
      permalink + ".json?limit=10")


//  fun openNewSub(subreddit: String,
//      after: String = ""): Observable<RedditNewsResponse> = redditApi.openNewSub(subreddit, after)

//  fun openNewSub(subreddit: String,
//      mode: String = "hot",
//      after: String = ""): Observable<RedditNewsResponse> = redditApi.openNewSub(subreddit, mode,
//      after)

  fun openSub(subreddit: String,
      mode: String,
      after: String = "", time: String?): Observable<RedditNewsResponse> = redditApi.openSub(
      subreddit, mode,
      after, mode, time)


}