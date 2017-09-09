@file:JvmName("ExtensionsUtils")

package com.wasseemb.FeatherForReddit.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.wasseemb.FeatherForReddit.Api.RedditNewsDataResponse
import com.wasseemb.FeatherForReddit.RetryWithDelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Date
import java.util.concurrent.TimeUnit


/**
 * Created by Wasseem on 03/08/2017.
 */
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
    Glide.with(context).load(imageUrl).into(this)
  }
}