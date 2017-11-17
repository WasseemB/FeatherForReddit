@file:JvmName("ExtensionsUtils")

package com.wasseemb.FeatherForReddit.extensions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Adapter.UserAdapterDelegate
import com.wasseemb.FeatherForReddit.Adapter.UserAdapterMiniDelegate
import com.wasseemb.FeatherForReddit.Api.RedditChildrenResponse
import com.wasseemb.FeatherForReddit.Api.RedditNewsDataResponse
import com.wasseemb.FeatherForReddit.DetailViewActivity
import com.wasseemb.FeatherForReddit.ImageViewActivity
import com.wasseemb.FeatherForReddit.RetryWithDelay
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.get
import com.wasseemb.FeatherForReddit.extensions.UrlType.GIF
import com.wasseemb.FeatherForReddit.extensions.UrlType.IMAGE
import com.wasseemb.FeatherForReddit.extensions.UrlType.LINK
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Date
import java.util.concurrent.TimeUnit


/**
 * Created by Wasseem on 03/08/2017.
 */

private var subscribe: Disposable? = null

enum class UrlType {
  IMAGE, LINK, GIF, VIDEO
}

fun setupItemClick(userAdapter: UserAdapter, context: Context) {
  val prefs = PreferenceHelper.defaultPrefs(context)
  val value: String? = prefs["card", "mini"] //getter
  if (value.equals("mini"))
    setupItemClickMini(userAdapter, context)
  else
    setupItemClickLarge(userAdapter, context)
}

fun setupItemClickMini(userAdapter: UserAdapter, context: Context) {

  subscribe = (userAdapter.adapterDelegate as UserAdapterMiniDelegate).clickEvent
      .subscribe({
        val response = it as RedditChildrenResponse
        val intent = Intent(context, DetailViewActivity::class.java)
        intent.putExtra("permalink", response.data.permalink)
//          val options = makeSceneTransitionAnimation(this, this.card_view,
//              "robot_trans").toBundle()
        context.startActivity(intent)
        //Toast.makeText(this, "Clicked on ${response.data.title}", Toast.LENGTH_LONG).show()
      }, { e -> Log.d("SubredditActivity", e.toString()) },
          { Log.d("SubredditActivity", "Completed") })
}

fun setupItemClickLarge(userAdapter: UserAdapter, context: Context) {

  subscribe = (userAdapter.adapterDelegate as UserAdapterDelegate).clickEvent
      .subscribe({
        val response = it as RedditChildrenResponse
        val intent = Intent(context, DetailViewActivity::class.java)
        intent.putExtra("permalink", response.data.permalink)
//          val options = makeSceneTransitionAnimation(this, this.card_view,
//              "robot_trans").toBundle()
        context.startActivity(intent)
        //Toast.makeText(this, "Clicked on ${response.data.title}", Toast.LENGTH_LONG).show()
      }, { e -> Log.d("SubredditActivity", e.toString()) },
          { Log.d("SubredditActivity", "Completed") })
}


fun setupImageClick(userAdapter: UserAdapter, context: Context) {
  val prefs = PreferenceHelper.defaultPrefs(context)
  val value: String? = prefs["card", "mini"] //getter
  if (value.equals("mini"))
    setupImageClickMini(userAdapter, context)
  else
    setupImageClickLarge(userAdapter, context)
}

fun setupImageClickMini(userAdapter: UserAdapter, context: Context) {
  subscribe = (userAdapter.adapterDelegate as UserAdapterMiniDelegate).imageClickEvent
      .subscribe({
        val response = it as RedditChildrenResponse
        val intent = Intent(context, ImageViewActivity::class.java)
        intent.putExtra("image", response.data.url)
        context.startActivity(intent)
        //Toast.makeText(this, "Clicked on ${response.data.title}", Toast.LENGTH_LONG).show()
      })
}

fun setupImageClickLarge(userAdapter: UserAdapter, context: Context) {
  subscribe = (userAdapter.adapterDelegate as UserAdapterDelegate).imageClickEvent
      .subscribe({
        val response = it as RedditChildrenResponse
        val intent = Intent(context, ImageViewActivity::class.java)
        intent.putExtra("image", response.data.url)
        context.startActivity(intent)
        //Toast.makeText(this, "Clicked on ${response.data.title}", Toast.LENGTH_LONG).show()
      })
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Date.timeFromNow(): String {
  val date = Date()
  val duration = date.time - this.time
  val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
  val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration)
  val diffInHours = TimeUnit.MILLISECONDS.toHours(duration)
  val diffInDays = TimeUnit.MILLISECONDS.toDays(duration)
  if (diffInSeconds < 60) return diffInSeconds.toString() + "s"
  if (diffInMinutes < 60) return diffInMinutes.toString() + "m"
  if (diffInHours < 24) return diffInHours.toString() + "h"
  if (diffInDays < 31) return diffInHours.toString() + "d"
  return "Out of range"
}

fun Double.roundTwoDigits(): String {
  val df = DecimalFormat("#.##")
  df.roundingMode = RoundingMode.CEILING
  return df.format(this)
}

fun handleImage(redditItem: RedditNewsDataResponse): String? {

  if (redditItem.preview != null) {
    if (!redditItem.url.contains("i.redd.it") && redditItem.url.contains(".gif")) {
      return redditItem.url.replace(".gif", "h.gif")
    } else if (redditItem.url.contains("gfycat")) {
      return redditItem.url.replace("gfycat", "thumbs.gfycat").plus("-poster.jpg")

    } else
      return redditItem.preview.images[0].source.url


  }
  return null
}


fun urlType(url: String): UrlType {
  if (url.endsWith("gif") || url.endsWith("gifv") || url.contains("gfycat"))
    return GIF
  else if (url.endsWith("png") || url.endsWith(".jpg") || url.endsWith("jpeg"))
    return IMAGE
  else
    return LINK
}


fun numToK(number: Double): String {
  if (number > 1000) return (number / 1000).roundTwoDigits() + "k"
  else return number.toInt().toString()

}


fun <T> Observable<T>.applySchedulersWithDelay(): Observable<T> {
  return subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).retryWhen(
      RetryWithDelay(3, 2))

}

fun ImageView.loadImg(imageUrl: String?) {
  if (imageUrl.isNullOrEmpty()) {
    val relative: RelativeLayout = this.parent as RelativeLayout
    relative.visibility = View.GONE
  } else {
    Picasso.with(context)
        .load(imageUrl)
        .fit()
        .centerCrop()
        .into(this)
    //Glide.with(context).load(imageUrl).into(this)
  }


}

fun ImageView.loadImage(imageUrl: String?) {

  Picasso.with(context)
      .load(imageUrl)
      .fit()
      .centerCrop()
      .into(this)
  //Glide.with(context).load(imageUrl).into(this)


}